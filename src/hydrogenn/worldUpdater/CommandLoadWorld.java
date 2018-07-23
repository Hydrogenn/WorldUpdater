package hydrogenn.worldUpdater;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandLoadWorld implements CommandExecutor {
	
	private Plugin plugin;

	public CommandLoadWorld(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		World world = Bukkit.getWorld("worldupdate_old");
		
		Coordinate location = new Coordinate(Integer.parseInt(args[1]));
		final int range;
		int rangeValue;
		try {
			rangeValue = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			rangeValue = 4096;
		}
		range = rangeValue;

		int chunksToRun = 80;
		int total = (int) Math.pow(range * 2 + 1, 2);
		final AtomicInteger runningTotal = new AtomicInteger(0);
		
		sender.sendMessage(ChatColor.YELLOW + "Loading chunks...");
		
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < chunksToRun; i++) {
					loadPosition(world, location.getX(), location.getZ());
					location.keepMoving();
				}
				
				Bukkit.getLogger().info("Completed chunk " + location.getX() + ", " + location.getZ());
				Bukkit.getLogger().info(runningTotal.get() + "/" + total);
				
				runningTotal.set(runningTotal.get() + chunksToRun);
				
				if (location.greatestDistance() >= range) {
					Bukkit.getScheduler().cancelTasks(plugin);
					sender.sendMessage(ChatColor.AQUA + "Done!");
				}
				
				
				
			}
			
		}, 1L, 1L);
		
		return true;
		
	}

	private void loadPosition(World world, int x, int z) {
		world.loadChunk(x, z);
		world.unloadChunk(x, z);
	}

}
