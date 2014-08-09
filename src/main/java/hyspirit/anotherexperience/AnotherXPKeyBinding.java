/**
 * Set the key used by the mod.
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class AnotherXPKeyBinding {
	//Registered keys
	public static KeyBinding spendXP;
	
	public static void init(){
		spendXP = new KeyBinding("key.spendxp", Keyboard.KEY_G, "key.categories.anotherxp");
		
		ClientRegistry.registerKeyBinding(spendXP);
		FMLCommonHandler.instance().bus().register(new KeyInputHandler());
	}

}
