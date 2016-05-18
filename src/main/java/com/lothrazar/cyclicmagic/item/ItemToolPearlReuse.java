package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolPearlReuse extends BaseTool implements IHasRecipe, IHasConfig {
	public static final String name = "ender_pearl_reuse";
	private static final int durability = 2000;
	private static final int cooldown = 10;
 
	public ItemToolPearlReuse() {
		super(durability);  
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {

		worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.entity_enderpearl_throw, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		playerIn.getCooldownTracker().setCooldown(this, cooldown);

		if (!worldIn.isRemote) {
			EntityEnderPearl entityenderpearl = new EntityEnderPearl(worldIn, playerIn);
			entityenderpearl.func_184538_a(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			worldIn.spawnEntityInWorld(entityenderpearl);
		}

		super.onUse(itemStackIn, playerIn, worldIn, hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void addRecipe() {
		 
		GameRegistry.addShapedRecipe(new ItemStack(this), "eee", "ese", "eee", 'e', new ItemStack(Items.ender_eye), 
				's', new ItemStack(Blocks.emerald_block));
	}
	
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack){
	    return true;
	}

	@Override
	public void syncConfig(Configuration config) {
	
		Property prop = config.get(Const.ConfigCategory.items, "EnderPearlReuse", true, "Reuseable ender pearl");
		prop.setRequiresMcRestart(true);

		ItemRegistry.setConfigMap(this,prop.getBoolean());
	}
}