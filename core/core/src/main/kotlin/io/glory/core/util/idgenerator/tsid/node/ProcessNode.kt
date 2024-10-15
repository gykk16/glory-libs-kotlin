package io.glory.core.util.idgenerator.tsid.node

enum class ProcessNode(
    private val id: Long,
    private val description: String
) {
    PROCESS_0(0, ""),
    PROCESS_1(1, ""),
    PROCESS_2(2, ""),
    PROCESS_3(3, ""),
    PROCESS_4(4, ""),
    PROCESS_5(5, ""),
    PROCESS_6(6, ""),
    PROCESS_7(7, ""),
    PROCESS_8(8, ""),
    PROCESS_9(9, ""),
    PROCESS_10(10, ""),
    PROCESS_11(11, ""),
    PROCESS_12(12, ""),
    PROCESS_13(13, ""),
    PROCESS_14(14, ""),
    PROCESS_15(15, ""),
    PROCESS_16(16, ""),
    PROCESS_17(17, ""),
    PROCESS_18(18, ""),
    PROCESS_19(19, ""),
    PROCESS_20(20, ""),
    PROCESS_21(21, ""),
    PROCESS_22(22, ""),
    PROCESS_23(23, ""),
    PROCESS_24(24, ""),
    PROCESS_25(25, ""),
    PROCESS_26(26, ""),
    PROCESS_27(27, ""),
    PROCESS_28(28, ""),
    PROCESS_29(29, ""),
    PROCESS_30(30, ""),
    PROCESS_31(31, ""),
    INVALID(-1, "invalid");

    companion object {
        fun from(id: Long): ProcessNode {
            return entries.firstOrNull { it.id == id } ?: INVALID
        }
    }
}
