package io.github.rektinpieces.mcspeedrunvs.data

import org.bukkit.entity.Player

enum class GameStage {
    NOT_PLAYING, QUEUING, PLAYING
}

class SpeedrunGame {
    private val teamsMap = mutableMapOf<Player, String>()
    var stage = GameStage.NOT_PLAYING
        private set

    fun startQueuing() {
        stage = GameStage.QUEUING
        teamsMap.clear()
    }
    
    fun start() {
        assert(stage == GameStage.QUEUING)
        stage = GameStage.PLAYING
    }
    
    fun end() {
        stage = GameStage.NOT_PLAYING
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