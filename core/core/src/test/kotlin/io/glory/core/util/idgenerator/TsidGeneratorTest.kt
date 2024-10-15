package io.glory.core.util.idgenerator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.logging.Logger
import kotlin.test.Test

class TsidGeneratorTest {

    private val logger: Logger = Logger.getLogger("TraceIdGeneratorTest")
    private val idGenerator = TsidGenerator(1, 1)

    @Test
    fun parse() {
        // given
        val ids = arrayOf(
            "1215145127207641088",
            "1171993924705325056"
        )

        // when
        ids.forEach {
            val parsed = TsidGenerator.parse(it.toLong())
            val deprecated = TsidGenerator.parse(it.toLong())
            logger.info { "==> parsed = ${parsed.contentToString()} , depricated = ${deprecated.contentToString()}" }
            assertThat(parsed).isEqualTo(deprecated)
        }
    }

    @Test
    fun `generate`(): Unit {
        // given
        val deprecatedGenerator = TsidGenerator(1, 1)

        // when
        val tsid = idGenerator.generate()
        val deprecated = deprecatedGenerator.generate()
        println("tsid = $tsid , deprecated = $deprecated")

        // then
        val parsed = TsidGenerator.parse(tsid)
        val parsedDeprecated = TsidGenerator.parse(deprecated)
        println("parsed = ${parsed.contentToString()} , deprecated = ${parsedDeprecated.contentToString()}")
        assertThat(parsed[0]).isLessThanOrEqualTo(parsedDeprecated[0])
    }

    @Test
    fun `generateCheckDuplicate`(): Unit {
        // given
        val expected = 1000
        val set: MutableSet<Long> = TreeSet()

        // when
        for (i in 0 until expected) {
            val tsid = idGenerator.generate()
            set.add(tsid)
        }

        // then
        assertThat(set).hasSize(expected)
        println("set.size = ${set.size}")

        set.forEach { println(TsidGenerator.parse(it).contentToString()) }
    }

    @Disabled("heavy test")
    @DisplayName("Tsid Generator 32 thread 테스트 - 동시 요청자 수: 256, 1024, 4096, 100000, 500000 일 때")
    @ParameterizedTest
    @ValueSource(ints = [256, 256, 1024, 4096, 100000, 500000])
    fun generateHeavy(numberOfUsers: Int) {
        // given
        val threadPoolSize = 32
        val executorService = Executors.newFixedThreadPool(threadPoolSize)
        val endLatch = CountDownLatch(threadPoolSize)

        // when
        val tsids = Array(threadPoolSize) {
            LongArray(numberOfUsers)
        }

        val startTime = System.currentTimeMillis()

        for (i in 0 until threadPoolSize) {
            executorService.execute {
                try {
                    for (j in 0 until numberOfUsers) {
                        tsids[i][j] = idGenerator.generate()
                    }
                } finally {
                    endLatch.countDown()
                }
            }
        }
        endLatch.await()

        val endTime = System.currentTimeMillis()
        logger.info { "[동시 요청자 수: " + numberOfUsers + "명] " + (endTime - startTime) + "ms" }

        val set: MutableSet<Long> = HashSet()
        for (ids in tsids) {
            Arrays.stream(ids).forEach { e: Long -> set.add(e) }
        }

        // then
        executorService.shutdownNow()

        logger.info { "==> set.size() = ${set.size} " }
        assertThat(set).hasSize(numberOfUsers * threadPoolSize)
    }
}