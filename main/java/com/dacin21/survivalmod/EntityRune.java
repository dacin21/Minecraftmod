package com.dacin21.survivalmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EntityRune extends EntityThrowable {

	public EntityRune(World par1World) {
		super(par1World);
	}

	public EntityRune(World par1World, EntityLivingBase par2EntityLivingBase) {
		super(par1World, par2EntityLivingBase);
	}

	public EntityRune(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6);
	}

	@Override
	protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
		if (par1MovingObjectPosition.entityHit != null)
        {
            par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        for (int i = 0; i < 32; ++i)
        {
            this.worldObj.spawnParticle("portal", this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
        }

        if (!this.worldObj.isRemote)
        {
            if (this.getThrower() != null && this.getThrower() instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)this.getThrower();

                if (entityplayermp.playerNetServerHandler.func_147362_b().isChannelOpen() && entityplayermp.worldObj == this.worldObj)
                {
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, this.posX, this.posY, this.posZ, 0.0F);
                    if (!MinecraftForge.EVENT_BUS.post(event))
                    { // Don't indent to lower patch size
                    if (this.getThrower().isRiding())
                    {
                        this.getThrower().mountEntity((Entity)null);
                    }

                    this.getThrower().setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
                    this.getThrower().fallDistance = 0.0F;
                    this.getThrower().attackEntityFrom(DamageSource.fall, event.attackDamage);
                    }
                }
            }

            this.setDead();
        }

	}

}
