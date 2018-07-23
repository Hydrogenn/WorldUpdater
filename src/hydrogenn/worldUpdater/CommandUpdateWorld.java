package hydrogenn.worldUpdater;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.Structure;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CommandUpdateWorld implements CommandExecutor {

	private static final EntityType[] newEntities = {
			EntityType.DOLPHIN,
			EntityType.DROWNED,
			EntityType.TURTLE,
			EntityType.PHANTOM,
			EntityType.TROPICAL_FISH,
			EntityType.COD,
			EntityType.SALMON,
			EntityType.PUFFERFISH
	};
	
	private Plugin plugin;

	public CommandUpdateWorld(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

		World oldGeneratorWorld = Bukkit.getWorld("worldupdate_old");
		World baseWorld = Bukkit.getWorld("worldupdate_base");
		World newGeneratorWorld = Bukkit.getWorld("worldupdate_new");
		
		if (baseWorld == null || oldGeneratorWorld == null || newGeneratorWorld == null) {
			sender.sendMessage(ChatColor.DARK_RED + "Not all required worlds were found. Emergency shutdown!");
			return true;
		}
		
		Coordinate location = new Coordinate();
		final int range;
		int rangeValue;
		try {
			rangeValue = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			rangeValue = 4096;
		}
		range = rangeValue;

		int chunksToRun = 1;
		int total = (int) Math.pow(range * 2 + 1, 2);
		final AtomicInteger runningTotal = new AtomicInteger(0);
		
		sender.sendMessage(ChatColor.RED + "I hope you backed up this world, because it's about to get torn apart and put together!");
		
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < chunksToRun; i++) {
					updateAtPosition(baseWorld, oldGeneratorWorld, newGeneratorWorld, location.getX(), location.getZ());
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
	
	private void updateAtPosition(World baseWorld, World oldWorld, World newWorld, int x, int z) {
		
		if (!testChunk(baseWorld, x, z)) {
			return;
		}
		
		Chunk baseChunk = baseWorld.getChunkAt(x, z);
		Chunk oldChunk = oldWorld.getChunkAt(x, z);
		Chunk newChunk = newWorld.getChunkAt(x, z);
		
		for (int blockX = x << 4; blockX < (x << 4) + 16; blockX++) {
			for (int blockZ = z << 4; blockZ < (z << 4) + 16; blockZ++) {
				for (int blockY = 0; blockY < 256; blockY++) {

					
					if (baseChunk.getBlock(blockX, blockY, blockZ).getType().equals(oldChunk.getBlock(blockX, blockY, blockZ).getType())) {
						
						Block newGeneratedBlock = newChunk.getBlock(blockX, blockY, blockZ);
						Block oldGeneratedBlock = baseChunk.getBlock(blockX, blockY, blockZ);
						
						BlockState newBlockState = newGeneratedBlock.getState();
						Location blockStateLocation = newBlockState.getLocation();
						blockStateLocation.setWorld(baseWorld);
						BlockState oldBlockState;
						
						oldGeneratedBlock.setType(newGeneratedBlock.getType(), false);
						oldGeneratedBlock.setBlockData(newGeneratedBlock.getBlockData(), false);
						
						if (newBlockState instanceof Container) {
							
							oldBlockState = blockStateLocation.getBlock().getState();
							
							if (oldBlockState instanceof Container) {
								Bukkit.getLogger().info("Found matching chests. Updating inventory.");
								((Container) oldBlockState).getInventory().setContents(((Container) newBlockState).getInventory().getContents());
							} else {
								Bukkit.getLogger().info("A container was not found on the other end. This is an error, no changes were made.");
							}
						}
						
					}
					
					
				}
			}
		}
		
		for (Entity entity : newChunk.getEntities()) {
			if (Arrays.asList(newEntities).contains(entity.getType())) {
				Location entityLocation = entity.getLocation();
				entityLocation.setWorld(baseWorld);
				entity.teleport(entityLocation);
			}
		}
		
		Bukkit.getLogger().info("Completed chunk " + x + ", " + z);
		
	}

	public boolean testChunk(World world, int x, int z) {
		
		if (world.loadChunk(x, z, false)) {
			world.unloadChunk(x, z);
			return true;
		} else return false;
		
	}

}
