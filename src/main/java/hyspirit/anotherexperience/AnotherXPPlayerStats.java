package hyspirit.anotherexperience;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class AnotherXPPlayerStats implements IExtendedEntityProperties{

	public static final String PROPERTIES_ID = "AnotherXPPlayerStats";
	
	private final EntityPlayer player;
		
	private int mining;
	
	public AnotherXPPlayerStats(EntityPlayer player){
		this.player=player;
		mining=0;
	}
	
	//Add these properties to a player
	public static final void register(EntityPlayer player){
		player.registerExtendedProperties(PROPERTIES_ID, new AnotherXPPlayerStats(player));
	}
	
	//Get the stats of a specific player
	public static final AnotherXPPlayerStats getPlayerStats(EntityPlayer player){
		return (AnotherXPPlayerStats) player.getExtendedProperties(PROPERTIES_ID);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound p = new NBTTagCompound();
		
		p.setInteger("mining", mining);
		
		compound.setTag(PROPERTIES_ID, p);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound p = (NBTTagCompound) compound.getTag(PROPERTIES_ID);
		
		mining = p.getInteger("mining");
		System.out.println("Stats du joueur chargées : " + mining);
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Allow me to use less functions
	 * @param stat
	 * @return the skill level, or -1 if skill does not exist
	 */
	public int getStatLevel(String stat){
		if(stat.equals("mining")) return mining;
		
		return -1;
	}
	
	public void setStatLevel(String stat, int level){
		if(level<1) return;
		
		if(stat.equals("mining")) mining = level;
	}
		
	/**
	 * Allow me to use less functions
	 * @param stat
	 */
	public void addStatLevel(String stat){
		if(stat.equals("mining")) mining++;
	}
	
	/**
	 * Try to increase the level of sent skill
	 * @param skill
	 * @return true if level successfully increased.
	 */
	public boolean upgradeSkill(String skill){
		if(!canUpgrade(skill)) return false;
		
		//Reduce the level of the player by the level of the skill +1
		player.addExperienceLevel(-getStatLevel(skill)-1);
		addStatLevel(skill);
		
		return true;
	}

	/**
	 * Is the player able to upgrade this skill level ?
	 * @param skill
	 * @return True if the skill may be upgraded with player's current xp
	 */
	public boolean canUpgrade(String skill) {
		return getStatLevel(skill)+1 <= player.experienceLevel;
	}

	public void updateClient(EntityPlayerMP player) {
		AnotherExperience.network.sendTo(new PacketUpdate("mining", this.getStatLevel("mining")), player);
	}
	
	public String toString(){
		return "{[mining, "+ mining +"]}";
	}

	/**
	 * Used to get the player's breaking speed against a block
	 * @param block
	 * @return The bonus breaking speed
	 */
	public float getBreakingSpeed(Block block) {
		if(block.getHarvestTool(0)=="pickaxe") 
			return mining*mining*0.1F;
		
		return 0;
	}

}
