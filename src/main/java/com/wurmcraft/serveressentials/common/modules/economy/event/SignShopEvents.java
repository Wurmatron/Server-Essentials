package com.wurmcraft.serveressentials.common.modules.economy.event;

import static com.wurmcraft.serveressentials.common.modules.economy.utils.SignCreationHelper.createAdminSign;
import static com.wurmcraft.serveressentials.common.modules.economy.utils.SignShopHandler.handleAdminSigns;
import static com.wurmcraft.serveressentials.common.modules.economy.utils.SignShopHandler.handleSigns;

import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.NBT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SignShopEvents {

  @SubscribeEvent
  public void onRightClickEvent(PlayerInteractEvent.RightClickBlock e) {
    IBlockState state = e.getEntityPlayer().world.getBlockState(e.getPos());
    if (state
        .getBlock()
        .getUnlocalizedName()
        .equalsIgnoreCase(Blocks.STANDING_SIGN.getUnlocalizedName())) {
      TileEntitySign sign = (TileEntitySign) e.getEntityPlayer().world.getTileEntity(e.getPos());
      if (sign != null) {
        if (sign.getTileData().hasKey(NBT.SHOP_DATA)) { // Valid / Created Sign
          if (handleAdminSigns(sign, e.getEntityPlayer())
              || handleSigns(sign, e.getEntityPlayer())) {
            //           msg user about what they have purchased and for how much
          } else {
            ChatHelper.sendMessage(
                e.getEntityPlayer(),
                LanguageModule.getUserLanguage(e.getEntityPlayer()).local.ECO_SIGN_INVALID);
          }
        } else { // Possible New Sign
          if (createAdminSign(sign, state, e.getEntityPlayer(), e.getItemStack())) {
            ChatHelper.sendMessage(
                e.getEntityPlayer(),
                LanguageModule.getUserLanguage(e.getEntityPlayer()).local.ECO_SIGN_CREATED);
          }
        }
      }
    }
  }
}
