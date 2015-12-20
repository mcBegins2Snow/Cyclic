package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

/**
 * phasing out ISpell interface. now every spell must extend this base class for
 * now at least, until we make a spell book or some other system to replace this
 * linked list setup
 * 
 * @author Sam Bassett (Lothrazar)
 *
 */
public class BaseSpell implements ISpell {

	private ResourceLocation icon;
	private int ID;
	private String name;
	protected int durability;
	protected int experience;
	protected int cooldown;

	public BaseSpell(int id, String n) {
		ID = id;
		name = n;
		// default non-zero costs
		durability = 100;
		experience = 1;
		cooldown = 20;
		
		icon = new ResourceLocation(Const.MODID, "textures/spells/"+name+".png");
	}
	
	public String getName(){
		return StatCollector.translateToLocal("spell."+name+".name");
	}

	@Override
	public int getCastCooldown() {
		return cooldown;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {
		// never cast a base spell, always override this
		return false;
	}

	@Override
	public int getCostExp() {
		return experience;
	}

	@Override
	public int getCostDurability() {
		return durability;
	}

	private final static ResourceLocation header = new ResourceLocation(Const.MODID, "textures/spells/exp_cost_dummy.png");
	private final static ResourceLocation header_empty = new ResourceLocation(Const.MODID, "textures/spells/exp_cost_empty_dummy.png");

	@Override
	public void onCastFailure(World world, EntityPlayer player, BlockPos pos) {

		UtilSound.playSoundAt(player, UtilSound.fizz);
	}

	@Override
	public ResourceLocation getIconDisplayHeaderEnabled() {
		return header;
	}

	@Override
	public ResourceLocation getIconDisplayHeaderDisabled() {
		return header_empty;
	}

	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos) {
		player.swingItem();
		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT, pos);

		System.out.println("cast success, draining cost exp "+this.getCostExp());
		if (this.getCostExp() > 0) {
			UtilExperience.drainExp(player, this.getCostExp());
		}
		if (this.getCostDurability() > 0 && player.getHeldItem() != null) {
			player.getHeldItem().damageItem(this.getCostDurability(), player);
		}
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos) {
		if (player.capabilities.isCreativeMode) {
			return true;
		}

		boolean canCast = (getCostExp() <= UtilExperience.getExpTotal(player)) && (player.getHeldItem() != null) && player.getHeldItem().getItem() == ItemRegistry.master_wand && player.getHeldItem().getItemDamage() >= this.getCostDurability();

		return canCast;
	}

	@Override
	public ResourceLocation getIconDisplay() {
		return icon;
	}

	@Override
	public ISpell left() {
		int idx = SpellRegistry.spellbook.indexOf(this);// -1 for not found
		if (idx == -1) {
			return null;
		}

		if (idx == 0)
			idx = SpellRegistry.spellbook.size() - 1;
		else
			idx = idx - 1;

		return SpellRegistry.spellbook.get(idx);
	}

	@Override
	public ISpell right() {
		int idx = SpellRegistry.spellbook.indexOf(this);
		if (idx == -1) {
			return null;
		}

		if (idx == SpellRegistry.spellbook.size() - 1)
			idx = 0;
		else
			idx = idx + 1;

		return SpellRegistry.spellbook.get(idx);
	}
}
