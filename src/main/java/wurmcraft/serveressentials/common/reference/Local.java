package wurmcraft.serveressentials.common.reference;

import net.minecraft.util.text.TextFormatting;

public class Local {
    // Home
    public static final String HOME_TELEPORTED = TextFormatting.AQUA + "Teleported to home \"#\"";
    public static final String HOME_NONE = TextFormatting.BLUE + "No default home set!";
    public static final String HOME_NONEXISTENT = TextFormatting.RED + "No home exists!";
    public static final String HOME_INVALID = TextFormatting.RED + "Invalid home, \"#\" does not exist!";
    public static final String HOME_REPLACED = TextFormatting.GREEN + "Home \"#\" location updated";
    public static final String HOME_SET = TextFormatting.AQUA + "Home \"#\" set";
    public static final String HOME_MAX = TextFormatting.RED + "Failed to set home! Max amount of homes has been reached '#'";
    public static final String HOME_FAILED = TextFormatting.RED + "Failed to set home! Unknown error";
    public static final String HOME_DELETED = TextFormatting.RED + "Home \"#\" deleted!";
    public static final String HOME_ERROR_DELETION = TextFormatting.RED + "Error failed to delete home \"#\"!";
    public static final String WARP_NAME = TextFormatting.RED + "Invalid Warp Name";
    public static final String WARP_CREATED = TextFormatting.AQUA + "Warp \"#\" created!";
    public static final String WARP_NONE = TextFormatting.RED + "No Warp \"#\" found!";
    public static final String WARP_TELEPORT = TextFormatting.AQUA + "Teleported to warp \"#\"";
    public static final String WARP_DELETE = TextFormatting.RED + "Warp \"#\" deleted!";
    public static final String SPAWN_SET = TextFormatting.GREEN + "Spawn set in dimension '@'";
    public static final String SPAWN_TELEPORTED = TextFormatting.GREEN + "Teleported to spawn";
    public static final String PLAYER_NOT_FOUND = TextFormatting.RED + "Player \"#\" not found!";
    public static final String PLAYER_INVENTORY = TextFormatting.GREEN + "Opening Player \"#\"'s Inventory!";
    public static final String PLAYER_INVENTORY_ENDER = TextFormatting.GREEN + "Opening Player \"#\"'s Ender Chest!";
    public static final String COMMAND_NOT_FOUND = TextFormatting.RED + "Command not found!";
    public static final String COMMAND_FORCED = TextFormatting.BLUE + "You have been forced to run a command!";
    public static final String COMMAND_SENDER_FORCED = TextFormatting.GREEN + "Player \"#\" has been forced to run ";
    public static final String LAST_SEEN = TextFormatting.GREEN + "Last Seen # ago";
    public static final String HEAL_SELF = TextFormatting.AQUA + "You have healed yourself!";
    public static final String HEAL_OTHER = TextFormatting.AQUA + "You have been healed";
    public static final String HEAL_OTHER_SENDER = TextFormatting.AQUA + "'#' has been healed";
    public static final String MODE_INVALID = TextFormatting.RED + "Invalid \"#\" Gamemode!";
    public static final String MODE_CHANGED = TextFormatting.GREEN + "Your gamemode has been changed to #";
    public static final String MODE_CHANGED_OTHER = TextFormatting.GREEN + "You have chanced \"#\"'s Gamemode to $";

    // Teleport
    public static final String TELEPORT_COOLDOWN = TextFormatting.RED + "Please wait another '#' seconds!";
}
