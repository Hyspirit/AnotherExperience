package hyspirit.anotherexperience;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO Auto-generated method stub
		
	}
	
	// Getters and setters
	public int getMiningLevel(){
		return mining;
	}

}
