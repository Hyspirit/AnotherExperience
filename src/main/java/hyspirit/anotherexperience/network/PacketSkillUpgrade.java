/**
 * The PacketSkillUpgrade is used to notify the server when a player is trying to upgrade one of his skills.
 * Only one information is sent to the server, which mean this packet should be lighter compared to PacketUpdate.
 * TODO : change the String to an int, will make it even lighter.
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience.network;

import hyspirit.anotherexperience.AnotherXPPlayerStats;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSkillUpgrade implements IMessage {
	
	private String skill;
	
	//Needed by FML
	public PacketSkillUpgrade(){}
	
	public PacketSkillUpgrade(String skill, int level){
		this.skill=skill;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		skill= ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, skill);
	}
	
	public String toString(){
		return "Packet[" + skill + "]";
	}
	
	public static class PacketHandler implements IMessageHandler<PacketSkillUpgrade, IMessage>{

		public PacketHandler(){}
		
		/**
		 * This methods is called when a packet is received
		 */
		@Override
		public IMessage onMessage(PacketSkillUpgrade message, MessageContext ctx) {
			AnotherXPPlayerStats playerStats = AnotherXPPlayerStats.getPlayerStats(ctx.getServerHandler().playerEntity);
			playerStats.upgradeSkill(message.skill);
			return null;	//No response
		}

	}

}
