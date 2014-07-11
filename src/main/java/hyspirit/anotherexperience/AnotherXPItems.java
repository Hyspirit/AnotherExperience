package hyspirit.anotherexperience;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class AnotherXPItems {
	public static Item keepingOrb;
	public static Item pureXp;
	
	public static void init(){
		//Create items
		keepingOrb = new KeepingOrb();
		pureXp = new PureXP();
		
		//Register items
		GameRegistry.registerItem(keepingOrb, keepingOrb.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(pureXp, pureXp.getUnlocalizedName().substring(5));
	}
}
