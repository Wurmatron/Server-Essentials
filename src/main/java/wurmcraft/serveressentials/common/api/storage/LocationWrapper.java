package wurmcraft.serveressentials.common.api.storage;

public class LocationWrapper extends Location {

  private int y;
  private int dim;

  public LocationWrapper(int x, int y, int z, int dim) {
    super(x, z);
    this.y = y;
    this.dim = dim;
  }

  public int getY() {
    return y;
  }

  public int getDim() {
    return dim;
  }

}
