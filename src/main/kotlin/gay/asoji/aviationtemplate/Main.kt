package gay.asoji.aviationtemplate

import dev.minn.jda.ktx.events.listener
import dev.minn.jda.ktx.jdabuilder.default
import dev.minn.jda.ktx.jdabuilder.intents
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.events.session.ShutdownEvent
import net.dv8tion.jda.api.requests.GatewayIntent
import xyz.artrinix.aviation.Aviation
import xyz.artrinix.aviation.AviationBuilder
import xyz.artrinix.aviation.events.AviationExceptionEvent
import xyz.artrinix.aviation.events.CommandFailedEvent
import xyz.artrinix.aviation.internal.utils.on
import xyz.artrinix.aviation.ratelimit.DefaultRateLimitStrategy

val logger = KotlinLogging.logger {  }

object AviationTemplate {
    val config = Config.loadConfig()
    lateinit var jda: JDA
    lateinit var aviation: Aviation

    @JvmStatic
    suspend fun main(args: Array<String>): Unit = runBlocking {
        logger.info { "Starting Aviation Template" }

        jda = default(config.discord.botToken, enableCoroutines = true) {
            intents += listOf(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
        }

        aviation = AviationBuilder()
            .apply {
                ratelimitProvider = DefaultRateLimitStrategy()
                doTyping = true
                developers.addAll(config.discord.adminIDs.toTypedArray())
                registerDefaultParsers()
            }
            .build()
            .apply {
                slashCommands.register("gay.asoji.aviationtemplate.commands")
            }

        listenAviationEvents()

        jda.addEventListener(aviation)

        jda.listener<ReadyEvent> {
            aviation.syncCommands(jda)
            logger.info { "Aviation Template started!" }
        }

        jda.listener<MessageReceivedEvent> { event ->
            if (event.author.isBot || !event.isFromGuild) return@listener
        }

        jda.listener<ShutdownEvent> {
            logger.info { "Shutting down Aviation Template..." }
        }
    }

    private fun listenAviationEvents() {
        aviation.on<AviationExceptionEvent> {
            logger.error { "Oopsies. Aviation threw an error: ${this.error}" }
        }

        aviation.on<CommandFailedEvent> {
            logger.error { "[Command Execution] A command has failed: ${this.error}" }
        }
    }
}
