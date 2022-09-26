package space.moonstudio.showdown.utils.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class GuiListener implements Listener {
	
	@EventHandler
	public void onGuiClick(InventoryClickEvent event) 
	{ 
		if(event.getInventory().getHolder() instanceof GuiHolder)
		{
			GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
			holder.onGuiClick(event); 
		}
	}
	
	@EventHandler
	public void onGuiDrag(InventoryDragEvent event) 
	{
		if(event.getInventory().getHolder() instanceof GuiHolder)
			event.setCancelled(true);
	}
}
