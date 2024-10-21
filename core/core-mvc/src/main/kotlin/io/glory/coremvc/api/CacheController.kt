package io.glory.coremvc.api

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.ConditionalOnFeature
import io.glory.coremvc.MvcCommonFeature.CAFFEINE_CACHE
import io.glory.coremvc.response.v2.ApiResource
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.Serializable

private val logger = KotlinLogging.logger {}

@ConditionalOnFeature(features = [CAFFEINE_CACHE])
@RestController
@RequestMapping("/_caches")
class CacheController(
    private val cacheManager: CacheManager
) {

    init {
        logger.info { "# ==> CacheController initialized" }
    }

    @GetMapping
    fun cacheStats(): ResponseEntity<ApiResource> {
        val cacheInfoList: MutableList<CacheInfo> = cacheManager.cacheNames
            .stream()
            .peek { name: String -> logger.info { "# ==> cacheName = $name" } }
            .map { cacheName: String -> getCacheInfo(cacheName) }
            .toList()

        return ApiResource.of(cacheInfoList)
    }

    @GetMapping("/{cacheName}")
    fun cacheStat(@PathVariable cacheName: String): ResponseEntity<ApiResource> {

        require(cacheManager.cacheNames.contains(cacheName)) { "Cache not found: $cacheName" }

        val cacheInfo = getCacheInfo(cacheName)
        return ApiResource.of(cacheInfo)
    }

    @DeleteMapping
    fun evictCaches(): ResponseEntity<ApiResource> {
        cacheManager.cacheNames.forEach { cacheName ->
            logger.info { "# ==> evict cache = $cacheName" }
            val caffeineCache = cacheManager.getCache(cacheName!!) as CaffeineCache?
            val nativeCache = caffeineCache!!.nativeCache

            nativeCache.invalidateAll()
        }
        return ApiResource.success()
    }

    @DeleteMapping("/{cacheName}")
    fun evictCache(@PathVariable cacheName: String): ResponseEntity<ApiResource> {
        require(cacheManager.cacheNames.contains(cacheName)) { "Cache not found: $cacheName" }

        logger.info { "# ==> evict cache = $cacheName" }
        val caffeineCache = cacheManager.getCache(cacheName) as CaffeineCache
        val nativeCache = caffeineCache.nativeCache
        nativeCache.invalidateAll()

        return ApiResource.success()
    }

    private fun getCacheInfo(cacheName: String): CacheInfo {
        val caffeineCache = cacheManager.getCache(cacheName) as CaffeineCache
        val nativeCache = caffeineCache.nativeCache

        /* note: 중복된 key 가 있는지 확인 하기 위해 set 이 아닌 list 로 만든다 */
        val keys = nativeCache.asMap().keys.stream().map { obj: Any -> obj.toString() }.toList()

        val stats = nativeCache.stats()
        return CacheInfo(cacheName, keys.size, keys, stats.toString())
    }

    @JvmRecord
    data class CacheInfo(
        val name: String,
        val size: Int,
        val keys: List<String>,
        val stats: String
    ) : Serializable

}