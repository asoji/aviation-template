package gay.asoji.aviationtemplate

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.file.TomlFileReader
import gay.asoji.aviationtemplate.data.AviationTemplateBotConfig
import kotlinx.serialization.serializer
import java.io.File
import kotlin.system.exitProcess

object Config {
    private val configPath = System.getProperty("aviationtemplate_config", "config.toml")

    fun loadConfig(): AviationTemplateBotConfig {
        return try {
            logger.info { "Loading aviation template config..." }
            TomlFileReader.decodeFromFile(serializer(), configPath)
        } catch (e: Exception) {
            logger.error {
                "Oops, An exception happened!"
                e
            }
            exitProcess(1)
        }
    }
}