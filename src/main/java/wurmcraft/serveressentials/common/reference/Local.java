package wurmcraft.serveressentials.common.reference;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import wurmcraft.serveressentials.common.config.ConfigHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Modifier;

public class Local {

	// Home
	public static String HOME_TELEPORTED;
	public static String HOME_NONE;
	public static String HOME_NONEXISTENT;
	public static String HOME_INVALID;
	public static String HOME_REPLACED;
	public static String HOME_SET;
	public static String HOME_MAX;
	public static String HOME_FAILED;
	public static String HOME_DELETED;
	public static String HOME_ERROR_DELETION;
	// Warp
	public static String WARP_NAME;
	public static String WARP_CREATED;
	public static String WARP_NONE;
	public static String WARP_TELEPORT;
	public static String WARP_DELETE;
	public static String WARPS_NONE;
	// Global
	public static String SPAWN_SET;
	public static String SPAWN_TELEPORTED;
	public static String COMMAND_FORCED;
	public static String COMMAND_SENDER_FORCED;
	public static String LAST_SEEN;
	public static String COMMAND_NOT_FOUND;
	public static String NO_RULES;
	public static String PAGE_NONE;
	public static String RULE_CREATED;
	public static String RULE_REMOVED;
	public static String RULE_INVALID_INDEX;
	public static String MOTD_CREATED;
	public static String MOTD_REMOVED;
	public static String MOTD_INVALID_INDEX;
	public static String NO_MOTD;
	public static String TOP;
	public static String SKULL;
	public static String PLAYER_ONLY;
	public static String AFK_NOW;
	public static String AFK_OFF;
	public static String PING_REPLY;
	public static String PREGEN_NOTIFY;
	public static String PREGEN_FINISHED;
	public static String PREGEN_WARN;
	public static String PREGEN_STARTED;
	public static String PREGEN;
	public static String PREGEN_STOP;
	public static String LOCKDOWN;
	public static String LOCKDOWN_ENABLED;
	public static String LOCKDOWN_DISABLED;
	public static String BANNED_MOD;
	public static String RANK_RELOAD;
	public static String RANK_NOT_FOUND;
	// Player
	public static String PLAYER_NOT_FOUND;
	public static String PLAYER_INVENTORY;
	public static String PLAYER_INVENTORY_ENDER;
	public static String MODE_INVALID;
	public static String MODE_CHANGED;
	public static String MODE_CHANGED_OTHER;
	public static String DATA_RELOADED;
	public static String DATA_RELOADED_OTHER;
	public static String FROZEN;
	public static String FROZEN_OTHER;
	public static String UNFROZEN;
	public static String UNFROZEN_OTHER;
	public static String HEAL_SELF;
	public static String HEAL_OTHER;
	public static String HEAL_OTHER_SENDER;
	public static String FLY_ENABLED;
	public static String FLY_DISABLED;
	public static String FLY_ENABLED_OTHER;
	public static String FLY_DISABLED_OTHER;
	public static String PLAYER_FILE_DELETE;
	public static String PLAYER_FILE_DELETE_OTHER;
	public static String MUTED;
	public static String UNMUTED;
	public static String MUTED_OTHER;
	public static String UNMUTED_OTHER;
	public static String NOTIFY_MUTED;
	public static String NO_VAULTS;
	public static String VAULT_NOT_FOUND;
	public static String VAULT_CREATED;
	public static String VAULT_MAX_HIT;
	public static String VAULT_ITEMS;
	public static String VAULT_DELETED;
	public static String VAULT_NAME;
	public static String SPEED_CHANGED;
	public static String MESSAGE_SENT;
	public static String MISSING_MESSAGE;
	public static String MAIL_SENT;
	public static String NO_MAIL;
	public static String MAIL_REMOVED;
	public static String HAS_MAIL;
	public static String MAIL_INVALID;
	public static String MAIL_REMOVED_ALL;
	public static String INVALID_KIT_NAME;
	public static String KIT_CREATED;
	public static String KIT_NOTFOUND;
	public static String KIT_REMOVED;
	public static String NO_KITS;
	public static String FULL_INV;
	public static String KIT;
	public static String SPY;
	public static String SPY_OTHER;
	public static String NICKNAME_OTHER;
	public static String NICKNAME_SET;
	public static String RANK_CHANGED;
	public static String RANK_UP;
	public static String RANK_UP_NOTIFY;
	public static String NEXT_RANK;
	public static String RANK_MAX;
	public static String SPAM;
	public static String NICK_NONE;
	public static String PERK_CHANGED;
	public static String ONLINE_TIME;
	public static String EXPERIENCE;
	public static String BALANCE;
	public static String GOD_ENABLED;
	public static String GOD_DISABLED;
	// Teleport
	public static String TPA_USERNAME_NONE;
	public static String TPA_USER_NOTFOUND;
	public static String TPA_REQUEST_SENT;
	public static String TPA_REQUEST;
	public static String TPA_DENY;
	public static String TPA_ACCEPED_OTHER;
	public static String TPA_ACCEPTED;
	public static String TELEPORT_COOLDOWN;
	public static String TPA_NONE;
	public static String TELEPORT_BACK;
	public static String TELEPORTED;
	public static String TELEPORT_TO;
	public static String TELEPORTED_FROM;
	public static String INVALID_HOME_NAME;
	public static String NO_TPA;
	public static String TPA_SELF;
	public static String INVALID_LASTLOCATION;
	public static String TPLOCK;
	public static String RAND_TP;
	public static String RTP_FAIL;
	// Team
	public static String TEAM_CREATE_MISSING_NAME;
	public static String TEAM_CREATED;
	public static String TEAM_INVALID;
	public static String TEAM_JOINED;
	public static String TEAM_LEFT;
	public static String TEAM_LEADER_PERM;
	public static String TEAM_MISSING_NAME;
	public static String TEAM_INVITED;
	public static String TEAM_INVITED_OTHER;
	public static String TEAM_KICKED;
	public static String TEAM_KICKED_OTHER;
	public static String TEAMADMIN_DISBAND;
	public static String TEAM_NONE;
	public static String TEAM_SET_VALUE;
	public static String TEAM_INVAID_VALUE;
	// Protection
	public static String CLAIM_BREAK;
	public static String CLAIM_PLACE;
	public static String CLAIM_INTERACT;
	public static String CLAIM_EXPLOSION;
	public static String CHUNK_CLAIMED;
	public static String CHUNK_ALREADY_CLAIMED;
	public static String CLAIM_REMOVED;
	public static String MISSING_CLAIM;
	public static String CLAIM_DISABLED;
	// Chat
	public static String SPACER;
	public static String CHANNEL_CHANGED;
	public static String CHANNEL_PERMS;
	public static String CHANNEL_INVALID;
	public static String INVALID_NUMBER;
	public static String WEBSITE;
	// Item
	public static String NO_ITEM;
	public static String NAME_CHANGED;
	public static String ITEM_SENT;
	public static String ITEM_NONE;
	// Eco
	public static String CURRENT_MONEY;
	public static String CURRENT_MONEY_OTHER;
	public static String NEGATIVE_MONEY;
	public static String MISSING_MONEY;
	public static String MONEY_SENT;
	public static String MONEY_SENT_RECEIVER;
	public static String PURCHASE;
	public static String MISSING_STACK;
	public static String SELL_STACK;
	public static String SIGN_INVALID;
	public static String MONEY_NONE;
	public static String PLAYER_INVENTORY_FULL;
	public static String ITEM_SOLD;
	public static String LINK_CHEST;
	public static String SIGN_FIRST;
	public static String LINKED;
	// Security
	public static String SECURITY_CREATIVE_KICK;

	public static void load () {
		Gson gson = new GsonBuilder ().excludeFieldsWithModifiers (Modifier.TRANSIENT).setPrettyPrinting ().create ();
		File lang = new File (ConfigHandler.saveLocation + File.separator + "Language" + File.separator + ConfigHandler.defaultLang + ".lang");
		if (lang.exists ()) {
			try {
				BufferedReader reader = new BufferedReader (new FileReader (lang));
				gson.fromJson (reader,Local.class);
			} catch (Exception e) {
				e.printStackTrace ();
			}
		}
	}
}
