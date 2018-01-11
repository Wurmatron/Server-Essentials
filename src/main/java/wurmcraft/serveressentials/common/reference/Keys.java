package wurmcraft.serveressentials.common.reference;

public enum Keys {

	PLAYER_DATA("player-data");

	private String name;

	Keys (String name) {
		this.name = name;
	}

	@Override
	public String toString () {
		return name;
	}
}
