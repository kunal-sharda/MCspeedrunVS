package io.github.rektinpieces.mcspeedrunvs

import io.github.rektinpieces.mcspeedrunvs.commands.SpeedrunCommand
import io.github.rektinpieces.mcspeedrunvs.data.SpeedrunGame
import io.github.rektinpieces.mcspeedrunvs.event.DragonKilledListener
import org.bukkit.plugin.java.JavaPlugin

class Plugin : JavaPlugin() {
    override fun onEnable() {
        val game = SpeedrunGame(this)
        server.pluginManager.registerEvents(DragonKilledListener(game), this)
        getCommand("sr")!!.setExecutor(SpeedrunCommand(game))

        logger.info("Enabled MCSpeedrunVS")
    }
}