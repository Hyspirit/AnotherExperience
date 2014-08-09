/**
 * Add a creative tab, so I can put all the mods items/blocks to it.
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience;

import hyspirit.anotherexperience.blocks.AnotherXPBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class AnotherXPCreativeTabs extends CreativeTabs {

	public AnotherXPCreativeTabs(){
		super("AnotherXP");
	}
	
	@Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock(AnotherXPBlocks.concentratedXp);
	}

}
