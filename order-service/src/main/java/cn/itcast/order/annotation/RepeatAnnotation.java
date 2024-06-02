package cn.itcast.order.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface RepeatAnnotation {
    String value() default "";

    long timeOut() default 1L;
}
