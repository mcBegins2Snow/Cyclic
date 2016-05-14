package com.lothrazar.cyclicmagic.net;

import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpellBuildType implements IMessage, IMessageHandler<PacketSpellBuildType, IMessage> {

	public static final int ID = 17;

	public PacketSpellBuildType() {

	}

	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	@Override
	public IMessage onMessage(PacketSpellBuildType message, MessageContext ctx) {

		EntityPlayer player = ctx.getServerHandler().playerEntity;
		ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(player);

		if (wand == null) { return null; }

		ItemCyclicWand.BuildType.toggle(wand);

		return null;
	}
}