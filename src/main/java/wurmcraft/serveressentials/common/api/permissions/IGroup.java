package wurmcraft.serveressentials.common.api.permissions;

public interface IGroup {

    String getName();

    boolean isDefault();

    String getPrefix();

    String getSuffix();

    IGroup[] getInheritance();

    String[] getPermissions();
}