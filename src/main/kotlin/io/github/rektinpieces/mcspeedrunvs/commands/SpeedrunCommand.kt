package io.github.rektinpieces.mcspeedrunvs.commands

import io.github.rektinpieces.mcspeedrunvs.data.GameStage
import io.github.rektinpieces.mcspeedrunvs.data.SpeedrunGame
import io.github.rektinpieces.mcspeedrunvs.srMessage
import io.github.rektinpieces.mcspeedrunvs.titleBroadcast
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot


class SpeedrunCommand(private val game: SpeedrunGame) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.srMessage("Command can only be called from a player.")
            return false
        }

        try {
            when (args[0]) {
                "create" -> {
                    if (game.stage != GameStage.NOT_PLAYING) {
                        sender.srMessage("You are currently already in a game.")
                        return false
                    }
                    game.startQueuing()
                    titleBroadcast("Started new Speedrun VS game!")
                }
                "start" -> {
                    if (game.stage != GameStage.QUEUING) {
                        sender.srMessage("You are not currently queuing for a game.")
                        return false
                    }
                    game.start()
                    titleBroadcast("Starting game!")
                    // TODO teleport all the players
                }
                "teams" -> {
                    if (game.stage != GameStage.QUEUING) {
                        sender.srMessage("You are not currently queuing for a game.")
                        return false
                    }
                    if (args.size == 1) {
                        // Send teams instead
                        val teams = game.getTeams()
                        val map = mutableMapOf<String, MutableList<String>>()
                        for ( (player, teamName) in teams) {
                            // TODO this is kinda bad code
                            map[teamName] = map.getOrDefault(teamName, mutableListOf())
                            map[teamName]!!.add(player.displayName)
                        }
                        sender.srMessage("Current teams: $map")
                    }
                    else {
                        when (args[1]) {
                            "create" -> {
                                game.createTeam(sender, args[2])
                                sender.srMessage("Created team ${args[2]} with you as the first player.")
                            }
                            "autoassign" -> {
                                game.autoAssignTeams(Bukkit.getOnlinePlayers())
                                sender.srMessage("Autoassigned teams.")
                            }
                            "join" -> {
                                game.joinTeam(sender, args[2])
                                sender.srMessage("Joined team ${args[2]}.")
                            }
                            else -> {
                                sender.srMessage("Invalid teams command.")
                            }
                        }
                    }
                }
                "end" -> {
                    if (game.stage != GameStage.PLAYING) {
                        sender.srMessage("You are not currently playing a game.")
                        return false
                    }
                    game.end()
                    titleBroadcast("The game has ended.")
                }
                "status" -> {
                    sender.srMessage("You are currently ${game.stage}")
                }
                else -> sender.srMessage("Unknown command.")
            }
        } catch (e: IndexOutOfBoundsException) {
            sender.srMessage("Invalid arguments.")
            return false
        }
        return true
    }
}