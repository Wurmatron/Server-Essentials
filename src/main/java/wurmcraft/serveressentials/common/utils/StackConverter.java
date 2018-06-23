package wurmcraft.serveressentials.common.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class StackConverter {

  // Copied from WurmTweaks 2
  public static String convertToString(ItemStack stack) {
    if (stack != null) {
      String item = "";
      String unlocalizedName =
          stack.getItem().getRegistryName().getResourceDomain() + ":" + stack.getItem()
              .getRegistryName().getResourcePath();
      if (unlocalizedName.length() > 0) {
        int stackSize = stack.getCount();
        int meta = stack.getItemDamage();
        String nbt = "";
        if (stack.getTagCompound() != null) {
          nbt = stack.getTagCompound().toString();
        }
        item = "[" + unlocalizedName + "]";
        item = item + "x" + stackSize;
        item = item + "@" + meta;
        if (nbt != null && nbt.length() > 0) {
          item = item + "%" + nbt;
        }
        return item;
      }
    }
    return null;
  }

  // Copied from WurmTweaks 2
  public static ItemStack convertToStack(String stringStack) {
    if (stringStack != null && stringStack.length() > 0 && stringStack.startsWith("[")
        && stringStack.contains("]") && stringStack.contains(":")
        && stringStack.indexOf("]x") != -1) {
      String modid = stringStack.substring(stringStack.indexOf("[") + 1, stringStack.indexOf(":"));
      String unlocalizedName = stringStack
          .substring(stringStack.indexOf(":") + 1, stringStack.indexOf("]"));
      int stackSize = Integer
          .parseInt(stringStack.substring(stringStack.indexOf("]") + 2, stringStack.indexOf("@")));
      if (stringStack.contains("%")) {
        int meta = Integer.parseInt(
            stringStack.substring(stringStack.indexOf("@") + 1, stringStack.indexOf("%")));
        try {
          NBTTagCompound nbt = JsonToNBT.getTagFromJson(
              stringStack.substring(stringStack.indexOf("%") + 1, stringStack.length()));
          ItemStack stack = GameRegistry
              .makeItemStack(modid + ":" + unlocalizedName, meta, stackSize, "");
          stack.setTagCompound(nbt);
          return stack;
        } catch (NBTException e) {
          e.printStackTrace();
          return null;
        }
      } else {
        int meta = Integer
            .parseInt(stringStack.substring(stringStack.indexOf("@") + 1, stringStack.length()));
        return GameRegistry.makeItemStack(modid + ":" + unlocalizedName, meta, stackSize, "");
      }
    }
    return null;
  }
}
