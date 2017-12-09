package wurmcraft.serveressentials.common.reference;

public enum Perm {

	// Teleport
	SET_HOME ("teleport.setHome"),HOME ("teleport.home"),DEL_HOME ("teleport.deleteHome"),WARP ("teleport.warp"),SPAWN ("teleport.spawn"),TPA ("teleport.tpa"),TPA_ACCEPT ("teleport.tpaAccept"),TPA_DENY ("teleport.tpaDeny"),TPLOCK ("teleport.lock"),RTP ("teleport.rtp"),

	// Admin
	SET_WARP ("admin.setWarp"),DEL_WARP ("admin.delWarp"),SET_SPAWN ("admin.setSpawn"),INVSEE ("admin.invsee"),SUDO ("admin.sudo"),HEAL ("admin.heal"),GAMEMODE ("admin.gameMode"),ADD_RULE ("admin.addRule"),DEL_RULE ("admin.delRule"),ADD_MOTD ("admin.addMotd"),DEL_MOTD ("admin.delMotd"),TEAM_ADMIN ("admin.teamAdmin"),BROADCAST ("admin.broadcast"),SKULL ("admin.skull"),DEL_PLAYER_FILE ("admin.delPlayerFile"),RELOAD_PLAYER_DATA ("admin.reloadPlayerData"),TELEPORT ("admin.teleport"),FREEZE ("admin.freeze"),TOP ("admin.top"),RENAME ("admin.rename"),MUTE ("admin.mute"),SPEED ("admin.speed"),SPY ("admin.spy"),SET_GROUP ("admin.setGroup"),TP_HERE ("admin.tpHere"),KIT_ADMIN ("admin.kitAdmin"),PREGEN ("admin.pregen"),LOCKDOWN ("admin.lockdown"),PERK ("admin.perk"),MODLIST ("admin.modlist"),

	// Perk
	ENDER_CHEST ("perk.enderChest"),FLY ("perm.fly"),BACK ("perm.back"),VAULT ("perm.vault"),NICK ("perm.nick"),

	// Common
	SEEN ("common.seen"),RULES ("common.rules"),MOTD ("common.motd"),TEAM ("common.team"),AFK ("common.afk"),PING ("common.ping"),SUICIDE ("common.suiside"),LIST ("common.list"),CHANNEL ("common.channel"),MONEY ("common.money"),PAY ("common.pay"),MARKET ("common.market"),HELP ("common.help"),WEBSITE ("common.website"),MESSAGE ("common.message"),MAIL ("common.mail"),SEND_ITEM ("common.sendItem"),KIT ("common.kit"),REPLY ("common.reply"),ONLINE_TIME ("common.onlineTime"),AUTORANK ("common.autorank"),BAL_TOP ("eco.baltop"),

	// Claim
	CLAIM ("claim.claim"),DEL_CLAIM ("claim.delClaim"),

	// Security
	CREATIVE ("security.creative");

	private String name;

	Perm (String name) {
		this.name = name;
	}


	@Override
	public String toString () {
		return name;
	}

	public static boolean isValidPerm (String perm) {
		return Perm.valueOf (perm) != null;
	}
}
