package com.dacin21.survivalmod.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class DacinDamageSource extends DamageSource {
	private float hungerDamage=0.3F;

	public DacinDamageSource(String par1Str) {
		super(par1Str);
	}
	
	public void setHungerDamage(float par1){
		this.hungerDamage = par1;
	}
	
    public float getHungerDamage()
    {
        return this.hungerDamage;
    }
	
	
    public static DamageSource causeMathDamage(ProjectileMath par0ProjectileMath, Entity par1Entity)
    {
        return (new EntityDamageSourceIndirect("math", par0ProjectileMath, par1Entity)).setProjectile();
    }

}
