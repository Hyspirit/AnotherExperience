/**
 * Generalized class for an experience storing item, which allow to create a lot of different just with one class.
 * The amount of experience the item can store, his durability and the storing 'fee' are customizable.
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience.items;

import hyspirit.anotherexperience.AnotherExperience;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ExperienceStoringItem extends Item{
	//Unique to each item
	private int xpPerDurability;
	private int costToStore;

	/**
	 * 
	 * @param maxDurability The maximum amount of durability point
	 * @param xpPerDurabilityPoint The amount of xp this should store per durability point
	 * @param costToStore How much experience have to pay the player to give one durability point to the item
	 * @param name The unlocalized name of the item
	 */
	public ExperienceStoringItem(int maxDurability, int xpPerDurabilityPoint, int costToStore, String name){
		super();
		this.xpPerDurability=xpPerDurabilityPoint;
		this.costToStore=costToStore;
		
		setCreativeTab(AnotherExperience.creativeTab);
		setUnlocalizedName(name);
		setTextureName(AnotherExperience.MODID + ":" + name);	//Define texture
		setMaxStackSize(1);	//Max stack size...
		setMaxDamage(maxDurability);
	}

	/**
	 * When the player is right-clicking, it will give him some experience.
	 */
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player){
		//If player is sneaking, replenish the item of experience
		if(player.isSneaking()){
			if(item.getItemDamage()>0 && AnotherExperience.removeExperience(costToStore, player))
				item.setItemDamage(item.getItemDamage()-1);
				
			return item;
		}
		
		//This item should not break.
		if(item.getItemDamage()>=this.getMaxDamage()) return item;
		
		player.addExperience(xpPerDurability);
		if(item.attemptDamageItem(1, null))
			item.stackSize--;
        return item;
    }
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack item, EntityPlayer player, List list, boolean b){
		list.add("Contain " + (getMaxDamage() - item.getItemDamage())*xpPerDurability + " experience");
	}
	
	@Override
	public boolean hasEffect(ItemStack i){
		return i.getItemDamage()==0;
	}
}
