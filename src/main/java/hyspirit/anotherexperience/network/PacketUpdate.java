/**
 * The PacketUpdate is used to send to the client ALL the informations about the player.
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience.network;

import net.minecraft.client.Minecraft;
import hyspirit.anotherexperience.AnotherXPPlayerStats;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdate implements IMessage {

//	private String skill;
	private int level[] = new int[AnotherXPPlayerStats.skillName.length];
	private int passiveXP[] = new int[AnotherXPPlayerStats.skillName.length];
	
	public PacketUpdate(){}
	
	public PacketUpdate(AnotherXPPlayerStats stats){
		for(int i=0; i<level.length; i++){
			level[i] = stats.getStatLevel(AnotherXPPlayerStats.skillName[i]);
			passiveXP[i] = stats.getPassiveExperience(AnotherXPPlayerStats.skillName[i]);
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		for(int i=0; i<level.length; i++){
			level[i] = buf.readInt();
			passiveXP[i] = buf.readInt();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for(int i=0; i<level.length; i++){
			buf.writeInt(level[i]);
			buf.writeInt(passiveXP[i]);
		}
	}
	
	public String toString(){
		String s = "{";
		for(int i=0; i<level.length; i++)
			s+="[" + AnotherXPPlayerStats.skillName[i] + ", " + level[i] + "(" + passiveXP[i] + ")]";
		return s+"}";
	}
	
	public static class PacketHandler implements IMessageHandler<PacketUpdate, IMessage>{

		public PacketHandler(){}
		
		/**
		 * This methods is called when a packet is received
		 */
		@Override
		public IMessage onMessage(PacketUpdate message, MessageContext ctx) {
			//Get the stats
			AnotherXPPlayerStats stats = AnotherXPPlayerStats.getPlayerStats(Minecraft.getMinecraft().thePlayer);
			
			System.out.println("[AnotherExperience] Updating player stats from data sent by server.");
			
			//Update them
			for(int i=0; i<message.level.length; i++){
				stats.setStatLevel(AnotherXPPlayerStats.skillName[i], message.level[i]);
				stats.setPassiveExperience(AnotherXPPlayerStats.skillName[i], message.passiveXP[i]);
			}
			return null;	//No response
		}

	}

}
