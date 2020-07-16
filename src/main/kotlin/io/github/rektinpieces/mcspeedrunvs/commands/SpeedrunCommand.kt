package io.github.rektinpieces.mcspeedrunvs.commands

import io.github.rektinpieces.mcspeedrunvs.data.SpeedrunGame
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.IndexOutOfBoundsException

class SpeedrunCommand(val game: SpeedrunGame) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Command can only be called from a player.")
            return false
        }
        
        try {
            when (args[0]) {
                "create" -> {
                    game.startQueuing()
                }
                "start" -> {
                    game.start()
                    // TODO teleport all the players
                }
                "teams" -> {
                    if (args.size == 1) {
                        // Send teams instead
                        val teams = game.getTeams()
                        val map = mutableMapOf<String, MutableList<String>>()
                        for ( (player, teamName) in teams) {
                            // TODO this is kinda bad code
                            map.set(teamName, map.getOrDefault(teamName, mutableListOf()))
                            map.get(teamName)!!.add(player.displayName)
                        }
                        sender.sendMessage(map.toString())
                    }
                    else {
                        when (args[1]) {
                            "create" -> {
                                game.createTeam(sender, args[2])
                            }
                            "autoassign" -> {
                                game.autoAssignTeams(Bukkit.getOnlinePlayers())
                            }
                            "join" -> {
                                game.joinTeam(sender, args[2])
                            }
                        }
                    }
                }
                "end" -> {
                    game.end()
                }
                "status" -> {
                    sender.sendMessage(game.stage.toString())
                }
                else -> sender.sendMessage("Unknown command.")
            }
        } catch (e: IndexOutOfBoundsException) {
            sender.sendMessage("Invalid arguments.")
            return false
        }
        return true
    }
}