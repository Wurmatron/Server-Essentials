package wurmcraft.serveressentials.common.reference;

public enum Keys {

	PLAYER_DATA ("player-data"),WARP ("warp"),KIT ("kit"),TEAM ("team"),RANK ("rank"),AUTO_RANK ("auto-rank"),CHANNEL ("channel");

	private String name;

	Keys (String name) {
		this.name = name;
	}

	@Override
	public String toString () {
		return name;
	}
}
