package com.nthbyte.dialogueexample;

import com.nthbyte.dialogue.DialogueAPI;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Example plugin. Shows you how to use Dialogue within you own plugin!
 * @author <a href="linktr.ee/c10_">Caleb Owens</a>
 * @version 1.6.0
 */
public final class DialogueExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        DialogueAPI.hook(this);
        getServer().getPluginCommand("dialogue").setExecutor(new DialogueCommand());
        getServer().getPluginManager().registerEvents(new InputListener(), this);
    }

    @Override
    public void onDisable() { }

}
