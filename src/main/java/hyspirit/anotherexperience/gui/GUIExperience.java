/**
 * Contain all informations about the level managing GUI (define buttons, what they do...)
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience.gui;

import hyspirit.anotherexperience.AnotherExperience;
import hyspirit.anotherexperience.AnotherXPPlayerStats;
import hyspirit.anotherexperience.network.PacketSkillUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GUIExperience extends GuiScreen {
	public static final int GUI_ID = 0;
	
	public GUIExperience(){}
	
	public void initGui(){
		AnotherXPPlayerStats stats = AnotherXPPlayerStats.getPlayerStats(Minecraft.getMinecraft().thePlayer);
		
		GuiButton button;
		//new GuiButton(id, x, y, width, height, "display");
		//new GuiButton(id, x, y, "display");
		//Buttons have problems to display greater than w:200 and h:20, so stay under it.
		button = new GuiButton(-1, width/2-100, height-height/8-10, 200, 20, "Back");
		buttonList.add(button);
		
		for(int i=0; i<stats.skillName.length; i++){
			button = new GuiButton(i, width/10, 10+i*30, width/3, 20, stats.skillName[i] + " (" + stats.getStatLevel(stats.skillName[i]) + ")");
			button.enabled=stats.canUpgrade(stats.skillName[i]);
			buttonList.add(button);
			
			if(++i>=stats.skillName.length) break;
			
			button = new GuiButton(i, width/2+width/10, 10+(i-1)*30, width/3, 20, stats.skillName[i] + " (" + stats.getStatLevel(stats.skillName[i]) + ")");
			button.enabled=stats.canUpgrade(stats.skillName[i]);
			buttonList.add(button);
		}
	}
	
	public void drawScreen(int x, int y, float par3){
		drawDefaultBackground();
		
		//Draw all the buttons
		super.drawScreen(x, y, par3);
	}
	
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	public void actionPerformed(GuiButton button){
		AnotherXPPlayerStats stats = (AnotherXPPlayerStats) Minecraft.getMinecraft().thePlayer.getExtendedProperties(AnotherXPPlayerStats.PROPERTIES_ID);
		String skill;
		
		if(button.id==-1){
			Minecraft.getMinecraft().thePlayer.closeScreen();
			return;
		}
		
		skill = AnotherXPPlayerStats.skillName[button.id];
		
		stats.upgradeSkill(skill);
		button.displayString=skill + " (" + stats.getStatLevel(skill) + ")";
		
		AnotherExperience.network.sendToServer(new PacketSkillUpgrade(skill, stats.getStatLevel(skill)));
	}
}
