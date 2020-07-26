package io.github.rektinpieces.mcspeedrunvs

import io.github.rektinpieces.mcspeedrunvs.commands.SpeedrunCommand
import io.github.rektinpieces.mcspeedrunvs.data.SpeedrunGame
import io.github.rektinpieces.mcspeedrunvs.event.AdvancementListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class Plugin : JavaPlugin() {
    override fun onEnable() {
        val game = SpeedrunGame(this)
        server.pluginManager.registerEvents(AdvancementListener(game), this)
        getCommand("sr")!!.setExecutor(SpeedrunCommand(game))

        logger.info("Enabled MCSpeedrunVS")
    }
}