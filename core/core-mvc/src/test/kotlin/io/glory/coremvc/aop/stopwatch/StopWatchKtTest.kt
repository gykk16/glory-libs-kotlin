package io.glory.coremvc.aop.stopwatch

import org.junit.jupiter.api.Test

class StopWatchKtTest {

    @Test
    fun `logs stopWatch`() = stopWatch {
        // do something
        println("do something")
    }

    @Test
    fun `logs stopWatch with title`() = stopWatch("testStopWatch") {
        // do something
        println("do something")
    }
}