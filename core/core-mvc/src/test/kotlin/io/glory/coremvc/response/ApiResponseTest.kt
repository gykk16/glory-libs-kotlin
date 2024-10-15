package io.glory.coremvc.response

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ApiResponseTest {

    @Test
    fun `SUCCESS ApiResponse 생성`(): Unit {
        // given
        val success = SuccessCode.SUCCESS
        val message = "SUCCESS ApiResponse 생성"

        // when
        val response = ApiResponse.success()
        val response2 = ApiResponse.success(message)

        // then
        response.body?.let {
            assertThat(it.code).isEqualTo(success)
            assertThat(it.message).isEqualTo(success.message)
            assertThat(it.data).isEqualTo(success.message)
        }
        response2.body?.let {
            assertThat(it.code).isEqualTo(success)
            assertThat(it.message).isEqualTo(success.message)
            assertThat(it.data).isEqualTo(message)
        }
    }

    @Test
    fun `SERVER_ERROR ApiResponse 생성`(): Unit {
        // given
        val serverError = ErrorCode.SERVER_ERROR
        val message = "SERVER_ERROR ApiResponse 생성"

        // when
        val response = ApiResponse.error()
        val response2 = ApiResponse.error(message)

        // then
        response.body?.let {
            assertThat(it.code).isEqualTo(serverError)
            assertThat(it.message).isEqualTo(serverError.message)
            assertThat(it.data).isEqualTo(serverError.message)
        }
        response2.body?.let {
            assertThat(it.code).isEqualTo(serverError)
            assertThat(it.message).isEqualTo(serverError.message)
            assertThat(it.data).isEqualTo(message)
        }
    }

}