package hyspirit.anotherexperience;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GUIExperience extends GuiScreen {
	public static final int GUI_ID = 0;
	
	public GUIExperience(){
		
	}
	
	public void initGui(){
		AnotherXPPlayerStats stats = AnotherXPPlayerStats.getPlayerStats(Minecraft.getMinecraft().thePlayer);
		
		GuiButton button = new GuiButton(0, 10, 10, "Mining (" + stats.getStatLevel("mining") + ")");
		button.enabled=stats.canUpgrade("mining");
		buttonList.add(button);
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
		
		switch(button.id){
		case 0:
			skill = "mining";
			break;
		
		default:
			skill = "";
			break;
		}
		stats.upgradeSkill(skill);
		button.displayString=skill + "(" + stats.getStatLevel(skill) + ")";
		
		AnotherExperience.network.sendToServer(new PacketSkillUpgrade(skill, stats.getStatLevel(skill)));
	}
}
