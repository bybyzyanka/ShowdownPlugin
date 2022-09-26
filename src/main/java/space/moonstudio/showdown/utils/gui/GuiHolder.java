package space.moonstudio.showdown.utils.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class GuiHolder implements InventoryHolder {
	
	public String title;
	public Inventory inventory;
	
	public GuiHolder(String title, int size)
	{
		this.title = title;
		inventory = Bukkit.createInventory(this, size, title);
	}
	
	@Override
	public Inventory getInventory() 
	{
		return inventory;
	}

	public abstract void onGuiClick(InventoryClickEvent event);
}
