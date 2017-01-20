package wurmcraft.serveressentials.common.api.storage;

import wurmcraft.serveressentials.common.utils.LogHandler;

/**
 * Used to hold the data about a player
 */
public class PlayerData {

    private int max_homes = 4;
    private Home[] homes = new Home[max_homes];

    public PlayerData(Home[] homes) {
        if (homes != null && homes.length > 0)
            this.homes = homes;
    }

    public Home[] getHomes() {
        return homes;
    }

    public Home getHome(String name) {
        for (Home h : homes)
            if (h.getName().equalsIgnoreCase(name))
                return h;
        return null;
    }

    public void setHomes(Home[] homes) {
        if (homes != null && homes.length > 0)
            this.homes = homes;
    }

    public String addHome(Home home) {
        if (home != null && homes != null && home.getName() != null) {
            boolean temp = false;
            for (int index = 0; index < homes.length; index++) {
                if (homes[index] != null && homes[index].getName().equalsIgnoreCase(home.getName())) {
                    homes[index] = home;
                    return "chat.homeReplaced.name";
                } else if (homes[index] == null) {
                    homes[index] = home;
                    return "chat.homeSet.name";
                }
            }
            return "chat.homeMax.name";
        }
        return "chat.homeError.name";
    }
}
