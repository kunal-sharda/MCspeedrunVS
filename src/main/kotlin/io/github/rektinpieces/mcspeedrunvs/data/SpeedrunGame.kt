package io.github.rektinpieces.mcspeedrunvs.data

import io.github.rektinpieces.mcspeedrunvs.Plugin
import org.apache.commons.lang.time.StopWatch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot

enum class GameStage {
    NOT_PLAYING, QUEUING, PLAYING
}

/**
 * Manages the speedrun game & the scoreboard.
 */
class SpeedrunGame(private val plugin: Plugin) {
    private val teamsMap = mutableMapOf<Player, String>()

    // Either create the objective if it doesn't exist, or get it if it does
    private val scoreboardObjective = Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective("time")
            ?: Bukkit.getScoreboardManager()!!.mainScoreboard.registerNewObjective("time", "dummy", "Time")

    init {
        // Hide scoreboard on start, because we start out not in a game
        scoreboardObjective.displaySlot = null

        // Update the scoreboard objective in the background
        object : BukkitRunnable() {
            override fun run() {
                val elapsedSec = (stopwatch.time / 1000).toInt()
                Bukkit.getScoreboardManager()!!.mainScoreboard
                        .getObjective("time")?.getScore("Time")!!.score = elapsedSec

            }
        }.runTaskTimer(plugin, 20, 20)
    }

    var stage = GameStage.NOT_PLAYING
        private set
    private val stopwatch = StopWatch()

    fun startQueuing() {
        stage = GameStage.QUEUING
        teamsMap.clear()
    }

    fun start() {
        assert(stage == GameStage.QUEUING)
        stage = GameStage.PLAYING
        stopwatch.reset()
        stopwatch.start()
        // Show the scoreboard objective
        scoreboardObjective.displaySlot = DisplaySlot.SIDEBAR
    }

    fun end() {
        stage = GameStage.NOT_PLAYING
        scoreboardObjective.displaySlot = null
    }

    fun createTeam(from: Player, name: String) {
        teamsMap[from] = name
    }

    fun leaveTeam(player: Player) {
        teamsMap.remove(player)
    }

    fun joinTeam(player: Player, name: String) {
        teamsMap[player] = name;
    }

    fun autoAssignTeams(players: Collection<Player>) {
        // TODO
    }

    fun getTeams(): Map<Player, String> {
        return teamsMap;
    }
}