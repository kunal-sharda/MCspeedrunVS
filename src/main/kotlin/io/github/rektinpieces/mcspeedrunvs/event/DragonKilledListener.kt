package io.github.rektinpieces.mcspeedrunvs.event

import io.github.rektinpieces.mcspeedrunvs.chatBroadcast
import io.github.rektinpieces.mcspeedrunvs.data.SpeedrunGame
import org.bukkit.Bukkit
import org.bukkit.entity.EnderDragon
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent


class DragonKilledListener(private val game: SpeedrunGame) : Listener {
    @EventHandler
    fun onEnderDragonDeath(e: EntityDeathEvent) {
        if (e.entity is EnderDragon) {
            val player = e.entity.killer!!
            val winningTeam = game.getTeams()[player];
            chatBroadcast("${player.displayName} has ended the game!")
            chatBroadcast("The winning team is $winningTeam!")
            // Remove scoreboard
            Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective("time")!!.unregister()
            game.end()
        }
    }

}