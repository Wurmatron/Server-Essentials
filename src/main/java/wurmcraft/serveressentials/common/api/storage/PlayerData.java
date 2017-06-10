package wurmcraft.serveressentials.common.api.storage;

import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.api.team.ITeam;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.RankManager;
import wurmcraft.serveressentials.common.utils.TeamManager;

import java.util.ArrayList;
import java.util.List;

/**
 Used to hold the data about a player
 */
public class PlayerData {

	private String rank;
	private String nickname;
	private int max_homes = 4;
	private long teleport_timer;
	private long lastseen;
	private int money;
	private BlockPos lastLocation;
	private Home[] homes = new Home[max_homes];
	private List <Mail> currentMail = new ArrayList <> ();
	private String team;
	private String currentChannel;
	private boolean muted;
	private int vaultSlots;
	private int marketSlots;
	private boolean spy;
	private long firstJoin;
	private int onlineTime;

	public PlayerData (IRank group) {
		this.rank = group.getName ();
	}

	public Home[] getHomes () {
		return homes;
	}

	public void setHomes (Home[] homes) {
		if (homes != null && homes.length > 0)
			this.homes = homes;
	}

	public Home getHome (String name) {
		if (homes != null)
			for (Home h : homes)
				if (h != null && h.getName ().equalsIgnoreCase (name))
					return h;
		return null;
	}

	public String addHome (Home home) {
		if (home != null && homes != null && home.getName () != null) {
			boolean temp = false;
			for (int index = 0; index < homes.length; index++) {
				if (homes[index] != null && homes[index].getName ().equalsIgnoreCase (home.getName ())) {
					homes[index] = home;
					return Local.HOME_REPLACED.replaceAll ("#",home.getName ());
				} else if (homes[index] == null) {
					homes[index] = home;
					return Local.HOME_SET.replaceAll ("#",home.getName ());
				}
			}
			return Local.HOME_MAX.replaceAll ("#",Integer.toString (max_homes));
		}
		return Local.HOME_INVALID;
	}

	public String delHome (String name) {
		if (name != null && homes != null) {
			for (int index = 0; index < homes.length; index++) {
				if (homes[index] != null && homes[index].getName ().equalsIgnoreCase (name)) {
					homes[index] = null;
					return Local.HOME_DELETED.replaceAll ("#",name);
				}
			}
		}
		return Local.HOME_ERROR_DELETION.replaceAll ("#",name);
	}

	public long getTeleport_timer () {
		return teleport_timer;
	}

	public void setTeleport_timer (long time) {
		teleport_timer = time;
	}

	public List <Mail> getMail () {
		return currentMail;
	}

	public void addMail (Mail mail) {
		currentMail.add (mail);
	}

	public void removeMail (int index) {
		currentMail.remove (index);
	}

	public long getLastseen () {
		return lastseen;
	}

	public void setLastseen (long lastseen) {
		this.lastseen = lastseen;
	}

	public IRank getRank () {
		IRank group = RankManager.getRankFromName (rank);
		if (group != null)
			return group;
		setRank (RankManager.getDefaultRank ());
		return RankManager.getDefaultRank ();
	}

	public void setRank (IRank rank) {
		if (RankManager.getRanks ().contains (rank))
			this.rank = rank.getName ();
		else
			this.rank = RankManager.getDefaultRank ().getName ();
	}

	public void setRank (String rank) {
		IRank group = RankManager.getRankFromName (rank);
		if (group != null)
			setRank (group);
		else
			setRank (RankManager.getDefaultRank ());
	}

	public Team getTeam () {
		if (team != null) {
			Team playerTeam = TeamManager.getTeamFromName (team);
			if (playerTeam != null)
				return playerTeam;
			else
				playerTeam = null;
		}
		return null;
	}

	public void setTeam (ITeam t) {
		if (t != null)
			this.team = t.getName ();
		else
			this.team = null;
	}

	public BlockPos getLastLocation () {
		return lastLocation;
	}

	public void setLastLocation (BlockPos loc) {
		this.lastLocation = loc;
	}

	public int getMoney () {
		return money;
	}

	public void setMoney (int money) {
		this.money = money;
	}

	public void setCurrentChannel (Channel channel) {
		if (channel != null)
			this.currentChannel = channel.getName ();
		else
			this.currentChannel = ChannelManager.getDefaultChannel ().getName ();
	}

	public String getCurrentChannel () {
		return currentChannel;
	}

	public boolean isMuted () {
		return muted;
	}

	public void setMuted (boolean muted) {
		this.muted = muted;
	}

	public int getVaultSlots () {
		return vaultSlots;
	}

	public void setVaultSlots (int vaultSlots) {
		this.vaultSlots = vaultSlots;
	}

	public int getMarketSlots () {
		return marketSlots;
	}

	public void setMarketSlots (int marketSlots) {
		this.marketSlots = marketSlots;
	}

	public boolean isSpy () {
		return spy;
	}

	public void setSpy (boolean spy) {
		this.spy = spy;
	}

	public String getNickname () {
		return nickname;
	}

	public void setNickname (String nickname) {
		this.nickname = nickname;
	}

	public void setFirstJoin() {
		this.firstJoin = System.currentTimeMillis ();
	}

	public long getFirstJoin() {
		return this.firstJoin;
	}

	public int getOnlineTime () {
		return onlineTime;
	}

	public void setOnlineTime (int onlineTime) {
		this.onlineTime = onlineTime;
	}
}
