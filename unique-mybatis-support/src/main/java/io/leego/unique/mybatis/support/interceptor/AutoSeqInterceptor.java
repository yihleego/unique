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
import java.util.Properties;

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
        if (args == null || args.length < 2) {
            return invocation.proceed();
        }
        Object parameter = args[1];
        AutoSeq tas = parameter.getClass().getAnnotation(AutoSeq.class);
        if (tas != null) {
            String key = getAlias(tas.value(), tas.key());
            if (hasLength(key) && hasLength(tas.field())) {
                Field field = ReflectUtils.getDeepField(parameter, tas.field());
                boolean flag = setSeqValue(key, field, parameter);
                if (flag) {
                    return invocation.proceed();
                }
            }
        }
        Field[] fields = ReflectUtils.getDeepFields(parameter, true);
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            if (annotations.length == 0) {
                continue;
            }
            for (Annotation annotation : annotations) {
                if (annotation instanceof AutoSeq) {
                    AutoSeq fas = (AutoSeq) annotation;
                    String key = getAlias(fas.value(), fas.key());
                    if (hasLength(key)) {
                        setSeqValue(key, field, parameter);
                    }
                    return invocation.proceed();
                }
            }
        }
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

    private boolean setSeqValue(String key, Field field, Object parameter) throws IllegalAccessException {
        if (field == null) {
            return true;
        }
        field.setAccessible(true);
        Object value = read(parameter, field);
        if (value != null) {
            // The value already exists.
            return true;
        }
        Class<?> type = field.getType();
        if (!Number.class.isAssignableFrom(type)) {
            return false;
        }
        if (Long.class.isAssignableFrom(type)) {
            write(parameter, field, nextValue(key));
        } else if (Integer.class.isAssignableFrom(type)) {
            write(parameter, field, nextValue(key).intValue());
        } else if (Short.class.isAssignableFrom(type)) {
            write(parameter, field, nextValue(key).shortValue());
        } else if (BigInteger.class.isAssignableFrom(type)) {
            write(parameter, field, BigInteger.valueOf(nextValue(key)));
        } else if (BigDecimal.class.isAssignableFrom(type)) {
            write(parameter, field, BigDecimal.valueOf(nextValue(key)));
        } else {
            return false;
        }
        return true;
    }

    private Long nextValue(String key) {
        return uniqueClient.next(key);
    }

    private Object read(Object o, Field field) throws IllegalAccessException {
        return ReflectUtils.getFieldValue(o, field);
    }

    private void write(Object o, Field field, Object value) throws IllegalAccessException {
        ReflectUtils.setFieldValue(o, field, value);
    }

    private boolean hasLength(String text) {
        return text != null && text.length() > 0;
    }

    private String getAlias(String s1, String s2) {
        return hasLength(s1) ? s1 : s2;
    }

}
