package io.leego.unique.mybatis.support.interceptor;

import io.leego.unique.client.UniqueClient;
import io.leego.unique.common.util.ReflectUtils;
import io.leego.unique.mybatis.support.annotation.AutoSeq;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Yihleego
 */
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class AutoSeqInterceptor implements Interceptor {
    private final UniqueClient uniqueClient;

    public AutoSeqInterceptor(UniqueClient uniqueClient) {
        this.uniqueClient = uniqueClient;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (args == null || args.length < 2 || args[1] == null) {
            return invocation.proceed();
        }
        handle(args[1]);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // ignored
    }

    private void handle(Object parameter) throws NoSuchFieldException, IllegalAccessException {
        if (parameter instanceof Map) {
            List<Meta> metas = new ArrayList<>();
            Map<?, ?> map = (Map<?, ?>) parameter;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Collection) {
                    Collection<?> objects = (Collection<?>) value;
                    for (Object object : objects) {
                        Meta meta = getMeta(object);
                        if (meta != null) {
                            metas.add(meta);
                        }
                    }
                    if (!metas.isEmpty()) {
                        break;
                    }
                }
            }
            setSeq(metas);
        } else if (parameter instanceof Collection) {
            List<Meta> metas = new ArrayList<>();
            Collection<?> objects = (Collection<?>) parameter;
            for (Object object : objects) {
                Meta meta = getMeta(object);
                if (meta != null) {
                    metas.add(meta);
                }
            }
            if (!metas.isEmpty()) {
                setSeq(metas);
            }
        } else {
            Meta meta = getMeta(parameter);
            if (meta != null) {
                setSeq(Collections.singletonList(meta));
            }
        }
    }

    private Meta getMeta(Object object) throws NoSuchFieldException, IllegalAccessException {
        if (object == null) {
            return null;
        }
        AutoSeq tas = object.getClass().getAnnotation(AutoSeq.class);
        if (tas != null) {
            String key = getAlias(tas.value(), tas.key());
            if (hasLength(key) && hasLength(tas.field())) {
                Field field = ReflectUtils.getDeepField(object, tas.field());
                if (isSettable(object, field)) {
                    return new Meta(key, field, object);
                }
            }
            return null;
        }

        Field[] fields = ReflectUtils.getDeepFields(object, true);
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            if (annotations.length == 0) {
                continue;
            }
            for (Annotation annotation : annotations) {
                if (annotation instanceof AutoSeq) {
                    AutoSeq fas = (AutoSeq) annotation;
                    String key = getAlias(fas.value(), fas.key());
                    if (hasLength(key) && isSettable(object, field)) {
                        return new Meta(key, field, object);
                    }
                    return null;
                }
            }
        }
        return null;
    }

    private boolean isSettable(Object object, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object value = read(object, field);
        if (value != null) {
            // The value already exists.
            return false;
        }
        Class<?> type = field.getType();
        return Long.class.isAssignableFrom(type)
                || Integer.class.isAssignableFrom(type)
                || Short.class.isAssignableFrom(type)
                || BigInteger.class.isAssignableFrom(type)
                || BigDecimal.class.isAssignableFrom(type)
                || String.class.isAssignableFrom(type);
    }

    private void setSeq(List<Meta> list) throws IllegalAccessException {
        if (list.size() == 1) {
            setSeq(list.get(0), uniqueClient.next(list.get(0).getKey()));
        } else {
            Map<String, List<Meta>> group = list.stream().collect(Collectors.groupingBy(Meta::getKey));
            for (Map.Entry<String, List<Meta>> entry : group.entrySet()) {
                String key = entry.getKey();
                List<Meta> metas = entry.getValue();
                LinkedList<Long> sequences = uniqueClient.next(key, metas.size());
                for (Meta meta : metas) {
                    setSeq(meta, sequences.remove());
                }
            }
        }
    }

    private void setSeq(Meta meta, Long sequence) throws IllegalAccessException {
        Object object = meta.getObject();
        Field field = meta.getField();
        Class<?> type = field.getType();
        if (Long.class.isAssignableFrom(type)) {
            write(object, field, sequence);
        } else if (Integer.class.isAssignableFrom(type)) {
            write(object, field, sequence.intValue());
        } else if (Short.class.isAssignableFrom(type)) {
            write(object, field, sequence.shortValue());
        } else if (BigInteger.class.isAssignableFrom(type)) {
            write(object, field, BigInteger.valueOf(sequence));
        } else if (BigDecimal.class.isAssignableFrom(type)) {
            write(object, field, BigDecimal.valueOf(sequence));
        } else if (String.class.isAssignableFrom(type)) {
            write(object, field, String.valueOf(sequence));
        }
    }

    private Object read(Object o, Field field) throws IllegalAccessException {
        return ReflectUtils.getFieldValue(o, field);
    }

    private void write(Object o, Field field, Object value) throws IllegalAccessException {
        ReflectUtils.setFieldValue(o, field, value);
    }

    private boolean hasLength(String text) {
        return text != null && !text.isEmpty();
    }

    private String getAlias(String s1, String s2) {
        return hasLength(s1) ? s1 : s2;
    }

    private static class Meta {
        private String key;
        private Field field;
        private Object object;

        public Meta(String key, Field field, Object object) {
            this.key = key;
            this.field = field;
            this.object = object;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

}
