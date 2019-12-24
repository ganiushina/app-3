package ru.geekbrains.gb_android_libraries.di.module;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Versioned {
    /**
     * The name.
     */
    int value() default 0;
}
