package io.glory.coremvc.registrar

import io.glory.coremvc.MvcCommonFeature

/**
 * @see MvcCommonFeaturesRegistrar
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
annotation class EnableMvcCommonFeatures(
    val features: Array<MvcCommonFeature> = [],
    val enableAll: Boolean = false
)
