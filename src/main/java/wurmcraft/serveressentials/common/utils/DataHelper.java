package wurmcraft.serveressentials.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import wurmcraft.serveressentials.common.api.storage.*;
import wurmcraft.serveressentials.common.reference.Local;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DataHelper {

    public static final File saveLocation = new File(FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory() + File.separator + wurmcraft.serveressentials.common.reference.Global.NAME);
    public static final File playerDataLocation = new File(saveLocation + File.separator + "Player-Data" + File.separator);
    public static final File warpLocation = new File(saveLocation + File.separator + "Warp" + File.separator);

    public static HashMap<UUID, PlayerData> loadedPlayers = new HashMap<>();
    public static ArrayList<Warp> loadedWarps = new ArrayList<>();
    public static Global globalSettings;

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
        PlayerData data = loadPlayerData(name);
        loadedPlayers.put(name, data);
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
        return Local.HOME_FAILED;
    }

    public static String deleteHome(UUID name, String home) {
        PlayerData data = getPlayerData(name);
        if (data == null)
            data = loadPlayerData(name);
        if (data != null) {
            File playerFileLocation = new File(playerDataLocation + File.separator + name.toString() + ".json");
            String msg = data.delHome(home);
            try {
                Files.write(Paths.get(playerFileLocation.getAbsolutePath()), gson.toJson(data).getBytes());
                reloadPlayerData(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return msg;
        }
        return Local.HOME_ERROR_DELETION.replaceAll("#", home);
    }

    public static void updateTeleportTimer(UUID name) {
        PlayerData data = getPlayerData(name);
        if (data == null)
            data = loadPlayerData(name);
        if (data != null) {
            File playerFileLocation = new File(playerDataLocation + File.separator + name.toString() + ".json");
            data.setTeleport_timer(System.currentTimeMillis());
            try {
                Files.write(Paths.get(playerFileLocation.getAbsolutePath()), gson.toJson(data).getBytes());
                reloadPlayerData(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addMail(Mail mail) {
        PlayerData data = getPlayerData(mail.getReciver());
        boolean wasLoaded = true;
        if (data == null) {
            data = loadPlayerData(mail.getReciver());
            wasLoaded = false;
        }
        if (data != null) {
            File playerFileLocation = new File(playerDataLocation + File.separator + mail.getReciver().toString() + ".json");
            data.addMail(mail);
            try {
                Files.write(Paths.get(playerFileLocation.getAbsolutePath()), gson.toJson(data).getBytes());
                if (wasLoaded)
                    reloadPlayerData(mail.getReciver());
                else
                    unloadPlayerData(mail.getReciver());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ITextComponent displayLocation(Home home) {
        TextComponentString text = new TextComponentString("X = " + home.getPos().getX() + " Y = " + home.getPos().getY() + " Z = " + home.getPos().getZ());
        text.getStyle().setColor(TextFormatting.GREEN);
        return text;
    }

    public static ITextComponent displayLocation(Warp warp) {
        TextComponentString text = new TextComponentString("X = " + warp.getPos().getX() + " Y = " + warp.getPos().getY() + " Z = " + warp.getPos().getZ());
        text.getStyle().setColor(TextFormatting.GREEN);
        return text;
    }

    public static ITextComponent displayLocation(SpawnPoint spawn) {
        TextComponentString text = new TextComponentString("X = " + spawn.location.getX() + " Y = " + spawn.location.getY() + " Z = " + spawn.location.getZ());
        text.getStyle().setColor(TextFormatting.GREEN);
        return text;
    }

    public static String createWarp(Warp warp) {
        if (loadedWarps.size() <= 0)
            loadWarps();
        if (!warpLocation.exists())
            warpLocation.mkdirs();
        File warpFileLocation = new File(warpLocation + File.separator + warp.getName() + ".json");
        try {
            warpFileLocation.createNewFile();
            Files.write(Paths.get(warpFileLocation.getAbsolutePath()), gson.toJson(warp).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Local.WARP_CREATED.replaceAll("#", warp.getName());
    }

    public static Warp[] getWarps() {
        if (loadedWarps.size() > 0)
            return loadedWarps.toArray(new Warp[0]);
        return new Warp[0];
    }

    public static void loadWarps() {
        if (warpLocation.exists()) {
            for (File file : warpLocation.listFiles())
                if (file.isFile() && file.getName().endsWith(".json")) {
                    ArrayList<String> lines = new ArrayList<>();
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
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
                    Warp warp = gson.fromJson(temp, Warp.class);
                    if (warp != null)
                        loadedWarps.add(warp);
                }
        }
    }

    public static Warp getWarp(String name) {
        if (loadedWarps.size() <= 0)
            loadWarps();
        if (loadedWarps.size() > 0 && name != null && name.length() > 0) {
            for (Warp warp : loadedWarps)
                if (warp.getName().equalsIgnoreCase(name))
                    return warp;
        }
        return null;
    }

    public static void deleteWarp(Warp warp) {
        loadedWarps.remove(warp);
        for (File file : warpLocation.listFiles())
            if (file.isFile() && file.getName().equalsIgnoreCase(warp.getName() + ".json"))
                file.delete();
    }

    public static void createGlobal(Global global) {
        if (!saveLocation.exists())
            saveLocation.mkdirs();
        File globalFile = new File(saveLocation + File.separator + "Global.json");
        try {
            globalFile.createNewFile();
            Files.write(Paths.get(globalFile.getAbsolutePath()), gson.toJson(global).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadGlobal() {
        File globalLocation = new File(saveLocation + File.separator + "Global.json");
        if (globalLocation.exists()) {
            ArrayList<String> lines = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(globalLocation));
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
            globalSettings = gson.fromJson(temp, Global.class);
        } else
            createGlobal(new Global(null));
    }

    public static void overrideGlobal(Global global) {
        createGlobal(global);
        loadGlobal();
    }
}
