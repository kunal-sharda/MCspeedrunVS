package io.github.rektinpieces.mcspeedrunvs.data

import com.google.common.base.Stopwatch
import io.github.rektinpieces.mcspeedrunvs.Plugin
import org.apache.commons.lang.time.StopWatch
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Score
import org.bukkit.scoreboard.ScoreboardManager

enum class GameStage {
    NOT_PLAYING, QUEUING, PLAYING
}

/**
 * Manages the speedrun game & the scoreboard.
 */
class SpeedrunGame(private val plugin: Plugin) {
    private val teamsMap = mutableMapOf<Player, String>()

    // Start the game timer
    var stage = GameStage.NOT_PLAYING
        private set
    private val stopwatch = StopWatch()

    private var task: BukkitTask? = null

    private fun createBoard() {
        // Create the Board from a new scoreboard
        for(player: Player in Bukkit.getOnlinePlayers()) {
            val board = Bukkit.getScoreboardManager()!!.newScoreboard

            // Create objective and set the Game Name, Player Team, and Time
            val obj = board.registerNewObjective("MCSp", "dummy",
                    ChatColor.translateAlternateColorCodes('&', "&a&1MCSpeedrunVS &a&1"))

            obj.displaySlot = DisplaySlot.SIDEBAR
            val score1 = obj.getScore("" + ChatColor.RED + "------------------")
            score1.score = 3
            val score2 = obj.getScore("" + ChatColor.AQUA + "Team: " + getPlayerTeam(player))
            score2.score = 2
            val score3 = obj.getScore("" + ChatColor.RED + "Speedrun Time: " + stopwatch.time)
            score3.score = 1

            // Update scoreboard
            player.scoreboard = board
        }
    }

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
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::createBoard, 0, 20)

    }

    fun end() {
        stage = GameStage.NOT_PLAYING
        for (player: Player in Bukkit.getOnlinePlayers()) {
            player.scoreboard = Bukkit.getScoreboardManager()!!.mainScoreboard
        }

    }

    fun createTeam(from: Player, name: String) {
        teamsMap[from] = name
    }

    fun leaveTeam(player: Player) {
        teamsMap.remove(player)
    }

    fun joinTeam(player: Player, name: String) {
        teamsMap[player] = name
    }

    fun autoAssignTeams(players: Collection<Player>) {
        // TODO
    }

    fun getPlayerTeam(player: Player): String {
       return getTeams().getValue(player)
    }

    fun getTeams(): Map<Player, String> {
        return teamsMap
    }
}
