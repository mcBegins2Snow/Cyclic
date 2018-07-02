/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.block.cablewireless;

import java.io.IOException;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.data.BlockPosDim;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.gui.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.gui.EnergyBar;
import com.lothrazar.cyclicmagic.gui.FluidBar;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;

public class GuiCableWireless extends GuiBaseContainer {

  private int colLeft;
  private int colMid;
  private int colRight;

  public GuiCableWireless(InventoryPlayer inventoryPlayer, TileCableWireless te) {
    super(new ContainerCableWireless(inventoryPlayer, te), te);
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileCableWireless.Fields.REDSTONE.ordinal();
    int xCenter = this.getScreenSize().width() / 2;
    colLeft = this.guiLeft + 42;
    colMid = xCenter - 8;
    colRight = xCenter + 28;
    //    this.progressBar = new ProgressBar(this, 10, 72, TileEntityPeatGenerator.Fields.TIMER.ordinal(), TileEntityPeatGenerator.TIMER_FULL);
    this.energyBar = new EnergyBar(this);
    energyBar.setWidth(16).setY(18).setX(colRight);
    this.fluidBar = new FluidBar(this, colMid, 18);
    fluidBar.setCapacity(TileCableWireless.TANK_FULL);
  }

  @Override
  public void initGui() {
    super.initGui();
    int y = 106;
    int size = Const.SQ;
    GuiButtonTooltip btnSize = new GuiButtonTooltip(TileCableWireless.SLOT_CARD_ITEM,
        this.guiLeft + colLeft,
        this.guiTop + y, size, size, "?");
    btnSize.setTooltip("wireless.target");
    this.addButton(btnSize);
    btnSize = new GuiButtonTooltip(TileCableWireless.SLOT_CARD_FLUID,
        this.guiLeft + colMid,
        this.guiTop + y, size, size, "?");
    btnSize.setTooltip("wireless.target");
    this.addButton(btnSize);
    btnSize = new GuiButtonTooltip(TileCableWireless.SLOT_CARD_ENERGY,
        this.guiLeft + colRight,
        this.guiTop + y, size, size, "?");
    btnSize.setTooltip("wireless.target");
    this.addButton(btnSize);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == TileCableWireless.SLOT_CARD_ITEM ||
        button.id == TileCableWireless.SLOT_CARD_FLUID ||
        button.id == TileCableWireless.SLOT_CARD_ENERGY) {
      //TODO: DIMENSION 
      BlockPosDim dim = ItemLocation.getPosition(tile.getStackInSlot(button.id));
      if (dim == null) {
        UtilChat.addChatMessage(ModCyclic.proxy.getClientPlayer(), "wireless.empty");
      }
      else {
        BlockPos target = dim.toBlockPos();
        if (tile.getWorld().isAreaLoaded(target, target.up())) {
          //get target
          Block block = tile.getWorld().getBlockState(target).getBlock();
          UtilChat.addChatMessage(ModCyclic.proxy.getClientPlayer(), block.getLocalizedName());
        }
        else {
          UtilChat.addChatMessage(ModCyclic.proxy.getClientPlayer(), "wireless.unloaded");
        }
      }
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0, x, y;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    // item transfer slot 
    x = this.guiLeft + colLeft;
    y = this.guiTop + 42;
    Gui.drawModalRectWithCustomSizedTexture(
        x, y,
        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    //now draw target location card slots 
    x = this.guiLeft + colLeft;
    y = this.guiTop + 86;
    Gui.drawModalRectWithCustomSizedTexture(// this is for item transfer
        x, y,
        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    x = this.guiLeft + colMid;
    Gui.drawModalRectWithCustomSizedTexture(// this is for item transfer
        x, y,
        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    x = this.guiLeft + colRight;
    Gui.drawModalRectWithCustomSizedTexture(// this is for item transfer
        x, y,
        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    y += Const.SQ;
    fluidBar.draw(((TileCableWireless) tile).getCurrentFluidStack());
  }
}
