package com.github.kripaliz.automation.step;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.kripaliz.automation.cucumber.glue.ContextLoader;

/**
 * A marker interface for StepDef classes. It takes care of the spring context
 * initialisation.
 *
 * @author kkurian
 * @deprecated not required on StepDef classes anymore. We now use
 *             {@link ContextLoader} to load the spring context.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface StepDef {

}
