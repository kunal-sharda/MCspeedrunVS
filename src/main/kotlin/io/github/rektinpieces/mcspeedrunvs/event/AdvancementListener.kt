package io.github.rektinpieces.mcspeedrunvs.event

import io.github.rektinpieces.mcspeedrunvs.data.SpeedrunGame
import org.bukkit.Bukkit
import org.bukkit.Bukkit.broadcastMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.scoreboard.DisplaySlot


class AdvancementListener(private val game: SpeedrunGame) : Listener {

    @EventHandler
    fun onEnderDragonKilled(event: PlayerAdvancementDoneEvent) {
        if (event.advancement.key.key == "end/kill_dragon") {
            val player = event.player
            val winningTeam = game.getTeams()[player];
            broadcastMessage("The game has ended!")
            broadcastMessage("The winning team is $winningTeam!")
            // Remove scoreboard
            Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective("time")!!.unregister()
            game.end()
        }

    }

}