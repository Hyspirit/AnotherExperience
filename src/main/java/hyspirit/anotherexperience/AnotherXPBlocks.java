package hyspirit.anotherexperience;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;

public class AnotherXPBlocks {
	public static Block concentratedXp;
	
	public static void init(){
		concentratedXp = new OreExperience();
		
		
		//Register the blocks
		GameRegistry.registerBlock(concentratedXp, concentratedXp.getUnlocalizedName().substring(5));
	}
}
