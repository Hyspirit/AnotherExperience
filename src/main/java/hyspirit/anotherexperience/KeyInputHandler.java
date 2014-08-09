/**
 * Each time the client press a key, check if it is the one used to open a gui, if so, open the gui !
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience;

import hyspirit.anotherexperience.gui.GUIExperience;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class KeyInputHandler {
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event){
		if(FMLClientHandler.instance().getClient().currentScreen != null) return;	// If player is in a gui, don't consider the key input.
		if(AnotherXPKeyBinding.spendXP.isPressed()){
			EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;;
			player.openGui(AnotherExperience.instance, GUIExperience.GUI_ID, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
		}
	}
}
