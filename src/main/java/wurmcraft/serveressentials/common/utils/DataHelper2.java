package wurmcraft.serveressentials.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import joptsimple.internal.Strings;
import wurmcraft.serveressentials.common.api.storage.IDataType;
import wurmcraft.serveressentials.common.reference.Keys;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataHelper2 {

	private static final Gson gson = new GsonBuilder ().setPrettyPrinting ().create ();
	private static HashMap <Keys, List <IDataType>> loadedData = new HashMap <> ();

	public static List <IDataType> getData (Keys key) {
		return loadedData.get (key);
	}

	public static void forceSave (File file,IDataType data) {
		if (!file.exists ())
			file.mkdirs ();
		File dataFile = new File (file + File.separator + data.getID () + ".json");
		try {
			dataFile.createNewFile ();
			Files.write (Paths.get (dataFile.getAbsolutePath ()),gson.toJson (data).getBytes ());
		} catch (IOException e) {
			e.printStackTrace ();
		}
	}

	public static <T extends IDataType> T load (File file,Keys key,T type) {
		if (file.exists ()) {
			try {
				String fileData = Strings.join (Files.readAllLines (Paths.get (file.getAbsolutePath ())),"");
				T data = gson.fromJson (fileData,(Class <T>) type.getClass ());
				if (loadedData.containsKey (key)) {
					loadedData.get (key).add (data);
				} else {
					List <IDataType> dataList = new ArrayList <> ();
					dataList.add (data);
					loadedData.put (key,dataList);
				}
				return data;
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
		return null;
	}

	public static boolean createIfNonExist (File file,IDataType data) {
		if (!new File (file + File.separator + data.getID () + ".json").exists ()) {
			forceSave (file,data);
			return true;
		}
		return false;
	}
}
