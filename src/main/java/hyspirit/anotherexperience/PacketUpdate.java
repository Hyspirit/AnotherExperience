package hyspirit.anotherexperience;

import net.minecraft.client.Minecraft;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdate implements IMessage {

	private String skill;
	private int level;
	
	public PacketUpdate(){}
	
	public PacketUpdate(String skill, int level){
		this.skill=skill;
		this.level=level;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		skill= ByteBufUtils.readUTF8String(buf);
		level = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, skill);
		buf.writeInt(level);
	}
	
	public String toString(){
		return "Packet[" + skill + ", " + level + "]";
	}
	
	public static class PacketHandler implements IMessageHandler<PacketUpdate, IMessage>{

		public PacketHandler(){}
		
		/**
		 * This methods is called when a packet is received
		 */
		@Override
		public IMessage onMessage(PacketUpdate message, MessageContext ctx) {
			System.out.println(message.toString() +" to " + ctx.side.toString());
			
			AnotherXPPlayerStats.getPlayerStats(Minecraft.getMinecraft().thePlayer).setStatLevel(message.skill, message.level);
			return null;	//No response
		}

	}

}
