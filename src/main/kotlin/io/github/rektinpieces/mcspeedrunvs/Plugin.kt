package io.github.rektinpieces.mcspeedrunvs

import io.github.rektinpieces.mcspeedrunvs.commands.SpeedrunCommand
import io.github.rektinpieces.mcspeedrunvs.data.SpeedrunGame
import io.github.rektinpieces.mcspeedrunvs.event.CompassListener
import io.github.rektinpieces.mcspeedrunvs.event.DragonKilledListener
import org.bukkit.plugin.java.JavaPlugin

class Plugin : JavaPlugin() {
    override fun onEnable() {
        val game = SpeedrunGame(this)
        val compassListener = CompassListener(game)
        val dragonKilledListener = DragonKilledListener(game)

        server.pluginManager.registerEvents(dragonKilledListener, this)
        server.pluginManager.registerEvents(compassListener, this)

        // Schedule compass position updating task
        server.scheduler.runTaskTimer(this, compassListener::changeCompassPosition, 20, 20)

        getCommand("sr")!!.setExecutor(SpeedrunCommand(game))

        logger.info("Enabled MCSpeedrunVS")
    }
}