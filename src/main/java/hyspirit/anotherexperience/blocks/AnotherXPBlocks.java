/**
 * Register all the blocks of the mod, and keep a reference to them.
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;

public class AnotherXPBlocks {
	public static final Block concentratedXp = new OreExperience();
	
	public static void init(){
		//Register the blocks
		GameRegistry.registerBlock(concentratedXp, concentratedXp.getUnlocalizedName().substring(5));
	}
}
