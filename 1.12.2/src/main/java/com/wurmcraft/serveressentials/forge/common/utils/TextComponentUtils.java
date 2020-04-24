package com.wurmcraft.serveressentials.forge.common.utils;

import static com.wurmcraft.serveressentials.forge.api.command.SECommand.COMMAND_INFO_COLOR;
import static net.minecraft.util.text.event.ClickEvent.Action.SUGGEST_COMMAND;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.HoverEvent.Action;

public class TextComponentUtils {

  public static ITextComponent addPlayerComponent(ITextComponent component,
      EntityPlayer player) {
    component.getStyle().setHoverEvent(new HoverEvent(
        Action.SHOW_TEXT, new TextComponentString(
        COMMAND_INFO_COLOR + player.getDisplayNameString() + " (" + player
            .getGameProfile().getId().toString() + ")")));
    return component;
  }

  public static ITextComponent addClickCommand(ITextComponent component, String command) {
    component.getStyle().setClickEvent(new ClickEvent(SUGGEST_COMMAND, command));
    return component;
  }

}
