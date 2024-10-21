package io.glory.coremvc.cache

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalListener
import com.github.benmanes.caffeine.cache.Scheduler
import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.ConditionalOnFeature
import io.glory.coremvc.MvcCommonFeature
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private val logger = KotlinLogging.logger {}

@Configuration
@EnableCaching
@ConditionalOnFeature(features = [MvcCommonFeature.CAFFEINE_CACHE])
class CaffeineConfig(
    private val caffeineProperties: CaffeineProperties
) {

    init {
        logger.info { "# ==> CaffeineConfig initialized" }
    }

    @Bean
    fun caffeineMapConfig(): Map<String, Caffeine<Any, Any>> {

        val caffeineMap = mutableMapOf<String, Caffeine<Any, Any>>()

        caffeineProperties.cachePolicyMap.forEach { (name, cachePolicy) ->
            val maxSize = cachePolicy.maxSize
            val expireAfterAccess = cachePolicy.expireAfterAccess

            val caffeine = Caffeine.newBuilder()
                .maximumSize(maxSize.toLong())
                .expireAfterAccess(expireAfterAccess)
                .evictionListener(RemovalListener<Any, Any> { key, _, cause -> logger.debug { "# ==> Key $key was evicted ($cause)" } })
                //                .removalListener(RemovalListener<Any, Any> { key, _, cause -> logger.debug { "# ==> Key $key was removed ($cause)" } })
                .recordStats()
                .scheduler(Scheduler.systemScheduler())

            caffeineMap[name] = caffeine

            logger.info { "# ==> caffeine = $name, maxSize = $maxSize, expireAfterAccess = $expireAfterAccess" }
        }

        return caffeineMap
    }

    @Bean
    fun caffeineCacheManager(caffeineMapConfig: Map<String, Caffeine<Any, Any>>): CacheManager {

        val cacheManager = CaffeineCacheManager()
        caffeineMapConfig.forEach { (k: String, v: Caffeine<Any, Any>) ->
            cacheManager.registerCustomCache(k, v.build())
        }
        return cacheManager
    }

}