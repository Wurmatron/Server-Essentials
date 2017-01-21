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

    // Teleport
    public static final String TELEPORT_COOLDOWN = TextFormatting.RED + "Please wait another '#' seconds!";
}
