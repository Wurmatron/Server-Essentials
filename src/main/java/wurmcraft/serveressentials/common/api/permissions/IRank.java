package wurmcraft.serveressentials.common.api.permissions;

public interface IRank {

    String getName();

    boolean isDefault();

    String getPrefix();

    String getSuffix();

    String[] getInheritance();

    String[] getPermissions();
}