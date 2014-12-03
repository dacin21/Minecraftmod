package com.dacin21.survivalmod.mob;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class PotionMetaUtils {
	/**
	 * carries all the Basic Potion types <br>
	 * doesn't include duration nor strength
	 * @author daniel
	 *
	 */
	public static enum EnumPotionID
	{

		POTION_REGENERATION(8193),
		POTION_SWIFTNESS(8194),
		POTION_FIRE_RESISTANCE(8195),
		POTION_POISON(8196),
		POTION_HEALING(8197),
		POTION_NIGHT_VISION(8198),
		POTION_WEAKNESS(8200),
		POTION_STRENGHT(8201),
		POTION_SLOWNESS(8202),
		POTION_HARMING(8204),
		POTION_INVISIBILITY(8206),
		;

		private final int potionID;
	
		private EnumPotionID(int par1) {
			this.potionID = par1;
		}
	
		public int potionID()
		{
			return this.potionID;
		}
	}
	
	public static ItemStack getPotionFromNumbers(String potionName, boolean explosive, boolean extended, boolean Strenght2, int debugOffset){
		int base = EnumPotionID.valueOf(potionName).potionID();
		int metadata = base + (explosive ? 8192:0) + (extended ? 64:0) + (Strenght2 ? 32:0);
		return new ItemStack(Items.potionitem, 1, metadata);
	}
	
	

}
