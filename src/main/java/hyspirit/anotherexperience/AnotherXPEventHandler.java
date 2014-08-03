package hyspirit.anotherexperience;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;

public class AnotherXPEventHandler {
	
	private static int harvestStackCounter=0;
	private static int playerTreeFellingLevel;
	
	/**
	 * Used to modify breaking speed of players accordingly to their experience level
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerBreakingBlock(PlayerEvent.BreakSpeed event){
		if(!(event.entity instanceof EntityPlayer) || AnotherXPPlayerStats.getPlayerStats(event.entityPlayer)==null) return;
		
		event.newSpeed*=AnotherXPPlayerStats.getPlayerStats(event.entityPlayer).getBreakingSpeed(event.block);
	}
	
	/**
	 * Called when a block is about to drop it's loots
	 * Remember : calling harvestBlock call this again
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerHarvestingBlock(BlockEvent.HarvestDropsEvent event){
		boolean isInArray=false;
		
		if(event.harvester==null) return;	//Blocks can drop their items without needing a player to break them
		
		int []ids = OreDictionary.getOreIDs(new ItemStack(event.block));	//Get the ids of harvested block
		if(ids.length==0) return;
		// Check if it is wood (id=0)
		for(int i=0; i<ids.length; i++)
			if(ids[i]==0) isInArray=true;
		if(isInArray==false) return;
		
		event.world.setBlockToAir(event.x, event.y, event.z);	// Remove the block from the world

		if(harvestStackCounter<=0)
			playerTreeFellingLevel=AnotherXPPlayerStats.getPlayerStats(event.harvester).getStatLevel(AnotherXPPlayerStats.skillName[2]);
		// TODO Make the range depend on the player's skill
		//Now try to harvest near blocks
		harvestStackCounter++;
		harvestNearWoodLog(event.world, event.harvester, event.x+1, event.y, event.z);
		harvestNearWoodLog(event.world, event.harvester, event.x-1, event.y, event.z);
		harvestNearWoodLog(event.world, event.harvester, event.x, event.y+1, event.z);
		harvestNearWoodLog(event.world, event.harvester, event.x, event.y-1, event.z);
		harvestNearWoodLog(event.world, event.harvester, event.x, event.y, event.z+1);
		harvestNearWoodLog(event.world, event.harvester, event.x, event.y, event.z-1);
		harvestStackCounter--;
	}
	
	/**
	 * Check if the target block is a wood log AND harvest it 
	 * @param world The world the block is in
	 * @param harvester The player which is trying to harvest
	 * @param x The x coordinate of the block
	 * @param y The y coordinate of the block
	 * @param z The z coordinate of the block
	 */
	private void harvestNearWoodLog(World world, EntityPlayer harvester, int x, int y, int z){
		if(playerTreeFellingLevel<=0) return;
		
		boolean isInArray=false;	// Used to check if it is wood
		
		Block b = world.getBlock(x, y, z);	//Get the next block to destroy
		int[] ids = OreDictionary.getOreIDs(new ItemStack(b));	// Get the ID of the block
		if(ids.length==0) return;	//If the block has no id, it can't be wood (or it is incorectly registerd in the OreDict)
		
		// Check if it is wood (id=0)
		for(int i=0; i<ids.length; i++)
			if(ids[i]==0) isInArray=true;
		if(isInArray==false) return;
		
		playerTreeFellingLevel--;
		b.harvestBlock(world, harvester, x, y, z, b.getDamageValue(world, x, y, z));	//Only drop the item, doesn't destroy the block? ALSO call THIS method again
		if(harvester.getCurrentEquippedItem()!=null) harvester.getCurrentEquippedItem().attemptDamageItem(1, world.rand);	// Damage the use item ;)
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
