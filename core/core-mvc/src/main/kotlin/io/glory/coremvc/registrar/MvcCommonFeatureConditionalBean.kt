package io.glory.coremvc.registrar

import io.glory.coremvc.MvcCommonFeature

/**
 * @see MvcCommonFeaturesRegistrar
 * @see MvcCommonFeature
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class MvcCommonFeatureConditionalBean(
    val value: MvcCommonFeature
)
