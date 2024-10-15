package io.glory.coremvc

import org.springframework.context.annotation.Conditional

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
@Conditional(MvcCommonFeatureCondition::class)
annotation class ConditionalOnFeature(
    val features: Array<MvcCommonFeature>
)
