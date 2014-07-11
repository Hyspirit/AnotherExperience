package hyspirit.anotherexperience;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid=AnotherExperience.MODID, name="Another Experience", version=AnotherExperience.VERSION)

public class AnotherExperience {
	//Usefull variables
	public static final String MODID = "anotherxp";
	public static final String VERSION = "0.0.1";
	
	@Instance("Another Experience")
	public static AnotherExperience instance;
	
	//Create a new creative tab
	public static CreativeTabs creativeTab = new AnotherXPCreativeTabs();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		//Initialize items
		AnotherXPItems.init();
		
		//Initialize blocks
		AnotherXPBlocks.init();
		
		//Register new blocks for generation
		GameRegistry.registerWorldGenerator(new AnotherXPWorldGen(), 0);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		AnotherXPItems.registerRecipes();
		
		//Register event class
		MinecraftForge.EVENT_BUS.register(new AnotherXPEventHandler());
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event){

	}
	
	/**
     * Remove experience from the player.
     * I copied the method EntityPlayer.addExperience and modified it.
     */
    public static boolean removeExperience(int xp, EntityPlayer player)
    {
    	//First, check if player have enough experience
        if(player.experienceTotal<xp) return false;	//Can't remove more experience than the player have
        
        player.addScore(-xp);
        
        player.experience -= (float)xp / (float)player.xpBarCap();

        for (player.experienceTotal -= xp; player.experience < 0.0F; player.experience /= (float)player.xpBarCap())
        {
            player.experience = (player.experience + 1.0F) * (float)player.xpBarCap();
            player.addExperienceLevel(-1);
        }
        return true;
    }
}
