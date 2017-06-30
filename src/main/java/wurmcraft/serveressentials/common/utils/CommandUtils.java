package wurmcraft.serveressentials.common.utils;

/**
 Created by matthew on 6/26/17.
 */
public class CommandUtils {

	public static String[] getArgsAfterCommand (int argPos,String[] args) {
		if (argPos < args.length)
			return ArrayUtils.splice (args,argPos,args.length - 1);
		return new String[0];
	}

	public static String[] permute(String s){
		String[] returnArray;
		if(s.length() == 1){
			returnArray = new String[2];
			returnArray[0] = s.toUpperCase();
			returnArray[1] = s.toLowerCase();
			return returnArray;
		}
		String[] permutedArray = permute(s.substring(1));
		returnArray = new String[permutedArray.length*2];
		for(int i = 0; i < permutedArray.length; i++) {
			returnArray[i * 2] = s.substring (0,1).toUpperCase () + permutedArray[i];
			returnArray[i * 2 + 1] = s.substring (0,1).toLowerCase () + permutedArray[i];
		}
		return returnArray;
	}
}
