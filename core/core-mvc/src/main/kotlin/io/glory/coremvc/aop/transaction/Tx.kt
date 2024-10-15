package io.glory.coremvc.aop.transaction

import io.github.oshai.kotlinlogging.KotlinLogging
import io.glory.coremvc.ConditionalOnFeature
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Component
class Tx(_txAdvice: TxAdvice) {

    init {
        txAdvice = _txAdvice
        logger.info { "# ==> Tx initialized" }
    }

    companion object {
        private lateinit var txAdvice: TxAdvice

        fun <T> run(function: () -> T): T {
            return txAdvice.run(function)
        }

        fun <T> readOnly(function: () -> T): T {
            return txAdvice.readOnly(function)
        }

        fun <T> write(function: () -> T): T {
            return txAdvice.write(function)
        }
    }

    @Component
    class TxAdvice {

        @Transactional
        fun <T> run(function: () -> T): T {
            return function.invoke()
        }

        @Transactional(readOnly = true)
        fun <T> readOnly(function: () -> T): T {
            return function.invoke()
        }

        @Transactional
        fun <T> write(function: () -> T): T {
            return function.invoke()
        }
    }

}