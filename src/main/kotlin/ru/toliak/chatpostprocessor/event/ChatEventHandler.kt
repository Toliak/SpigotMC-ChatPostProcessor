package ru.toliak.chatpostprocessor.event

import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin
import ru.toliak.chatpostprocessor.config.Config

class ChatEventHandler(
        val config: Config,
        val plugin: JavaPlugin,
) {
    /**
     * Resolves all prepared tempString.
     * Works at the maximum priority
     */
    protected fun resolve(event: AsyncPlayerChatEvent) {
        val postResultData = HashMap<String, String>()

        for (entry in config.byPriority) {
            for (v in entry.value.values) {
                if (!event.message.contains(v.tempString, false)) {
                    continue
                }

                postResultData[v.key] = v.replaceBy

                event.message = event.message.replace(v.tempString, "")
                event.format = event.format.replace(v.tempString, "")
            }
        }

        postResultData["old-format"] = event.format

        val result = config.postResult.replace(KEY_EXTRACT_REGEX) {
            postResultData[it.groups[1]!!.value] ?: ""
        }
        event.format = result
    }

    fun handle(event: AsyncPlayerChatEvent, priority: EventPriority) {
        if (config.debug) {
            // TODO: write custom logger implementation :DDD
            plugin.logger.info(
                    "AsyncPlayerChatEvent handled." +
                            "\n    Priority: $priority" +
                            "\n    Format: ${event.format}" +
                            "\n    Message: ${event.message}"
            )
        }

        if (priority == EventPriority.MONITOR) {
            return resolve(event)
        }

        val list = config.byPriority[priority]!!.values
        for (v in list) {
            if (!event.format.contains(v.pattern, v.ignoreCase)) {
                continue
            }

            event.message = "${event.message} ${v.tempString}"
        }
    }

    companion object {
        val KEY_EXTRACT_REGEX = "\\{([a-zA-Z-_]+)}".toRegex()
    }
}