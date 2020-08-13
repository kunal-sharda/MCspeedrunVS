package io.github.rektinpieces.mcspeedrunvs

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

private val CHAT_HEADER =
        "[" + ChatColor.RED + ChatColor.BOLD + "MCSpeedrunVS" + ChatColor.RESET + "] "

fun titleBroadcast(title: String) {
    Bukkit.getOnlinePlayers().forEach {
        it.sendTitle("MCSpeedrunVS", title, 10, 70, 20)
    }
}

fun chatBroadcast(message: String) {
    Bukkit.broadcastMessage(CHAT_HEADER + message)
}

/**
 * Extension method to send a prefixed message to a specific user
 */
fun CommandSender.srMessage(message: String) {
    this.sendMessage(CHAT_HEADER + message)
}