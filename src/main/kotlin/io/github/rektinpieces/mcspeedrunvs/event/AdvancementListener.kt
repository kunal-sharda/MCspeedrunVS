package io.github.rektinpieces.mcspeedrunvs.event

import org.bukkit.Bukkit.broadcastMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAdvancementDoneEvent



class AdvancementListener : Listener {

    @EventHandler
    fun AdvancementListener(event: PlayerAdvancementDoneEvent) {
        if (event.advancement.key.key == "end/kill_dragon") {
            broadcastMessage("The game has ended!")
        }

    }

}