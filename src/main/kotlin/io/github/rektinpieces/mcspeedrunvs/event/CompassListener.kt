package io.github.rektinpieces.mcspeedrunvs.event

import io.github.rektinpieces.mcspeedrunvs.data.SpeedrunGame
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
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
    private val selectIndex = mutableMapOf<Player, Int>().withDefault { 0 }

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
                selectIndex[player] = selectIndex.getValue(player) + 1
            }
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                compassModes[player] = CompassMode.ENEMY_CYCLE
                selectIndex[player] = selectIndex.getValue(player) + 1
            }
            Action.PHYSICAL -> {
                Bukkit.broadcastMessage("Epic, see: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/block/Action.html#PHYSICAL")
            }
            else -> {
            }
        }
    }

    /**
     * To be run by a Bukkit timer every tick.
     */
    fun changeCompassPosition() {
        val teams = game.getTeams()
        val players = Bukkit.getOnlinePlayers()
        // Player must be in a team
        players.filter { teams[it] != null }
                // Calculate compass position!
                .forEach { player ->
                    // Find closest target of different team
                    val targets = when (compassModes.getOrDefault(player, CompassMode.ALLY_CYCLE)) {
                        CompassMode.ALLY_CYCLE -> players
                                .filterNot { it == player }
                                // Same team
                                .filter { teams[it] == teams[player] }
                                // Sorted by distance
                                .sortedBy { player.location.distance(it.location) }
                        CompassMode.ENEMY_CYCLE -> players
                                .filterNot { it == player }
                                .filter { teams[it] != teams[player] }
                                .sortedBy { player.location.distance(it.location) }
                    }
                    if (targets.isEmpty()) {
                        return
                    }

                    val target = targets[selectIndex.getValue(player) % targets.size]

                    // Set the compass target
                    player.compassTarget = target.location

                    if (player.inventory.itemInMainHand.type == Material.COMPASS) {
                        // Send action bar message showing the nearest player
                        // TODO show compass mode, and add colors, and make sure this disappears properly
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                TextComponent("Nearest player: ${target.displayName}"))
                    }
                }
    }
}