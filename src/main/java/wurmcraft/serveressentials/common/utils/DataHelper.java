package wurmcraft.serveressentials.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import wurmcraft.serveressentials.common.api.storage.Home;
import wurmcraft.serveressentials.common.api.storage.PlayerData;
import wurmcraft.serveressentials.common.config.Settings;
import wurmcraft.serveressentials.common.reference.Global;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static wurmcraft.serveressentials.common.config.ConfigHandler.location;

public class DataHelper {

    public static final File saveLocation = new File(FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory() + File.separator + Global.NAME);
    public static final File playerDataLocation = new File(saveLocation + File.separator + "Player-Data" + File.separator);

    public static HashMap<UUID, PlayerData> loadedPlayers = new HashMap<>();

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void registerPlayer(EntityPlayer player) {
        if (!loadedPlayers.containsKey(player.getGameProfile().getId())) {
            PlayerData data = loadPlayerData(player.getGameProfile().getId());
            if (data != null)
                loadedPlayers.put(player.getGameProfile().getId(), data);
            else {
                PlayerData newData = new PlayerData(null);
                createPlayerFile(player.getGameProfile().getId(), newData);
                loadedPlayers.put(player.getGameProfile().getId(), newData);
            }
        }
    }

    private static void createPlayerFile(UUID name, PlayerData data) {
        if (!playerDataLocation.exists())
            playerDataLocation.mkdirs();
        File playerFileLocation = new File(playerDataLocation + File.separator + name.toString() + ".json");
        try {
            playerFileLocation.createNewFile();
            Files.write(Paths.get(playerFileLocation.getAbsolutePath()), gson.toJson(data).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerData getPlayerData(UUID name) {
        return loadedPlayers.get(name);
    }

    public static PlayerData forceLoadPlayerData(UUID name, boolean forced) {
        if (forced && getPlayerData(name) == null) {
            File playerFileLocation = new File(playerDataLocation + File.separator + name.toString() + ".json");
            if (playerFileLocation.exists()) {
                PlayerData data = loadPlayerData(name);
                if (data != null)
                    loadedPlayers.put(name, data);
            } else
                return null;
        } else if (!forced)
            return getPlayerData(name);
        return null;
    }

    public static PlayerData loadPlayerData(UUID name) {
        File playerFileLocation = new File(playerDataLocation + File.separator + name.toString() + ".json");
        if (playerFileLocation.exists()) {
            ArrayList<String> lines = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(playerFileLocation));
                String line;
                while ((line = reader.readLine()) != null)
                    lines.add(line);
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String temp = "";
            for (int s = 0; s <= lines.size() - 1; s++)
                temp = temp + lines.get(s);
            return gson.fromJson(temp, PlayerData.class);
        }
        return null;
    }

    public static void unloadPlayerData(UUID name) {
        if (loadedPlayers.containsKey(name))
            loadedPlayers.remove(name);
    }

    public static void reloadPlayerData(UUID name) {
        unloadPlayerData(name);
        loadPlayerData(name);
    }

    public static String addPlayerHome(UUID name, Home home) {
        PlayerData data = getPlayerData(name);
        if (data == null)
            data = loadPlayerData(name);
        if (data != null) {
            File playerFileLocation = new File(playerDataLocation + File.separator + name.toString() + ".json");
            String msg = data.addHome(home);
            try {
                Files.write(Paths.get(playerFileLocation.getAbsolutePath()), gson.toJson(data).getBytes());
                reloadPlayerData(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return msg;
        }
        return "chat.homeError.name";
    }
}
