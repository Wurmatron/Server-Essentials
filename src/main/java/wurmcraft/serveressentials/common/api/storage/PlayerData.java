package wurmcraft.serveressentials.common.api.storage;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.UsernameCache;
import wurmcraft.serveressentials.common.api.permissions.IRank;
import wurmcraft.serveressentials.common.api.team.ITeam;
import wurmcraft.serveressentials.common.api.team.Team;
import wurmcraft.serveressentials.common.chat.ChannelManager;
import wurmcraft.serveressentials.common.reference.Local;
import wurmcraft.serveressentials.common.utils.RankManager;
import wurmcraft.serveressentials.common.utils.TeamManager;
import wurmcraft.serveressentials.common.utils.UsernameResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 Used to hold the data about a player
 */
public class PlayerData implements IDataType {

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
	private boolean isFrozen;
	private boolean tpLock;
	private String[] customData = new String[0];
	private UUID uuid;

	public PlayerData() {}

	public PlayerData (UUID uuid,IRank group) {
		this.rank = group.getName ();
		this.uuid = uuid;
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

	public long getTeleportTimer () {
		return teleport_timer;
	}

	public void setTeleportTimer (long time) {
		teleport_timer = time;
	}

	public List <Mail> getMail () {
		return currentMail;
	}

	public void addMail (Mail mail) {
		currentMail.add (mail);
	}

	public void removeMail (int index) {
		if (index < currentMail.size ())
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

	public void setFirstJoin () {
		this.firstJoin = System.currentTimeMillis ();
	}

	public long getFirstJoin () {
		return this.firstJoin;
	}

	public int getOnlineTime () {
		return onlineTime;
	}

	public void setOnlineTime (int onlineTime) {
		this.onlineTime = onlineTime;
	}

	public boolean isFrozen () {
		return isFrozen;
	}

	public void setFrozen (boolean frozen) {
		isFrozen = frozen;
	}

	public boolean isTpLock () {
		return tpLock;
	}

	public void setTpLock (boolean tpLock) {
		this.tpLock = tpLock;
	}

	public int getMaxHomes () {
		return max_homes;
	}

	public void setMaxHomes (int max) {
		max_homes = max;
		if (max_homes < 0)
			max_homes = 0;
	}

	public String[] getCustomData () {
		return customData;
	}

	public void setCustomData (String[] data) {
		this.customData = customData;
	}

	public void addCustomData (String data) {
		List <String> custData = new ArrayList <> ();
		if (customData != null && customData.length > 0)
			Collections.addAll (custData,customData);
		custData.add (data);
		customData = custData.toArray (new String[0]);
	}

	@Override
	public String getID () {
		return uuid.toString ();
	}
}
