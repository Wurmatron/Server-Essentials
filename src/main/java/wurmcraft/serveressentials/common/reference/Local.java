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
    public static final String NO_RULES = TextFormatting.RED + "No Rules have been specified!";
    public static final String PAGE_NONE = TextFormatting.RED + "Page '#' does not exist, the max page number is \"$\"";
    public static final String RULE_CREATED = TextFormatting.BLUE + "Rule \"#\" created!";
    public static final String RULE_REMOVED = TextFormatting.RED + "Rule \"#\" removed!";
    public static final String RULE_INVALID_INDEX = TextFormatting.RED + "Rule \"#\" not found!";
    public static final String MOTD_CREATED = TextFormatting.BLUE + "Motd \"#\" created!";
    public static final String MOTD_REMOVED = TextFormatting.RED + "Motd \"#\" removed!";
    public static final String MOTD_INVALID_INDEX = TextFormatting.RED + "Motd \"#\" not found!";
    public static final String NO_MOTD = TextFormatting.RED + "No MOTD have been specified!";
    public static final String TPA_USERNAME_NONE = TextFormatting.RED + "No Username Found!";
    public static final String TPA_USER_NOTFOUND = TextFormatting.RED + "Player Not Found";
    public static final String TPA_REQUEST_SENT = TextFormatting.AQUA + "Tpa request sent to \"#\"";
    public static final String TPA_REQUEST = TextFormatting.AQUA + "# has sent you a tpa request type /tpaccept to accept";
    public static final String TPA_DENY = TextFormatting.RED + "Tpa request canceled";
    public static final String TPA_ACCEPED_OTHER = TextFormatting.AQUA + "\"#\" has been teleported to your location";
    public static final String TPA_ACCEPTED = TextFormatting.AQUA + "# has accepted your tpa request";
    public static final String TEAM_CREATE_MISSING_NAME = TextFormatting.RED + "Team name missing or invalid";
    public static final String TEAM_CREATED = TextFormatting.GREEN + "Team \"#\" created!";
    public static final String TEAM_INVALID = TextFormatting.RED + "No Team found with the name \"#\"";
    public static final String TEAM_JOINED = TextFormatting.GREEN + "You have just joined the \"#\" team";
    public static final String TEAM_LEFT = TextFormatting.AQUA + "You have just left the \"#\" team";
    public static final String TEAM_LEADER_PERM = TextFormatting.RED + "You are not the leader for this team.";
    public static final String TEAM_MISSING_NAME = TextFormatting.RED + "Missing player Name";
    public static final String TEAM_INVITED = TextFormatting.AQUA + "Player \"#\" invited";
    public static final String TEAM_INVITED_OTHER = TextFormatting.AQUA + "You have been invited to join the \"#\" team";

    // Teleport
    public static final String TELEPORT_COOLDOWN = TextFormatting.RED + "Please wait another '#' seconds!";
}
