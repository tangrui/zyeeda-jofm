package com.zyeeda.jofm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Operation {
	String name();
	String className() default "";
	String methodName() default "";
	boolean excluded() default false;
	String nextOperation() default "";
}
