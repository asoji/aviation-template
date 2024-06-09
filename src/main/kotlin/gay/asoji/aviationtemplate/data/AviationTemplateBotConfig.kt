package gay.asoji.aviationtemplate.data

import kotlinx.serialization.Serializable

@Serializable
data class AviationTemplateBotConfig(
    val discord: DiscordConfig
) {
    @Serializable
    data class DiscordConfig(
        val botToken: String,
        val adminIDs: Set<Long>
    )
}