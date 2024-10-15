package io.glory.coremvc.filter

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "tsid")
data class TsidProperties(
    var workerId: Int = 1,
    var processId: Int = 1
) {
    init {
        require(workerId in 0..31) { "Worker ID out of range [0, 31]: $workerId" }
        require(processId in 0..31) { "Process ID out of range [0, 31]: $processId" }
    }
}
