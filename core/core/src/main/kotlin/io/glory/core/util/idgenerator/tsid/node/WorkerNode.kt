package io.glory.core.util.idgenerator.tsid.node

enum class WorkerNode(
    private val id: Long,
    private val description: String
) {
    WORKER_0(0, ""),
    WORKER_1(1, ""),
    WORKER_2(2, ""),
    WORKER_3(3, ""),
    WORKER_4(4, ""),
    WORKER_5(5, ""),
    WORKER_6(6, ""),
    WORKER_7(7, ""),
    WORKER_8(8, ""),
    WORKER_9(9, ""),
    WORKER_10(10, ""),
    WORKER_11(11, ""),
    WORKER_12(12, ""),
    WORKER_13(13, ""),
    WORKER_14(14, ""),
    WORKER_15(15, ""),
    WORKER_16(16, ""),
    WORKER_17(17, ""),
    WORKER_18(18, ""),
    WORKER_19(19, ""),
    WORKER_20(20, ""),
    WORKER_21(21, ""),
    WORKER_22(22, ""),
    WORKER_23(23, ""),
    WORKER_24(24, ""),
    WORKER_25(25, ""),
    WORKER_26(26, ""),
    WORKER_27(27, ""),
    WORKER_28(28, ""),
    WORKER_29(29, ""),
    WORKER_30(30, ""),
    WORKER_31(31, ""),
    INVALID(-1, "invalid");

    companion object {
        fun from(id: Long): WorkerNode {
            return entries.firstOrNull { it.id == id } ?: INVALID
        }
    }
}
