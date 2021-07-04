package ru.toliak.chatpostprocessor.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import ru.toliak.chatpostprocessor.ChatPostProcessor

class CommandHandler(
        private val plugin: ChatPostProcessor,
) {
    fun handle(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size == 1 && "reload".equals(args[0], true)) {
            plugin.reloadConfig();
            sender.sendMessage("ChatPostProcessor configuration has been reloaded");

            plugin.initializeDependencies();
            sender.sendMessage("ChatPostProcessor dependencies re-initialized");
            return true;
        }

        return false;
    }
}