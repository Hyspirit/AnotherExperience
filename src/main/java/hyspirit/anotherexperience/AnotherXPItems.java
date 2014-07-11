package hyspirit.anotherexperience;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AnotherXPItems {
	public static Item keepingOrb;
	public static Item pureXp;
	
	/**
	 * Create the items of the mod and register them to the game
	 */
	public static void init(){
		//Create items
		keepingOrb = new ExperienceStoringItem(15, 17, 20, "keepingOrb");
		pureXp = new PureXP();
		
		//Register items
		GameRegistry.registerItem(keepingOrb, keepingOrb.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(pureXp, pureXp.getUnlocalizedName().substring(5));
	}
	
	/**
	 * Create all the recipes of the mod
	 */
	public static void registerRecipes(){
		ItemStack powder = new ItemStack(AnotherXPItems.pureXp);
		GameRegistry.addShapedRecipe(new ItemStack(keepingOrb, 1, keepingOrb.getMaxDamage()),"xxx","xyx","xxx", 'x', powder, 'y', new ItemStack(Items.glowstone_dust));
	}
}
