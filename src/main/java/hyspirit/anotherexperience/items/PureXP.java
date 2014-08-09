/**
 * The pureXP item is dropped by the OreExperience, and can be used by right-click to give the player some experience.
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience.items;

import hyspirit.anotherexperience.AnotherExperience;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PureXP extends Item {
	
	public PureXP(){
		super();
		setCreativeTab(AnotherExperience.creativeTab);
		setUnlocalizedName("pureXP");
		setTextureName(AnotherExperience.MODID + ":" + getUnlocalizedName().substring(5));
		setMaxStackSize(64);
	}
	
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player){
		player.addExperience(5);
		item.stackSize--;
		return item;
	}
}
