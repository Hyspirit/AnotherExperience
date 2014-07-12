package hyspirit.anotherexperience;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
		
		// TODO: Sync server and player when they upgrade a skill (both skill tags AND player XP
		//Get player stats
		AnotherXPPlayerStats stats = AnotherXPPlayerStats.getPlayerStats(event.entityPlayer);
		//Modify his speed
		if(event.block.getHarvestTool(0)=="pickaxe" && event.originalSpeed>1.0F) 
			event.newSpeed+=(float) stats.getStatLevel("mining")*5;
		System.out.println(event.newSpeed);
	}
	
	/**
	 * Used to register new NBTTags for new players
	 * @param event
	 */
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event){
		
		if(!(event.entity instanceof EntityPlayer)) return;
		
		System.out.println("Player constructed !");
		//Check if the player already have the properties AND if it is a player
		if(AnotherXPPlayerStats.getPlayerStats((EntityPlayer) event.entity)==null)
			AnotherXPPlayerStats.register((EntityPlayer) event.entity);
		
//		if(!event.entity.worldObj.isRemote)
//			AnotherXPPlayerStats.getPlayerStats((EntityPlayer) event.entity).updateClient();
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onEntityJoinWorld(EntityJoinWorldEvent e)
	{
		if (!e.world.isRemote && e.entity instanceof EntityPlayerMP)
		{
			AnotherXPPlayerStats.getPlayerStats((EntityPlayer) e.entity).updateClient((EntityPlayerMP) e.entity);
			System.out.println("UPDATED");
		}
	}
}
