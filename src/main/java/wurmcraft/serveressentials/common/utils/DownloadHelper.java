package wurmcraft.serveressentials.common.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadHelper {

	public static void save (String webFile,File saveLocation) {
		URL textFile = null;
		File location;
		try {
			textFile = new URL (webFile);
		} catch (MalformedURLException e) {
			e.printStackTrace ();
		}
		location = saveLocation;
		if (location != null)
			try {
				if (!location.exists ())
					FileUtils.copyURLToFile (textFile,location,10000,12000);
			} catch (IOException e) {
				LogHandler.info ("Cannot read " + textFile.getPath () + " I/O Exception");
				e.printStackTrace ();
			}
	}
}
