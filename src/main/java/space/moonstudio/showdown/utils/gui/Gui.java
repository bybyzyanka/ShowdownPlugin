package space.moonstudio.showdown.utils.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Gui extends GuiHolder {

	protected final TemplateGui template = TemplateGui.DEFAULT;

	public Gui(String title, int size)
	{
		super(title, size);
		inventory.setItem(template.getSlot(ButtonGui.BACK, size), ButtonGui.BACK.get());
	}
	
	public abstract boolean create(Player player);

	@Override
	public void onGuiClick(InventoryClickEvent event)
	{
		ItemStack item = event.getCurrentItem();
		if(item == null || item.getType() == Material.AIR)
			event.setCancelled(true);
	}

	public void open(Player player)
	{  
		if(!create(player))
			return;

		player.openInventory(inventory);
	} 
	
	public void update()
	{
		for(Player player : Bukkit.getOnlinePlayers()) 
		{
			if(player.getOpenInventory().getTopInventory() == null) continue;

			if(this.getClass().isInstance(player.getOpenInventory().getTopInventory().getHolder()))
				open(player);
		}
	}
}
