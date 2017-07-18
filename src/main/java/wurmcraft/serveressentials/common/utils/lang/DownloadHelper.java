package wurmcraft.serveressentials.common.utils.lang;

import org.apache.commons.io.FileUtils;
import wurmcraft.serveressentials.common.utils.LogHandler;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadHelper {

	public URL textFile;
	public File location;

	public DownloadHelper (String webFile,File saveLocation,boolean save) {
		try {
			textFile = new URL (webFile);
		} catch (MalformedURLException e) {
			e.printStackTrace ();
		}
		location = saveLocation;
		if (location != null && save)
			try {
				if (!location.exists ())
					FileUtils.copyURLToFile (textFile,location,10000,12000);
			} catch (IOException e) {
				LogHandler.info ("Cannot read " + textFile.getPath () + " I/O Exception");
				e.printStackTrace ();
			}
	}
}
