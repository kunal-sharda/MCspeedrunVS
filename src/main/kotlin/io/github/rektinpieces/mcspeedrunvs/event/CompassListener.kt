package io.github.rektinpieces.mcspeedrunvs.event

import io.github.rektinpieces.mcspeedrunvs.data.SpeedrunGame
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

enum class CompassMode {
    ALLY_CYCLE, ENEMY_CYCLE
}

class CompassListener(private val game: SpeedrunGame) : Listener {
    private val compassModes = mutableMapOf<Player, CompassMode>()

    // Left click: cycle through enemy team's nearest player
    // Right click: cycle through own team players
    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        // Check item in hand, then do stuff
        val player = event.player
        if (player.inventory.itemInMainHand.type != Material.COMPASS) {
            return
        }
        when (event.action) {
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                compassModes[player] = CompassMode.ALLY_CYCLE
            }
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                compassModes[player] = CompassMode.ENEMY_CYCLE
            }
            Action.PHYSICAL -> {
                Bukkit.broadcastMessage("Epic, see: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/block/Action.html#PHYSICAL")
            }
            else -> {
            }
        }
    }

    fun changeCompassPosition() {
        val teams = game.getTeams()
        val players = Bukkit.getOnlinePlayers()
        // Player must be in a team
        players.filter { teams[it] != null }
                // Calculate compass position!

                .forEach { player ->
                    // Find closest target of different team
                    when (compassModes.getOrDefault(player, CompassMode.ALLY_CYCLE)) {
                        CompassMode.ALLY_CYCLE -> TODO()
                        CompassMode.ENEMY_CYCLE -> {
                            val target = players
                                    .filterNot { it == player }
                                    .filter { teams[it] != teams[player] }
                                    .minBy { player.location.distance(it.location) }
                            // Returns if no other suitable target found
                                    ?: return

                            player.compassTarget = target.location
                        }
                    }
                }
    }
}