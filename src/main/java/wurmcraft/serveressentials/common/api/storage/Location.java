package wurmcraft.serveressentials.common.api.storage;

/**
 Stores x and z location
 */
public class Location {

	private int x;
	private int z;

	public Location (int x,int z) {
		this.x = x;
		this.z = z;
	}

	public int getX () {
		return x;
	}

	public int getZ () {
		return z;
	}

	@Override
	public String toString () {
		return "Location{X:" + x + " Z:" + z + "}";
	}
}
