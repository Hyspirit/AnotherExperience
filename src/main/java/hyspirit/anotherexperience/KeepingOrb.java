package hyspirit.anotherexperience;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class KeepingOrb extends Item {
	
	//Easier to modify, if needed
	private static final int xpPerDurability=17;
	private static final int durability=15;
	
	public KeepingOrb(){
		super();
		setCreativeTab(AnotherExperience.creativeTab);
		setUnlocalizedName("keepingOrb");
		setTextureName(AnotherExperience.MODID+":"+getUnlocalizedName().substring(5));	//Define texture
		setMaxStackSize(1);	//Max stack size...
		setMaxDamage(durability);
	}
	
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)    {
		//If player is sneaking, replenish the item of experience
		if(player.isSneaking()){
			if(item.getItemDamage()>0 && AnotherExperience.removeExperience(xpPerDurability, player)){
				item.setItemDamage(item.getItemDamage()-1);
				//Lower the player's xp
//				player.experience=0.0F;
//				player.experienceLevel=0;
//				int xp = player.experienceTotal;		//This part is replaced by my method
//				player.experienceTotal=0;
//				player.addExperience(xp-xpPerDurability);
			}
			return item;
		}
		
		//This item should not break.
		if(item.getItemDamage()>=durability) return item;
		
		player.addExperience(xpPerDurability);
		if(item.attemptDamageItem(1, null))
			item.stackSize--;
        return item;
    }
	
	//Added for convenience when registering recipe
	public static int getMaxDurability(){ return durability;}
}
