package tech.witkor.services.web.utilities

enum class VersionsEnum {
    V1_7, V1_7_R2, V_1_7_R10, V1_8, V1_8_R8, V1_9, V1_10, V1_11, V1_12, V1_12_R2, V1_13, V1_14, V1_15, V1_16, V1_16_R4, V1_16_R5,
    V1_17, V1_17_R1, V1_18, V1_18_R1, V1_18_R2, V1_19, V1_19_R2, V1_19_R3, V1_20, V1_20_R1, V1_20_R2;

    companion object {
        fun mapping(): Map<VersionsEnum, String> {
            return mapOf(
                V1_7 to "1.7",
                V1_7_R2 to "1.7.2",
                V_1_7_R10 to "1.7.10",
                V1_8 to "1.8",
                V1_8_R8 to "1.8.8",
                V1_9 to "1.9",
                V1_10 to "1.10",
                V1_11 to "1.11",
                V1_12 to "1.12",
                V1_12_R2 to "1.12.2",
                V1_13 to "1.13",
                V1_14 to "1.14",
                V1_15 to "1.15",
                V1_16 to "1.16",
                V1_16_R4 to "1.16.4",
                V1_16_R5 to "1.16.5",
                V1_17 to "1.17",
                V1_17_R1 to "1.17.1",
                V1_18 to "1.18",
                V1_18_R1 to "1.18.1",
                V1_18_R2 to "1.18.2",
                V1_19 to "1.19",
                V1_19_R2 to "1.19.2",
                V1_19_R3 to "1.19.3",
                V1_20 to "1.20",
                V1_20_R1 to "1.20.1",
                V1_20_R2 to "1.20.2"
            )
        }
        fun get(versionsEnum: VersionsEnum): String {
            return this.mapping().get(versionsEnum).toString();
        }
    }
}