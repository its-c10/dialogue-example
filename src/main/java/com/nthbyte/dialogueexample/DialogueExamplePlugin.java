package com.nthbyte.dialogueexample;

import com.nthbyte.dialogue.DialogueAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class DialogueExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        DialogueAPI.hook(this);
        getServer().getPluginCommand("dialogue").setExecutor(new DialogueCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
