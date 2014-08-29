/**
 * Main class of the mod.
 * Register everything (items, blocks, recipes, gui, event handler...)
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience;

import hyspirit.anotherexperience.blocks.AnotherXPBlocks;
import hyspirit.anotherexperience.gui.GUIHandler;
import hyspirit.anotherexperience.items.AnotherXPItems;
import hyspirit.anotherexperience.network.*;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid=AnotherExperience.MODID, name="Another Experience", version=AnotherExperience.VERSION)

public class AnotherExperience {
	//Usefull variables
	public static final String MODID = "anotherxp";
	public static final String VERSION = "0.1.2";
	
	@Instance("anotherxp")
	public static AnotherExperience instance;
	
	//Network
	public static SimpleNetworkWrapper network;
	
	//Create a new creative tab
	public static CreativeTabs creativeTab = new AnotherXPCreativeTabs();
	
	//Store the player stats on death
	public static HashMap<String, AnotherXPPlayerStats> deadStats = new HashMap();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		//Load config
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		AnotherXPPlayerStats.setPassiveExperienceUsage(config.get("Passive Experience", "activated", true, "Sould the passive experience system be used ?").getBoolean());
		AnotherXPPlayerStats.setPassiveExperienceModifiers(config.get("Passive Experience", "modifiers", new int[] {50, 30, 0, 15}, "The modifiers are as X in this formula, Y is the current level of the skill of the player : X*(Y+1)*(Y+1). Defaults are 50, 30, 0, 15. Null or negative value desactivate passive experience gain for this skill.").getIntList());
		AnotherXPPlayerStats.setOldLeveling(config.getBoolean("Use old leveling system", "activated", false, "The old leveling system consume skillLevel+1 vanilla experience to upgrade a skill. The new one use some vanilla experience points to up a bit the skill."));
		AnotherXPPlayerStats.setVanillaXPConsumption(config.getInt("Vanilla experience consumed", "modifiers", 10, 1, 255, "The amount set will be consumed when the player want to transfer vanilla experience to passive experience."));
		AnotherXPPlayerStats.setVanillaXPModifier(config.getFloat("Vanilla experience multiplicator", "modifiers", 1.5f, 0.1f, 1000f, "This value will be multiplied by amount of consumed experience, then added to passive experience."));
		
		config.save();
		
		//Initialize items
		AnotherXPItems.init();
		
		//Initialize blocks
		AnotherXPBlocks.init();
		
		//Register new blocks for generation
		GameRegistry.registerWorldGenerator(new AnotherXPWorldGen(), 0);
		
		//Network part
		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		if(event.getSide()==Side.CLIENT) network.registerMessage(PacketUpdate.PacketHandler.class, PacketUpdate.class, 1, Side.CLIENT);
		
		network.registerMessage(PacketAskForUpdate.PacketHandler.class, PacketAskForUpdate.class, 2, Side.SERVER);
		network.registerMessage(PacketSkillUpgrade.PacketHandler.class, PacketSkillUpgrade.class, 0, Side.SERVER);
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		//Register recipes
		AnotherXPItems.registerRecipes();
		
		if(event.getSide().isClient())
			AnotherXPKeyBinding.init();
		
		//Register event class
		MinecraftForge.EVENT_BUS.register(new AnotherXPEventHandler());
		
		//Register gui
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event){

	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event){
		event.registerServerCommand(new CommandSetSkillLevel());
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
