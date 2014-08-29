/**
 * Contain all informations about the level managing GUI (define buttons, what they do...)
 * 
 * @author Hyspirit
 */
package hyspirit.anotherexperience.gui;

import java.util.Iterator;

import hyspirit.anotherexperience.AnotherExperience;
import hyspirit.anotherexperience.AnotherXPPlayerStats;
import hyspirit.anotherexperience.network.PacketSkillUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GUIExperience extends GuiScreen {
	public static final int GUI_ID = 0;
	
	
	public GUIExperience(){}
	
	@Override
	public void initGui(){
		AnotherXPPlayerStats stats = AnotherXPPlayerStats.getPlayerStats(Minecraft.getMinecraft().thePlayer);
		
		GuiButton button;
		//new GuiButton(id, x, y, width, height, "display");
		//new GuiButton(id, x, y, "display");
		//Buttons have problems to display greater than w:200 and h:20, so stay under it.
		button = new GuiButton(-1, width/2-100, height-height/8-10, 200, 20, "Back");
		buttonList.add(button);
		
		for(int i=0; i<stats.skillName.length; i++){
			if(stats.getRequiredPassiveToGainLevel(stats.skillName[i])>0) button = new GuiButton(i, width/10, 10+i*30, width/3, 20, stats.skillName[i] + " (" + stats.getStatLevel(stats.skillName[i]) + ") - (" + stats.getPassiveExperience(stats.skillName[i]) + "/" + stats.getRequiredPassiveToGainLevel(stats.skillName[i]) + ")");
			else button = new GuiButton(i, width/10, 10+i*30, width/3, 20, stats.skillName[i] + " (" + stats.getStatLevel(stats.skillName[i]) + ")");
			button.enabled=stats.canUpgrade(stats.skillName[i]);
			buttonList.add(button);
			
			if(++i>=stats.skillName.length) break;
			
			if(stats.getRequiredPassiveToGainLevel(stats.skillName[i])>0) button = new GuiButton(i, width/2+width/10, 10+(i-1)*30, width/3, 20, stats.skillName[i] + " (" + stats.getStatLevel(stats.skillName[i]) + ") - (" + stats.getPassiveExperience(stats.skillName[i]) + "/" + stats.getRequiredPassiveToGainLevel(stats.skillName[i]) + ")");
			else button = new GuiButton(i, width/10, 10+(i-1)*30, width/3, 20, stats.skillName[i] + " (" + stats.getStatLevel(stats.skillName[i]) + ")");
			button.enabled=stats.canUpgrade(stats.skillName[i]);
			buttonList.add(button);
		}
	}

	@Override
	public void drawScreen(int x, int y, float par3){
		AnotherXPPlayerStats stats = AnotherXPPlayerStats.getPlayerStats(Minecraft.getMinecraft().thePlayer);

		drawDefaultBackground();

		Iterator it = buttonList.iterator();
		it.next();	// This is the cancel button
		while(it.hasNext()){
			GuiButton b = (GuiButton)it.next();
			if(stats.getRequiredPassiveToGainLevel(stats.skillName[b.id])>0) b.displayString = stats.skillName[b.id] + " (" + stats.getStatLevel(stats.skillName[b.id]) + ") - (" + stats.getPassiveExperience(stats.skillName[b.id]) + "/" + stats.getRequiredPassiveToGainLevel(stats.skillName[b.id]) + ")";
			else b.displayString=  stats.skillName[b.id] + " (" + stats.getStatLevel(stats.skillName[b.id]) + ")";
			b.enabled=stats.canUpgrade(stats.skillName[b.id]);
		}

		//Draw all the buttons
		super.drawScreen(x, y, par3);
		
		//Draw passive experience progress
		
		for(int i=0; i<stats.skillName.length; i+=2){
			if(stats.passiveModifier[i]>0){
				this.drawRect(width/10, 30+i*30, width/10+width/3, 40+i*30, 0xFF000000);
				this.drawRect(width/10+1, 31+i*30, (int)(width/10+1+(width/3-2)*((float)stats.getPassiveExperience(stats.skillName[i])/(float)stats.getRequiredPassiveToGainLevel(stats.skillName[i]))), 39+i*30, 0xFFFF0000);
			}
			
			if(i+1>=stats.skillName.length) break;
			
			if(stats.passiveModifier[i+1]>0){
				this.drawRect(width/2+width/10, 30+i*30, width/2+width/10+width/3, 40+i*30, 0xFF000000);
				this.drawRect(width/2+width/10+1, 31+i*30, (int)(width/2+width/10+1+(width/3-2)*((float)stats.getPassiveExperience(stats.skillName[i+1])/(float)stats.getRequiredPassiveToGainLevel(stats.skillName[i+1]))), 39+i*30, 0xFFFF0000);
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}
	
	@Override
	public void actionPerformed(GuiButton button){
		AnotherXPPlayerStats stats = (AnotherXPPlayerStats) Minecraft.getMinecraft().thePlayer.getExtendedProperties(AnotherXPPlayerStats.PROPERTIES_ID);
		
		if(button.id==-1){
			Minecraft.getMinecraft().thePlayer.closeScreen();
			return;
		}
		
		stats.upgradeSkill(stats.skillName[button.id]);
		
//		if(stats.getRequiredPassiveToGainLevel(stats.skillName[button.id])>0) button.displayString = stats.skillName[button.id] + " (" + stats.getStatLevel(stats.skillName[button.id]) + ") - (" + stats.getPassiveExperience(stats.skillName[button.id]) + "/" + stats.getRequiredPassiveToGainLevel(stats.skillName[button.id]) + ")";
//		else button.displayString=  stats.skillName[button.id] + " (" + stats.getStatLevel(stats.skillName[button.id]) + ")";
		
		
		AnotherExperience.network.sendToServer(new PacketSkillUpgrade(stats.skillName[button.id], stats.getStatLevel(stats.skillName[button.id])));
	}
}
