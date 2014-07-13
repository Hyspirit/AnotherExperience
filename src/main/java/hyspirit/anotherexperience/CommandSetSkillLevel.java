package hyspirit.anotherexperience;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class CommandSetSkillLevel implements ICommand {

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		return "setSkillLevel";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/setSkillLevel [Player] Skill Level";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] arguments) {
		if(!(sender instanceof EntityPlayer))return;
		if(arguments.length<2 || arguments.length>3) sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
		else if(arguments.length==2){
			if(arguments[0].equals("Tree_felling")) arguments[0]="Tree felling";
			AnotherXPPlayerStats.getPlayerStats(((EntityPlayer)sender)).setStatLevel(arguments[0], Integer.valueOf(arguments[1]));
			AnotherXPPlayerStats.getPlayerStats((EntityPlayer) sender).updateClient((EntityPlayerMP) sender);
		}
		else{
			if(arguments[1].equals("Tree_felling")) arguments[1]="Tree felling";
			EntityPlayer p = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(arguments[0]);
			if(p==null){
				sender.addChatMessage(new ChatComponentText("This player does not exist."));
				return;
			}
			AnotherXPPlayerStats.getPlayerStats(p).setStatLevel(arguments[1], Integer.valueOf(arguments[1]));
			AnotherXPPlayerStats.getPlayerStats(p).updateClient((EntityPlayerMP) p);
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(sender.getCommandSenderName());
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] var1, int var2) {
		// TODO Auto-generated method stub
		return false;
	}

}
