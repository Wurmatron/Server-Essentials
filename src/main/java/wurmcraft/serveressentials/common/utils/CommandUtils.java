package wurmcraft.serveressentials.common.utils;

/**
 * Created by matthew on 6/26/17.
 */
public class CommandUtils {
    public static String[] getArgsAfterCommand (int argPos,String[] args) {
        return ArrayUtils.splice (args,argPos,args.length - 1);
    }
}
