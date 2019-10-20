package com.wurmcraft.serveressentials.common.modules.economy.event;

import static com.wurmcraft.serveressentials.common.modules.economy.utils.SignCreationHelper.createAdminSign;
import static com.wurmcraft.serveressentials.common.modules.economy.utils.SignCreationHelper.createSign;
import static com.wurmcraft.serveressentials.common.modules.economy.utils.SignShopHandler.handleAdminSigns;
import static com.wurmcraft.serveressentials.common.modules.economy.utils.SignShopHandler.handleSigns;

import com.wurmcraft.serveressentials.common.modules.language.ChatHelper;
import com.wurmcraft.serveressentials.common.modules.language.LanguageModule;
import com.wurmcraft.serveressentials.common.reference.NBT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class SignShopEvents {

  public static NonBlockingHashMap<EntityPlayer, BlockPos> tracker = new NonBlockingHashMap<>();
  // TODO Config Option
  public static final ItemStack SHOP_CREATION_TOOL = new ItemStack(Items.REDSTONE, 1, 0);

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
          if (createAdminSign(sign, state, e.getEntityPlayer(), e.getItemStack())
              || createSign(sign, state, e.getEntityPlayer(), e.getItemStack())) {
            ChatHelper.sendMessage(
                e.getEntityPlayer(),
                LanguageModule.getUserLanguage(e.getEntityPlayer()).local.ECO_SIGN_CREATED);
          }
        }
      }
    }
  }

  @SubscribeEvent
  public void onLeftClick(PlayerInteractEvent.LeftClickBlock e) {
    if (e.getItemStack().isItemEqual(SHOP_CREATION_TOOL)
        && e.getEntityPlayer().world.getTileEntity(e.getPos()) instanceof IInventory) {
      tracker.put(e.getEntityPlayer(), e.getPos());
      ChatHelper.sendMessage(
          e.getEntityPlayer(),
          LanguageModule.getUserLanguage(e.getEntityPlayer()).local.ECO_SIGN_LINK_START);
    }
  }
}
