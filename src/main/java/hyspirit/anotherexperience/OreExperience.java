package hyspirit.anotherexperience;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class OreExperience extends Block{
	
	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;
	
	public OreExperience(){
		super(Material.rock);
		
		setCreativeTab(AnotherExperience.creativeTab);
		setHardness(5.0F);	//How hard is it to break ?
		setResistance(5.0F);	//How resistant is it to explosions ?
		setStepSound(soundTypeStone);//Wich sound does it play when walked on ?
		setBlockName("oreExperience");	//Set the name of the block
		setHarvestLevel("pickaxe", 1);	//Wich pickaxe level do we need to gather it ?
		setLightLevel(1F);	//Should the block emmit light
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon){
		blockIcon = icon.registerIcon(AnotherExperience.MODID+":"+getUnlocalizedName().substring(5));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_){
		// N.B: The first int count from 0 to 5, and it represent the side (used for multi-texture blocks)
		return blockIcon;
	}
	
	//Define how much experience it drop when broken by player
	public int getExpDrop(IBlockAccess world, int metadata, int fortune){
        return fortune == 0 ? 50 : fortune == 1 ? 100 : 150;
    }
	
	//Define what it drop
	public Item getItemDropped(int metadata, Random rand, int fortune){
		return AnotherXPItems.pureXp;
    }
	
	//How much does it drop ?
	public int quantityDropped(int meta, int fortune, Random rand){
        return 0 + rand.nextInt(4+fortune*2);
    }
}
