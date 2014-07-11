package hyspirit.anotherexperience;

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
