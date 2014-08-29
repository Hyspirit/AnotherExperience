/**
 * This class is attached to all players, and it define their stats, which change their game experience according to their level in these stats.
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience;

import hyspirit.anotherexperience.network.PacketUpdate;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class AnotherXPPlayerStats implements IExtendedEntityProperties{

	public static final String PROPERTIES_ID = "AnotherXPPlayerStats";
	
	private final EntityPlayer player;
	
	//Config
	private static boolean isPassiveExperienceActivated;
	private static boolean oldLeveling;
	private static int vanillaXPConsumption;
	private static float vanillaXPModifier;
	
	//I want the skill names to be public, but not theirs levels, so... And I don't need the skill name in all instances ;)
	public static final String[] skillName = {"Mining", "Digging", "Tree felling", "Woodcutting"};
	private int[] skillLevel = new int[skillName.length];
	private int[] passiveExperience = new int[skillName.length];
	public static int[] passiveModifier;
	
	//Called when a new player is found, and we build his properties
	public AnotherXPPlayerStats(EntityPlayer player){
		this.player=player;
		for(int i=0; i<skillName.length; i++){
			skillLevel[i]=0;
			passiveExperience[i]=0;
		}
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
		
		for(int i=0; i<skillName.length; i++){
			p.setInteger(skillName[i], skillLevel[i]);
			p.setInteger("passive"+skillName[i], passiveExperience[i]);
		}
		
		compound.setTag(PROPERTIES_ID, p);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound p = (NBTTagCompound) compound.getTag(PROPERTIES_ID);
		
		for(int i=0; i<skillName.length; i++){
			skillLevel[i] = p.getInteger(skillName[i]);
			passiveExperience[i] = p.getInteger("passive"+skillName[i]);
		}
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO Auto-generated method stub
	}
	
	//Setter of the passive experience modifier
	public static void setPassiveExperienceModifiers(int[] m){
		if(m.length!=skillName.length){
			System.out.println("[Another Experience] Wrong numbers of modifiers used for the passive experience modifiers. Using defaults parameters.");
			passiveModifier = new int[] {50, 30, 0, 15};
		}
		else passiveModifier=m;
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
		
		System.out.println("[AnotherExperience] A method sent an unknown skill name to getStatLevel : <" + stat + ">");
		return -1;
	}
	
	public void setStatLevel(String stat, int level){
		if(level<0) level = 0;
		
		for(int i=0; i<skillName.length; i++)
			if(stat.equals(skillName[i])){
				skillLevel[i] = level;
				return;
			}
		System.out.println("[AnotherExperience] A method sent an unknown skill name to setStatLevel : <" + stat + ">");
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
		System.out.println("[AnotherExperience] A method sent an unknown skill name to addStatLevel : <" + stat + ">");
	}
	
	/**
	 * Try to increase the level of sent skill
	 * @param skill
	 * @return true if level successfully increased.
	 */
	public boolean upgradeSkill(String skill){
		if(!canUpgrade(skill)) return false;
		
		if(oldLeveling){
			//Reduce the level of the player by the level of the skill +1
			player.addExperienceLevel(-getStatLevel(skill)-1);
			addStatLevel(skill);
		}
		else{
			AnotherExperience.removeExperience(vanillaXPConsumption, player);
			addPassiveExperience(skill, (int) (vanillaXPConsumption*vanillaXPModifier));
		}
		return true;
	}

	/**
	 * Is the player able to upgrade this skill level ?
	 * @param skill
	 * @return True if the skill may be upgraded with player's current xp
	 */
	public boolean canUpgrade(String skill) {
		return oldLeveling ? getStatLevel(skill) < player.experienceLevel : player.experienceTotal>=vanillaXPConsumption;
	}

	
	// - - - - - Passive experience related methods - - - - - 
	/**
	 * Add passive experience to the skill
	 * @param skill The skill you want to add passive experience
	 * @param amount The amount of experience to add
	 */
	public void addPassiveExperience(String skill, int amount){
		for(int i=0; i<skillName.length; i++)
			if(skill.equals(skillName[i])){
				if(passiveModifier[i]<=0) return;
				
				for(int j=0; j<amount; j++){
					passiveExperience[i]++;
					if(passiveExperience[i]>=passiveModifier[i]*(skillLevel[i]+1)*(skillLevel[i]+1)){
						addStatLevel(skillName[i]);
						passiveExperience[i]=0;
						updateClient((EntityPlayerMP) player);
					}
				}
				break;
			}
	}
	
	public int getRequiredPassiveToGainLevel(String stat){
		for(int i=0; i<skillName.length; i++)
			if(stat.equals(skillName[i]))
				return passiveModifier[i]*(skillLevel[i]+1)*(skillLevel[i]+1);
		
		System.out.println("[AnotherExperience] A method sent an unknown skill name to getRequiredPassiveToGainLevel.");
		return -1;
	}
	
	/**
	 * Allow me to use less functions
	 * @param stat
	 * @return the skill level, or -1 if skill does not exist
	 */
	public int getPassiveExperience(String stat){
		
		for(int i=0; i<skillName.length; i++)
			if(stat.equals(skillName[i]))
				return passiveExperience[i];
		
		System.out.println("[AnotherExperience] A method sent an unknown skill name to getStatLevel.");
		return -1;
	}
	
	public void setPassiveExperience(String stat, int level){
		if(level<0) level = 0;
		
		for(int i=0; i<skillName.length; i++)
			if(stat.equals(skillName[i])){
				passiveExperience[i] = level;
				return;
			}
		System.out.println("[AnotherExperience] A method sent an unknown skill name to setPassiveExperience.");
	}
	
	//Configuration for passive experience
	public static void setPassiveExperienceUsage(boolean b){ isPassiveExperienceActivated=b;}
	public static boolean isPassiveExperienceUsed(){return isPassiveExperienceActivated;}
	
	// - - - - - End of Passive experience related methods - - - - -
	
	//Configuration for oldLeveling
	public static void setOldLeveling(boolean b){oldLeveling=b;}
	public static boolean isOldLeveling(){return oldLeveling;}
	
	//Configuration for vanilla experience into passive experience
	public static void setVanillaXPConsumption(int value){
		if(value > 0) vanillaXPConsumption = value;
		else{
			System.out.println("[AnotherExperience] vanillaXPConsumption value is negative or null. Using default parameter : 10.");
			vanillaXPConsumption = 10;
		}
	}
	public static int getVanillaXPConsumption(){return vanillaXPConsumption;}
	public static void setVanillaXPModifier(float value){
		if(value > 0f) vanillaXPModifier = value;
		else{
			System.out.println("[AnotherExperience] vanillaXPModifier value is negative or null. Using default parameter : 1.5.");
			vanillaXPModifier = 1.5f;
		}
	}
	public static float getVanillaXPModifier(){return vanillaXPModifier;}
	
	/**
	 * Update the client with his correct stats.
	 * @param player The player to update
	 */
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
	 * Returned value will be multiplicated by the original breakspeed (So other mods can affect it too)
	 * @param block
	 * @return The bonus breaking speed
	 */
	public float getBreakingSpeed(Block block) {
		if(block.getHarvestTool(0)=="pickaxe") 
			return 0.8f+skillLevel[0]*0.015f;	//Mining
		if(block.getHarvestTool(0)=="shovel")
			return 0.5f+skillLevel[1]*0.023f;	//Digging
		if(block.getHarvestTool(0)=="axe")
			return 0.7f+skillLevel[3]*0.03f;	//Woodcutting
		
		return 1f;
	}

}
