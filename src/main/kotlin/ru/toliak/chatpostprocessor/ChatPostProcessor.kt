package ru.toliak.chatpostprocessor

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin
import ru.toliak.chatpostprocessor.command.CommandHandler
import ru.toliak.chatpostprocessor.config.Config
import ru.toliak.chatpostprocessor.event.ChatEventHandler

class ChatPostProcessor : JavaPlugin(), Listener {
    private var serializedConfig: Config? = null;
    private var commandHandler: CommandHandler? = null;
    private var chatEventHandler: ChatEventHandler? = null;

    fun initializeDependencies() {
        commandHandler = CommandHandler(this)
        serializedConfig = Config(config, this);
        chatEventHandler = ChatEventHandler(serializedConfig!!, this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        return commandHandler!!.handle(sender, command, label, args)
    }

    override fun onEnable() {
        saveDefaultConfig()

        initializeDependencies()

        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onChatLowest(event: AsyncPlayerChatEvent) {
        chatEventHandler!!.handle(event, EventPriority.LOWEST)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onChatLow(event: AsyncPlayerChatEvent) {
        chatEventHandler!!.handle(event, EventPriority.LOW)
    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onChatNormal(event: AsyncPlayerChatEvent) {
        chatEventHandler!!.handle(event, EventPriority.NORMAL)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onChatHigh(event: AsyncPlayerChatEvent) {
        chatEventHandler!!.handle(event, EventPriority.HIGH)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChatHighest(event: AsyncPlayerChatEvent) {
        chatEventHandler!!.handle(event, EventPriority.HIGHEST)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onChatMonitor(event: AsyncPlayerChatEvent) {
        chatEventHandler!!.handle(event, EventPriority.MONITOR)
    }
}