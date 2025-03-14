package io.glory.coremvc.response

import io.glory.coremvc.response.v1.CollectionResponse
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class CollectionResponseTest {

    @Test
    fun `of list`(): Unit {
        // given
        val list = listOf(1, 2, 3)

        // when
        val response = CollectionResponse(list)

        // then
        assertThat(response.size).isEqualTo(list.size)
        assertThat(response.content).isEqualTo(list)
    }

    @Test
    fun `of set`(): Unit {
        // given
        val set = setOf(1, 2, 3)

        // when
        val response = CollectionResponse(set)

        // then
        assertThat(response.size).isEqualTo(set.size)
        assertThat(response.content).isEqualTo(set)
    }

    @Test
    fun `of map`(): Unit {
        // given
        val map = mapOf(1 to "a", 2 to "b", 3 to "c")

        // when
        val response = CollectionResponse(map)

        // then
        assertThat(response.size).isEqualTo(map.size)
        assertThat(response.content).isEqualTo(map)
    }

}