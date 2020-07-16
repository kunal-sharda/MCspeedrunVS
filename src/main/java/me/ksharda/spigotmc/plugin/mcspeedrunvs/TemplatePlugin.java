package me.ksharda.spigotmc.plugin.mcspeedrunvs;

import me.ksharda.spigotmc.plugin.mcspeedrunvs.commands.*;
import me.ksharda.spigotmc.plugin.mcspeedrunvs.commands.EnrichCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class TemplatePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("enrich").setExecutor(new EnrichCommand());
        getLogger().info("Added the 'enrich' command.");
    }
}