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
    public static final String SPAWN_SET = TextFormatting.GREEN + "SpawnCommand set for dimension '@'";
    public static final String SPAWN_TELEPORTED = TextFormatting.GREEN + "Teleported to spawn";

    // Teleport
    public static final String TELEPORT_COOLDOWN = TextFormatting.RED + "Please wait another '#' seconds!";
}
