package com.klk.testing.automation.pageobject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A marker interface for PageObjects. Annotate any PageObject classes with
 * this.
 * 
 * @author kkurian
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Scope("cucumber-glue")
public @interface PageObject {

}
