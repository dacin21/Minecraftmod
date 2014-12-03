package com.dacin21.survivalmod.mob;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;



//Your declaration. If your mob swims, change EntityAnimal to EntityWaterMob.
public class EntityMath extends EntityMob implements IRangedAttackMob {
     private EntityLivingBase lastTarget;
      private String texture;
      private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 0.25D, 20, 60, 15.0F);
      /*												(RangedMob,MoveSpeed,field_96561_g,maxRangedAttackTime,field_96562_i)
       * rangedAttackTime = MathHelper.floor_float(f * (float)(this.maxRangedAttackTime - this.field_96561_g) + (float)this.field_96561_g);
       * f = MathHelper.sqrt_double(distanceToPlayer) / this.field_96562_i;
      */
	public EntityMath(World par1World) {
              super(par1World);
              this.texture = "/survivalmod/textures/entity/mob_math.png";
              //The below means if possible, it wont walk into water
              this.getNavigator().setAvoidsWater(true);
              //This is the hitbox size. I believe it starts in the center and grows outwards
              this.setSize(0.6F, 1.8F);
              //Pretty self-explanatory.
              this.isImmuneToFire = true;
              float var2 = 0.25F;

              //Now, we have the AI. Each number in the addTask is a priority. 0 is the highest, the largest is lowest.
              //They should be set in the order which the mob should focus, because it can only do one thing at a time. I'll explain my choice for order below.
              //There are tonnes of tasks you can add. Look in the JavaDocs or other mob classes to find some more!

              //Swimming should ALWAYS be first. Otherwise if your mob falls in water, but it's running away from you or something it'll drown.
              this.tasks.addTask(0, new EntityAISwimming(this));

              this.tasks.addTask(1, this.aiArrowAttack);

              
              //This makes the mob run away when you punch it
              //this.tasks.addTask(2, new EntityAIPanic(this, 0.38F));

              //This code is used to get the mob to follow you (like cows with wheat). Here it's set to a custom fruit
              this.tasks.addTask(3, new EntityAITempt(this, 0.3F, com.dacin21.survivalmod.survivalmod.doomBlade, false));

              //This makes the mob walk around. Without it, it'd just stand still.
              this.tasks.addTask(5, new EntityAIWander(this, var2));

              //This makes the mob watch the nearest player, within a range set by the float.
              this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));

              //Finally, this makes it look around when it's not looking at a player or wandering.
              this.tasks.addTask(7, new EntityAILookIdle(this));
              

              this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
              this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
              
      }

      //This is required. If it's false, none of the above takes effect.
	@Override
      public boolean isAIEnabled() {
              return true;
      }
     
      //Pretty obvious, set it's health!
      //public int getMaxHealth() {
      //        return (10);
      //}
     
      //The sound effect played when it's just living, like a cow mooing.
	@Override
      protected String getLivingSound() {
              return "mob.ghast.moan";
      }
     
      //The sound made when it's attacked. Often it's the same as the normal say sound, but sometimes different (such as in the ender dragon)
	@Override
      protected String getHurtSound() {
              return "mob.ghast.scream";
      }
     
      //The sound made when it actually dies.
	@Override
      protected String getDeathSound() {
              return "mob.ghast.death";
      }

      //The sound the mob plays when walking around. 
	//@Override
      protected void playStepSound(int par1, int par2, int par3, int par4) {
              this.worldObj.playSoundAtEntity(this, "mob.glog.step", 0.15F,  1.0F);
      }
     
      //A basic example of what a mob should drop on death. For more advanced examples, look at code for chicken or squid.
	@Override
      protected Item getDropItem() {
              return com.dacin21.survivalmod.survivalmod.fleshCluster;
      }
     
     
      

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase,
			float f) {
		lastTarget = par1EntityLivingBase;
        ProjectileMath entityarrow = new ProjectileMath(this.worldObj, this, par1EntityLivingBase, 1.6F, (float)(14 - this.worldObj.difficultySetting.getDifficultyId() * 4));
        entityarrow.setDamage((double)(f * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.difficultySetting.getDifficultyId() * 0.11F));
		
        this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.worldObj.spawnEntityInWorld(entityarrow);
		
	}
	
	public EntityLivingBase getTarget(){
		return this.lastTarget;
	}
}
