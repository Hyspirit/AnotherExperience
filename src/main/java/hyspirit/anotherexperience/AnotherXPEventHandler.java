package hyspirit.anotherexperience;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
		if(!(event.entity instanceof EntityPlayer) || AnotherXPPlayerStats.getPlayerStats(event.entityPlayer)==null) return;
		
		event.newSpeed*=AnotherXPPlayerStats.getPlayerStats(event.entityPlayer).getBreakingSpeed(event.block);
//		System.out.println("Harvest level : " + event.block.getHarvestLevel(0));
//		System.out.println("Harvest tool : " + event.block.getHarvestTool(0));
//		System.out.println("Can harvest block : " + event.entityPlayer.getHeldItem().getItem().canHarvestBlock(event.block, event.entityPlayer.getItemInUse()));
//		System.out.println(net.minecraftforge.oredict.OreDictionary.getOreID("logWood"));
		net.minecraft.item.ItemStack s = new net.minecraft.item.ItemStack(event.block);
		try{
		 int []ids = net.minecraftforge.oredict.OreDictionary.getOreIDs(s);
		 for(int i=0; i<ids.length; i++)
		System.out.println(ids[i]);
		}
		catch(NullPointerException e){
			System.out.println("Nul pointer.");
		}
		
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
			AnotherXPPlayerStats.getPlayerStats((EntityPlayer) e.entity).setStatLevel("Mining", stat.getStatLevel("Mining"));
		
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
