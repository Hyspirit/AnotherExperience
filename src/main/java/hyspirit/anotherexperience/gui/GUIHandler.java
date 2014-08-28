/**
 * Called when a player want to access a GUI, return the wished GUI.
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience.gui;

import hyspirit.anotherexperience.AnotherExperience;
import hyspirit.anotherexperience.network.PacketAskForUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// Got nothing for server... I guess...
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z) {
		switch(ID){
		case 0:
			AnotherExperience.network.sendToServer(new PacketAskForUpdate());
			return new GUIExperience();
		}
		return null;
	}
}
