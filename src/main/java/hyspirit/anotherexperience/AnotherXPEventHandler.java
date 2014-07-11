package hyspirit.anotherexperience;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class AnotherXPEventHandler {
	
	/**
	 * Used to modify breaking speed of players accordingly to their experience level
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerBreakingBlock(PlayerEvent.BreakSpeed event){
		System.out.println("speed  = " + event.originalSpeed);
		System.out.println("tool needed = " + event.block.getHarvestTool(0));
		//I don't know if you can get which type of tool you're using, so I'm considering that if you have the correct tool, you are harvesting at a rate greater than 1
		if(event.block.getHarvestTool(0)=="pickaxe" && event.originalSpeed>1.0F)
			event.newSpeed+=AnotherXPPlayerStats.getPlayerStats(event.entityPlayer).getMiningLevel()*0.1;
	}
	
	/**
	 * Used to register new NBTTags for new players
	 * @param event
	 */
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event){
		//Check if the player already have the properties AND if it is a player
		if(event.entity instanceof EntityPlayer && AnotherXPPlayerStats.getPlayerStats((EntityPlayer) event.entity)==null)
			AnotherXPPlayerStats.register((EntityPlayer) event.entity);
	}
}
