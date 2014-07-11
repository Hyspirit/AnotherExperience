package hyspirit.anotherexperience;

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
