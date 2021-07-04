package ru.toliak.chatpostprocessor.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventPriority
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class ConfigException(pathName: String) : RuntimeException() {
    override val message: String?

    init {
        this.message = "Error parsing $pathName from config.yml"
    }
}

class Config(
        cfg: FileConfiguration,
        private val plugin: JavaPlugin
) {
    val debug: Boolean;
    val postResult: String;
    val byPriority: Map<EventPriority, ConfigMain>;

    init {
        debug = cfg.getBoolean("debug", false)

        if (!cfg.isString("post-result")) {
            throw ConfigException("post-result")
        }
        postResult = cfg.getString("post-result").toString()

        if (!cfg.isConfigurationSection("by-priority")) {
            throw ConfigException("by-priority")
        }
        byPriority = EnumMap(EventPriority::class.java)

        for (priority in PRIORITY_LIST) {
            val path = "by-priority.$priority"

            if (!cfg.isList(path)) {
                plugin.logger.info("Config key $path may be invalid or does not exist")
                byPriority[EventPriority.valueOf(priority)] = ConfigMain(listOf())
                continue
            }

            val list = cfg.getMapList(path)
            byPriority[EventPriority.valueOf(priority)] = ConfigMain(list)
        }
    }

    companion object {
        val PRIORITY_LIST = listOf(
                "LOWEST",
                "LOW",
                "NORMAL",
                "HIGH",
                "HIGHEST",
                "MONITOR",
        )
    }
}