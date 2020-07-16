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
                    sender.sendMessage("Started new Speedrun VS game. You are currently the only player.")
                }
                "start" -> {
                    game.start()
                    sender.sendMessage("Starting game!")
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
                        sender.sendMessage("Current teams: $map")
                    }
                    else {
                        when (args[1]) {
                            "create" -> {
                                game.createTeam(sender, args[2])
                                sender.sendMessage("Created team ${args[2]} with you as the first player.")
                            }
                            "autoassign" -> {
                                game.autoAssignTeams(Bukkit.getOnlinePlayers())
                                sender.sendMessage("Autoassigned teams.")
                            }
                            "join" -> {
                                game.joinTeam(sender, args[2])
                                sender.sendMessage("Joined team ${args[2]}.")
                            }
                            else -> {
                                sender.sendMessage("Invalid teams command.")
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