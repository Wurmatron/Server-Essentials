package wurmcraft.serveressentials.common.reference;

import net.minecraft.util.text.TextFormatting;
import wurmcraft.serveressentials.common.config.Settings;

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
	// Warp
	public static final String WARP_NAME = TextFormatting.RED + "Invalid Warp Name";
	public static final String WARP_CREATED = TextFormatting.AQUA + "Warp \"#\" created!";
	public static final String WARP_NONE = TextFormatting.RED + "No Warp \"#\" found!";
	public static final String WARP_TELEPORT = TextFormatting.AQUA + "Teleported to warp \"#\"";
	public static final String WARP_DELETE = TextFormatting.RED + "Warp \"#\" deleted!";
	public static final String WARPS_NONE = TextFormatting.RED + "No warps set!";
	// Global
	public static final String SPAWN_SET = TextFormatting.GREEN + "Spawn set in dimension '@'";
	public static final String SPAWN_TELEPORTED = TextFormatting.GREEN + "Teleported to spawn";
	public static final String COMMAND_FORCED = TextFormatting.BLUE + "You have been forced to run a command!";
	public static final String COMMAND_SENDER_FORCED = TextFormatting.GREEN + "Player \"#\" has been forced to run ";
	public static final String LAST_SEEN = TextFormatting.GREEN + "Last Seen # ago";
	public static final String COMMAND_NOT_FOUND = TextFormatting.RED + "Command not found!";
	public static final String NO_RULES = TextFormatting.RED + "No Rules have been specified!";
	public static final String PAGE_NONE = TextFormatting.RED + "Page '#' does not exist, the max page number is \"$\"";
	public static final String RULE_CREATED = TextFormatting.BLUE + "Rule \"#\" created!";
	public static final String RULE_REMOVED = TextFormatting.RED + "Rule \"#\" removed!";
	public static final String RULE_INVALID_INDEX = TextFormatting.RED + "Rule \"#\" not found!";
	public static final String MOTD_CREATED = TextFormatting.BLUE + "Motd \"#\" created!";
	public static final String MOTD_REMOVED = TextFormatting.RED + "Motd \"#\" removed!";
	public static final String MOTD_INVALID_INDEX = TextFormatting.RED + "Motd \"#\" not found!";
	public static final String NO_MOTD = TextFormatting.RED + "No MOTD have been specified!";
	public static final String TOP = TextFormatting.GREEN + "You have been teleport to the top";
	public static final String SKULL = TextFormatting.AQUA + "#'s head has been placed in your inventory!";
	public static final String PLAYER_ONLY = TextFormatting.RED + "Command can only be run by players!";
	public static final String AFK_NOW = TextFormatting.RED + "# is not afk!";
	public static final String AFK_OFF = TextFormatting.RED + "# is no longer afk!";
	public static final String PING_REPLY = TextFormatting.AQUA + "NepNep NepNep Nep?";
	// Player
	public static final String PLAYER_NOT_FOUND = TextFormatting.RED + "Player \"#\" not found!";
	public static final String PLAYER_INVENTORY = TextFormatting.GREEN + "Opening Player \"#\"'s Inventory!";
	public static final String PLAYER_INVENTORY_ENDER = TextFormatting.GREEN + "Opening Player \"#\"'s Ender Chest!";
	public static final String MODE_INVALID = TextFormatting.RED + "Invalid \"#\" Gamemode!";
	public static final String MODE_CHANGED = TextFormatting.GREEN + "Your gamemode has been changed to #";
	public static final String MODE_CHANGED_OTHER = TextFormatting.GREEN + "You have chanced \"#\"'s Gamemode to $";
	public static final String DATA_RELOADED = TextFormatting.GOLD + "Your PlayerData has been reloaded!";
	public static final String DATA_RELOADED_OTHER = TextFormatting.GOLD + "#'s PlayerData has been reloaded";
	public static final String FROZEN = TextFormatting.DARK_RED + "You have been frozen";
	public static final String FROZEN_OTHER = TextFormatting.RED + "You have frozen #";
	public static final String UNFROZEN = TextFormatting.DARK_RED + "You have been unfrozen";
	public static final String UNFROZEN_OTHER = TextFormatting.RED + "# has been unfrozen";
	public static final String HEAL_SELF = TextFormatting.AQUA + "You have healed yourself!";
	public static final String HEAL_OTHER = TextFormatting.AQUA + "You have been healed";
	public static final String HEAL_OTHER_SENDER = TextFormatting.AQUA + "'#' has been healed";
	public static final String FLY_ENABLED = TextFormatting.GREEN + "Fly Mode Enabled!";
	public static final String FLY_DISABLED = TextFormatting.RED + "Fly Mode Disabled!";
	public static final String FLY_ENABLED_OTHER = TextFormatting.GREEN + "Fly Mode Enabled for #";
	public static final String FLY_DISABLED_OTHER = TextFormatting.RED + "Fly Mode Disabled for #";
	public static final String PLAYER_FILE_DELETE = TextFormatting.GOLD + "Your Player File has been deleted!";
	public static final String PLAYER_FILE_DELETE_OTHER = TextFormatting.GOLD + "#'s " + TextFormatting.AQUA + "player file has been deleted!";
	public static final String MUTED = TextFormatting.DARK_RED + "You have been muted!";
	public static final String UNMUTED = TextFormatting.DARK_RED + "You have been unmuted!";
	public static final String MUTED_OTHER = TextFormatting.RED + "You have muted #";
	public static final String UNMUTED_OTHER = TextFormatting.RED + "You have unmuted #";
	public static final String NOTIFY_MUTED = TextFormatting.DARK_RED + "You cannot talk in chat you have been muted!";
	public static final String NO_VAULTS = TextFormatting.RED + "You currently do not have any vaults";
	public static final String VAULT_NOT_FOUND = TextFormatting.RED + "Vault # not found!";
	public static final String VAULT_CREATED = TextFormatting.AQUA + "Vault # created!";
	public static final String VAULT_MAX_HIT = TextFormatting.RED + "Max Amount of Vaults Reached!";
	public static final String SPEED_CHANGED = TextFormatting.GREEN + "Your movement speed has changed to #";
	public static final String MESSAGE_SENT = TextFormatting.RED + "Message sent to #";
	public static final String MISSING_MESSAGE = TextFormatting.AQUA + "Missing Message";
	public static final String MAIL_SENT = TextFormatting.AQUA + "Mail Sent!";
	public static final String NO_MAIL = TextFormatting.RED + "No Mail!";
	public static final String MAIL_REMOVED = TextFormatting.AQUA + "Mail Deleted!";
	public static final String HAS_MAIL = TextFormatting.AQUA + "You have Mail!";
	public static final String INVALID_KIT_NAME = TextFormatting.RED + "Invalid Kit Name \"#\"";
	public static final String KIT_CREATED = TextFormatting.GOLD + "Kit \"#\" created!";
	public static final String KIT_NOTFOUND = TextFormatting.RED + "Kit \"#\" not found!";
	public static final String KIT_REMOVED = TextFormatting.AQUA + "Kit \"#\" removed!";
	public static final String NO_KITS = TextFormatting.RED + "No Kits";
	public static final String FULL_INV = TextFormatting.RED + "Inventory full dropping items in the world.";
	public static final String KIT= TextFormatting.AQUA + "Kit \"#\" placed in your inventory!";
	public static final String SPY = TextFormatting.AQUA + "Chat Spy #";
	public static final String SPY_OTHER = TextFormatting.AQUA + "Chat Spy # for &";
	public static final String NICKNAME = TextFormatting.AQUA + "Nickname changed to #";
	public static final String NICKNAME_OTHER = TextFormatting.AQUA + "Nickname for # changed to &";
	public static final String RANK_NOT_FOUND = TextFormatting.RED + "Rank \"#\" not found!";
	public static final String RANK_CHANGED = TextFormatting.AQUA + "Your rank has been changed to \"#\"";
	// Teleport
	public static final String TPA_USERNAME_NONE = TextFormatting.RED + "No Username Found!";
	public static final String TPA_USER_NOTFOUND = TextFormatting.RED + "Player Not Found";
	public static final String TPA_REQUEST_SENT = TextFormatting.AQUA + "Tpa request sent to \"#\"";
	public static final String TPA_REQUEST = TextFormatting.AQUA + "# has sent you a tpa request type /tpaccept to accept";
	public static final String TPA_DENY = TextFormatting.RED + "Tpa request canceled";
	public static final String TPA_ACCEPED_OTHER = TextFormatting.AQUA + "\"#\" has been teleported to your location";
	public static final String TPA_ACCEPTED = TextFormatting.AQUA + "# has accepted your tpa request";
	public static final String TELEPORT_COOLDOWN = TextFormatting.RED + "Please wait another '#' seconds!";
	public static final String TPA_NONE = TextFormatting.RED + "No active TPA request!";
	public static final String TELEPORT_BACK = TextFormatting.AQUA + "You have been sent to your last location!";
	public static final String TELEPORTED = TextFormatting.AQUA + "You have teleported!";
	public static final String TELEPORT_TO = TextFormatting.GREEN + "You have been teleported to #";
	public static final String TELEPORTED_FROM = TextFormatting.AQUA + "# has been teleported to %";
	public static final String INVALID_HOME_NAME = TextFormatting.RED + "Home Name \"#\" invalid!";
	// Team
	public static final String TEAM_CREATE_MISSING_NAME = TextFormatting.RED + "Team name missing or invalid";
	public static final String TEAM_CREATED = TextFormatting.GREEN + "Team \"#\" created!";
	public static final String TEAM_INVALID = TextFormatting.RED + "No Team found with the name \"#\"";
	public static final String TEAM_JOINED = TextFormatting.GREEN + "You have just joined the \"#\" team";
	public static final String TEAM_LEFT = TextFormatting.AQUA + "You have just left the \"#\" team";
	public static final String TEAM_LEADER_PERM = TextFormatting.RED + "You are not the leader for this team.";
	public static final String TEAM_MISSING_NAME = TextFormatting.RED + "Missing player Name";
	public static final String TEAM_INVITED = TextFormatting.AQUA + "Player \"#\" invited";
	public static final String TEAM_INVITED_OTHER = TextFormatting.AQUA + "You have been invited to join the \"#\" team";
	public static final String TEAM_KICKED = TextFormatting.RED + "You have been forfully kicked from your team";
	public static final String TEAM_KICKED_OTHER = TextFormatting.RED + "You have forfully kicked \"#\" from your team";
	public static final String TEAMADMIN_DISBAND = TextFormatting.DARK_RED + "You have disbanded the \"#\" team!";
	// Protection
	public static final String CLAIM_BREAK = TextFormatting.RED + "You do not have " + TextFormatting.GOLD + "#'s" + TextFormatting.RED + " permission to break this!";
	public static final String CLAIM_PLACE = TextFormatting.RED + "You do not have " + TextFormatting.GOLD + "#'s" + TextFormatting.RED + " permission to place a block here!";
	public static final String CLAIM_INTERACT = TextFormatting.RED + "You do not have " + TextFormatting.GOLD + "#'s" + TextFormatting.RED + " permission to interact on that block";
	public static final String CLAIM_EXPLOSION = TextFormatting.DARK_RED + "You are not allowed to blow up " + TextFormatting.GOLD + "#'s" + TextFormatting.DARK_RED + " claim!";
	public static final String CHUNK_CLAIMED = TextFormatting.AQUA + "This chunk has been claimed!";
	public static final String CHUNK_ALREADY_CLAIMED = TextFormatting.RED + "This chunk has already been claimed!";
	public static final String CLAIM_REMOVED = TextFormatting.RED + "This claim has been removed!";
	// Chat
	public static final String SPACER = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=";
	public static final String CHANNEL_CHANGED = TextFormatting.RED + "You channel has been changed to #";
	// Item
	public static final String NO_ITEM = TextFormatting.RED + "No Held Item Found";
	public static final String NAME_CHANGED = TextFormatting.AQUA + "Held name changed to " + TextFormatting.GOLD + "#";
	public static final String ITEM_SENT = TextFormatting.AQUA + "Item # sent to @";
	// Eco
	public static final String CURRENT_MONEY = TextFormatting.AQUA + "You currently have " + Settings.currencySymbol + "#";
	public static final String CURRENT_MONEY_OTHER = TextFormatting.AQUA + "# currently has " + Settings.currencySymbol + "%";
	public static final String NEGATIVE_MONEY = TextFormatting.DARK_RED + "You cannot pay someone a negative amount";
	public static final String MISSING_MONEY = TextFormatting.RED + "You don't have " + Settings.currencySymbol + "#";
	public static final String MONEY_SENT = TextFormatting.GOLD + "You have sent " + Settings.currencySymbol + "% to #";
	public static final String MONEY_SENT_RECEIVER = TextFormatting.GOLD + "# has payed you " + Settings.currencySymbol + "%";
	public static final String PURCHASE = TextFormatting.AQUA + "You have purchased # for @";
	public static final String MISSING_STACK = TextFormatting.RED + "You don't have a #";
	public static final String SELL_STACK = TextFormatting.AQUA + "# has been sold to @ for &";
}
