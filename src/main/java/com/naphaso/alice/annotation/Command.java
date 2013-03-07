package com.naphaso.alice.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/5/13
 * Time: 2:00 AM
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Command {
    public String pattern() default "";
}
