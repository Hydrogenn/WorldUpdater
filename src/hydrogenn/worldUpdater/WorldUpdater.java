package hydrogenn.worldUpdater;

import org.bukkit.plugin.java.JavaPlugin;

import hydrogenn.worldUpdater.CommandUpdateWorld;

public class WorldUpdater extends JavaPlugin {

	@Override
    public void onEnable() {
        getCommand("go").setExecutor(new CommandUpdateWorld(this));
        getCommand("load").setExecutor(new CommandLoadWorld(this));
        getServer().getPluginManager().registerEvents(new EverythingCanceler(), this);
    }
	
}
