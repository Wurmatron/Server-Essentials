package wurmcraft.serveressentials.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.math.BlockPos;
import wurmcraft.serveressentials.common.api.storage.Claim;
import wurmcraft.serveressentials.common.api.storage.Location;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class ClaimManager {

		public static final  File                           claimLocation = new File(DataHelper.saveLocation + File.separator + "Claims");
		private static final Gson                           gson          = new GsonBuilder().setPrettyPrinting().create();
		public static        HashMap<Location, RegionClaim> loadedClaims  = new HashMap<>();

		public static void saveRegionClaim(byte dimensionID, BlockPos locationInside, RegionClaim claim) {
				File saveLocation = new File(claimLocation + File.separator + Byte.toString(dimensionID));
				if (!saveLocation.exists()) saveLocation.mkdirs(); try {
						File regionFile = new File(saveLocation + File.separator + ChunkHelper.getSaveNameForRegion(locationInside));
						LogHandler.info("A: " + ChunkHelper.getSaveNameForRegion(locationInside));
						Files.write(Paths.get(regionFile.getAbsolutePath()), gson.toJson(claim).getBytes());
				} catch (IOException e) {
						e.printStackTrace();
				}
		}

		public static void loadRegionClaim(int dimensionID, Location location) {
				File saveLocation = new File(claimLocation + File.separator + dimensionID);
				File regionFile   = new File(saveLocation + File.separator + ChunkHelper.getSaveNameForRegion(location));
				if (regionFile.exists()) {
						ArrayList<String> lines = new ArrayList<>(); try {
								BufferedReader reader = new BufferedReader(new FileReader(regionFile)); String line;
								while ((line = reader.readLine()) != null) lines.add(line); reader.close();
						} catch (FileNotFoundException e) {
								e.printStackTrace();
						} catch (IOException e) {
								e.printStackTrace();
						} String temp = ""; for (int s = 0; s <= lines.size() - 1; s++)
								temp = temp + lines.get(s); loadedClaims.put(location, gson.fromJson(temp, RegionClaim.class));
				}
		}

		public static Claim getClaim(int dimensionID, BlockPos pos) {
				if (loadedClaims.size() > 0)
						for (Location loc : loadedClaims.keySet()) {
						Location location = ChunkHelper.getRegionLocation(pos);
						if (loc.getX() == location.getX() && loc.getZ() == location.getZ()) {
								LogHandler.info(location.toString());
								RegionClaim regionClaim = loadedClaims.get(loc);
								return regionClaim.getClaim(pos);
						}
				} return null;
		}
}
