package com.dacin21.survivalmod.NEI;

import com.dacin21.survivalmod.GuiInfuser;
import com.dacin21.survivalmod.survivalmod;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEISurvivalmodConfig implements  IConfigureNEI {

	@Override
	public String getName() {
		return survivalmod.modid;
	}

	@Override
	public String getVersion() {
		return "0.0.2";
	}

	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new RecipeHandlerInfuser());
		API.registerUsageHandler(new RecipeHandlerInfuser());
		API.setGuiOffset(GuiInfuser.class, 0, 0);
	}

}
