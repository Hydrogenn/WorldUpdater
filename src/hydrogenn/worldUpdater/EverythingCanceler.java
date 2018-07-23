package hydrogenn.worldUpdater;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EverythingCanceler implements Listener {

	@EventHandler
	public static void playerJoinEvent(PlayerJoinEvent e) {
		String[] messages = {
				ChatColor.GOLD + "Welcome, " + e.getPlayer().getDisplayName() + ".",
				ChatColor.GRAY + "If everything was set up correctly, " + ChatColor.GOLD + "worldupdate_base" + ChatColor.GRAY + " is the world you have built in,",
				ChatColor.GOLD + "worldupdate_old" + ChatColor.GRAY + " is the naturally generated world in the older version",
				ChatColor.GRAY + "And " + ChatColor.GOLD + "worldupdate_new" + ChatColor.GRAY + " is the naturally generated world in the newer version.",
				ChatColor.RED + "Make sure the old world has been fully generated using /load with the previous version's jar.",
				ChatColor.RED + "You must use a multiworld plugin for this to work.",
				ChatColor.GOLD + "When you are ready, type '/go'."
		};
		e.getPlayer().sendMessage(messages);
		e.setJoinMessage(null);
	}
	
	/*
	@EventHandler
	public static void blockBreakEvent(BlockBreakEvent e) {
		
		e.setCancelled(true);
		
		e.getPlayer().sendMessage(ChatColor.YELLOW + "Cancelled! Don't modify the world while updating.");
		
	}

	@EventHandler
	public static void blockPlaceEvent(BlockPlaceEvent e) {
		
		e.setCancelled(true);
		
		e.getPlayer().sendMessage(ChatColor.YELLOW + "Cancelled! Don't modify the world while updating.");
		
	}
	*/
	
}
