package tech.witkor.services.web.utilities

import java.lang.IllegalArgumentException

enum class ServerMode {
    SURVIVAL, ANARCHY, ECONOMY, LIFE_STEAL, CREATIVE, FREE_BUILD, EARTH_SMP, REAL_LIFE, HARDCORE, CAVE_BLOCK, CASH_BLOCK,
    WATER_BLOCK, ONE_BLOCK, MODS, VANILLA, RPG, PVP, SKY_GEN, BED_WARS, BOX_PVP, SKY_WARS, GUILDS, MEGA_DROP, EASY_HC, PARKOUR, MINI_GAMES;

    companion object {
        fun mapping(): Map<ServerMode, String> {
            return mapOf(
                SURVIVAL to "Survival",
                ANARCHY to "Anarchia SMP",
                ECONOMY to "Ekonomia",
                LIFE_STEAL to "Lifesteal SMP",
                CREATIVE to "Creative",
                FREE_BUILD to "FreeBuild",
                EARTH_SMP to "Earth SMP",
                REAL_LIFE to "RealLife",
                HARDCORE to "Hardcore",
                CAVE_BLOCK to "CaveBlock",
                CASH_BLOCK to "CashBlock",
                WATER_BLOCK to "WaterBlock",
                ONE_BLOCK to "OneBlock",
                MODS to "mods",
                VANILLA to "Vanilla",
                RPG to "RPG",
                PVP to "PvP",
                SKY_GEN to "SkyGen",
                BED_WARS to "BedWars",
                BOX_PVP to "BoxPvP",
                SKY_WARS to "SkyWars",
                GUILDS to "Gildie",
                MEGA_DROP to "MegaDrop",
                EASY_HC to "EasyHC",
                PARKOUR to "Parkour",
                MINI_GAMES to "MiniGames"
            )
        }

        fun get(serverMode: ServerMode): String {
            return this.mapping().get(serverMode).toString();
        }
        fun isValid(version: String): Boolean {
            return try {
                ServerMode.valueOf(version)
                true
            } catch (e: IllegalArgumentException) { false }
        }
    }
}