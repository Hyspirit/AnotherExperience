/**
 * The PacketAskForUpdate is used by the client to ask for it's own informations.
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience.network;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import hyspirit.anotherexperience.AnotherXPPlayerStats;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketAskForUpdate implements IMessage {
	
	public PacketAskForUpdate(){}
	
	@Override
	public void fromBytes(ByteBuf buf){}

	@Override
	public void toBytes(ByteBuf buf){}
	
	public static class PacketHandler implements IMessageHandler<PacketAskForUpdate, IMessage>{

		public PacketHandler(){}
		
		/**
		 * This methods is called when a packet is received
		 */
		@Override
		public IMessage onMessage(PacketAskForUpdate message, MessageContext ctx) {
			//Get the stats
			System.out.println("[AnotherExperience] Sending player stats to " + ctx.getServerHandler().playerEntity.getGameProfile().getName() + ".");
			return new PacketUpdate(AnotherXPPlayerStats.getPlayerStats(ctx.getServerHandler().playerEntity));
		}
	}
}