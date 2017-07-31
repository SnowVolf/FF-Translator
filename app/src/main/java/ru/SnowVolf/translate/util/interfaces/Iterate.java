package ru.SnowVolf.translate.util.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by Snow Volf on 15.06.2017, 2:48
 */
@Retention(CLASS)
@Target({METHOD, PARAMETER, FIELD, ANNOTATION_TYPE, PACKAGE, LOCAL_VARIABLE})
public @interface Iterate{int value();}
