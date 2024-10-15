package io.glory.coremvc.registrar

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.AbstractBeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.filter.AnnotationTypeFilter

private val logger = KotlinLogging.logger {}

//@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class MvcCommonFeaturesRegistrar : ImportBeanDefinitionRegistrar {

    init {
        logger.info { "# ==> MvcCommonFeaturesRegistrar initialized" }
    }

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val getAnnotatedFeatureAttributeMap = importingClassMetadata
            .getAllAnnotationAttributes(EnableMvcCommonFeatures::class.java.name) ?: return

        val provider = ClassPathScanningCandidateComponentProvider(false)
        provider.addIncludeFilter(AnnotationTypeFilter(MvcCommonFeatureConditionalBean::class.java))

        val annotated = provider.findCandidateComponents("io.glory.coremvc")
            .mapNotNull { beanDefinition: BeanDefinition ->
                try {
                    Class.forName(beanDefinition.beanClassName)
                } catch (e: ClassNotFoundException) {
                    null
                }
            }.toSet()

        val enableAllFeatures = getAnnotatedFeatureAttributeMap["enableAll"]?.firstOrNull() as? Boolean ?: false

        val features = getAnnotatedFeatureAttributeMap["features"]
            ?.flatMap { disableFeatures -> (disableFeatures as? Array<*>)?.toList() ?: emptyList() }
            ?: emptyList()

        if (enableAllFeatures) {
            annotated.forEach { featureClass: Class<*> ->
                registerBeanDefinition(registry, featureClass)
            }
        } else {
            annotated
                .filter { clazz: Class<*> ->
                    val annotation = clazz.getAnnotation(MvcCommonFeatureConditionalBean::class.java)
                    annotation != null && features.contains(annotation.value)
                }
                .forEach { featureClass: Class<*> ->
                    registerBeanDefinition(registry, featureClass)
                }
        }
    }

    private fun registerBeanDefinition(registry: BeanDefinitionRegistry, featureClass: Class<*>) {
        val definition: BeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(featureClass)
            .setScope(BeanDefinition.SCOPE_SINGLETON)
            .setLazyInit(false)
            .setAbstract(false)
            .setDependencyCheck(AbstractBeanDefinition.DEPENDENCY_CHECK_ALL)
            .beanDefinition
        registry.registerBeanDefinition(featureClass.name, definition)
    }

}
