package io.glory.coremvc

import org.springframework.boot.autoconfigure.condition.ConditionOutcome
import org.springframework.boot.autoconfigure.condition.SpringBootCondition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

class MvcCommonFeatureCondition : SpringBootCondition() {

    @Suppress("UNCHECKED_CAST")
    override fun getMatchOutcome(context: ConditionContext, metadata: AnnotatedTypeMetadata): ConditionOutcome {

        val env = context.environment

        val enabledAll = env.getProperty("core-mvc.features.all", Boolean::class.java, false)
        if (enabledAll) {
            return ConditionOutcome.match()
        }

        val attributes = ConditionalOnFeature::class.qualifiedName?.let { metadata.getAnnotationAttributes(it) }
        val features = attributes?.get("features") as Array<MvcCommonFeature>

        for (feature in features) {
            val featureOn = env.getProperty("core-mvc.features.${feature.property}", Boolean::class.java, false)
            if (featureOn) {
                return ConditionOutcome.match()
            }
        }

        return ConditionOutcome.noMatch("No feature enabled")
    }

}