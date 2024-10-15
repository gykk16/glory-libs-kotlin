package io.glory.coremvc.aop.transaction

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test

@SpringBootTest
class TxTest {

    @Autowired
    private lateinit var userTestService: UserTestService

    @Autowired
    private lateinit var userTestRepository: UserTestRepository

    @AfterEach
    fun tearDown() {
        userTestRepository.deleteAllInBatch()
    }

    @Test
    fun `readonly transaction`(): Unit {
        // given
        val names = listOf("a", "b", "c")

        // when
        val users = userTestService.saveReadOnly(names)

        // then
        val all = userTestService.findAll()
        assertThat(all).isEmpty()
    }

    @Test
    fun `write transaction`(): Unit {
        // given
        val names = listOf("a", "b", "c")

        // when
        val users = userTestService.save(names)

        // then
        val all = userTestService.findAll()
        assertThat(all).isNotEmpty.hasSize(names.size)
    }

}