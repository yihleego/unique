package io.leego.unique.mybatis.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for setting sequence automatically.
 * Usage:
 * <pre>
 * class Entity {
 *     <code>@AutoSeq("key")</code>
 *     Long id;
 * }
 * </pre>
 * <p>
 * <pre>
 * <code>@AutoSeq(key = "key", field = "id")</code>
 * class Entity {
 *     Long id;
 * }
 * </pre>
 * @author Yihleego
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Documented
@Inherited
public @interface AutoSeq {

    /**
     * Alias for {@link AutoSeq#key}.
     */
    String value() default "";

    /**
     * Alias for {@link AutoSeq#value}.
     */
    String key() default "";

    /**
     * The name of the field where the value needs to be set.
     * For class target.
     */
    String field() default "";

}
