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
	
	//I want the skill names to be public, but not theirs levels, so... And I don't need the skill name in all instances ;)
	public static final String[] skillName = {"mining", "digging"};
	private int[] skillLevel = new int[skillName.length];
	
	public AnotherXPPlayerStats(EntityPlayer player){
		this.player=player;
		for(int i=0; i<skillName.length; i++)
			skillLevel[i]=0;
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
		
		for(int i=0; i<skillName.length; i++)
			p.setInteger(skillName[i], skillLevel[i]);
		
		compound.setTag(PROPERTIES_ID, p);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound p = (NBTTagCompound) compound.getTag(PROPERTIES_ID);
		
		for(int i=0; i<skillName.length; i++)
			skillLevel[i] = p.getInteger(skillName[i]);
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
		
		for(int i=0; i<skillName.length; i++)
			if(stat.equals(skillName[i]))
				return skillLevel[i];
		
		System.out.println("[AnotherExperience] A method sent an unknown skill name to getStatLevel.");
		return -1;
	}
	
	public void setStatLevel(String stat, int level){
		if(level<1) return;
		
		for(int i=0; i<skillName.length; i++)
			if(stat.equals(skillName[i])){
				skillLevel[i] = level;
				return;
			}
		System.out.println("[AnotherExperience] A method sent an unknown skill name to setStatLevel.");
	}
		
	/**
	 * Allow me to use less functions
	 * @param stat
	 */
	public void addStatLevel(String stat){
		for(int i=0; i<skillName.length; i++)
			if(stat.equals(skillName[i])){
				skillLevel[i]++;
				return;
			}
		System.out.println("[AnotherExperience] A method sent an unknown skill name to addStatLevel.");
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
		AnotherExperience.network.sendTo(new PacketUpdate(this), player);
	}
	
	public String toString(){
		String s = "{";
		for(int i=0; i<skillName.length; i++)
			s+="[" + skillName[i] + ", " + skillLevel[i] + "]";
		return s+"}";
	}

	/**
	 * Used to get the player's breaking speed against a block
	 * @param block
	 * @return The bonus breaking speed
	 */
	public float getBreakingSpeed(Block block) {
		if(block.getHarvestTool(0)=="pickaxe") 
			return skillLevel[0]*skillLevel[0]*0.1F;
		if(block.getHarvestTool(0)=="shovel")
			return skillLevel[1]*skillLevel[1]*0.05F;
		
		return 0;
	}

}
