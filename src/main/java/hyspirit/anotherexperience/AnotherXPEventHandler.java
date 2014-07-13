package hyspirit.anotherexperience;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class AnotherXPEventHandler {
	
	/**
	 * Used to modify breaking speed of players accordingly to their experience level
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerBreakingBlock(PlayerEvent.BreakSpeed event){
		//Get player stats
		AnotherXPPlayerStats stats = AnotherXPPlayerStats.getPlayerStats(event.entityPlayer);
		//Modify his speed
		if(event.block.getHarvestTool(0)=="pickaxe" && event.originalSpeed>1.0F) 
			event.newSpeed+=(float) stats.getStatLevel("mining")*5;
	}
	
	/**
	 * Used to register new NBTTags for new players
	 * @param event
	 */
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event){
		
		if(!(event.entity instanceof EntityPlayer)) return;
		//Check if the player already have the properties AND if it is a player
		if(AnotherXPPlayerStats.getPlayerStats((EntityPlayer) event.entity)==null)
			AnotherXPPlayerStats.register((EntityPlayer) event.entity);
	}
	
	/**
	 * Used to send to the player his correct stats when he connect.
	 * Also retrieve his stats after he revived.
	 * @param e
	 */
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public void onEntityJoinWorld(EntityJoinWorldEvent e){
		if(!(e.entity instanceof EntityPlayerMP) || e.world.isRemote) return;
		
		AnotherXPPlayerStats stat = AnotherExperience.deadStats.remove(e.entity.getCommandSenderName());
		
		//Check if player was dead, if he was, then load properties.
		if(stat != null)
			AnotherXPPlayerStats.getPlayerStats((EntityPlayer) e.entity).setStatLevel("mining", stat.getStatLevel("mining"));
		
		//Finally, update the client
		AnotherXPPlayerStats.getPlayerStats((EntityPlayer) e.entity).updateClient((EntityPlayerMP) e.entity);
	}
	
	/**
	 * When the player die, store his stats.
	 * @param event
	 */
	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event){
		if(!(event.entity instanceof EntityPlayer)) return;
		
		AnotherExperience.deadStats.put(((EntityPlayer)event.entity).getCommandSenderName(), AnotherXPPlayerStats.getPlayerStats((EntityPlayer) event.entity));
	}
}
