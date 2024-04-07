package legendsmpcore.core.utils;

import legendsmpcore.core.GlobalConstants;
import legendsmpcore.customitems.ItemsConstants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Utility to read, parse, write to, and create local files, as well as download files from the web
 * */
public class FileUtils {
    /**
     * @param urlStr entire RAW url
     * @param fileToWrite file to write data from download to
     * */
    public static void downloadUsingStream(String urlStr, File fileToWrite) throws IOException {
        URL url = new URL(urlStr);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(fileToWrite);
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = bufferedInputStream.read(buffer, 0, 1024)) != -1) {
            fileOutputStream.write(buffer, 0, count);
        }
        fileOutputStream.close();
        bufferedInputStream.close();
    }

    public static boolean createNewFile(File fileL) {
        try {
            if (fileL.createNewFile()) {
                Bukkit.getLogger().info("Created " + fileL.getAbsolutePath());
            }
            return FileUtils.writeVersion(fileL);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "Error occurred in creating " +
                fileL.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Writes plugin and config version into local storage
     * */
    public static boolean writeVersion(File file){
        try {
            FileWriter myWriter = new FileWriter(file, false);
            myWriter.write(GlobalConstants.PLUGIN_VERSION);
            myWriter.close();
            Bukkit.getLogger().info("Successfully wrote " + GlobalConstants.PLUGIN_VERSION + " to " +
                    file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "Error occurred in writing to " +
                    file.getAbsolutePath());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Parses config version from local storage config
     * */
    public static String getVersion(File fileV){
        try {
            Scanner var2 = new Scanner(fileV);
            String var3 = var2.nextLine();
            var2.close();
            return var3;
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not find " + fileV.getAbsolutePath());
            e.printStackTrace();
            return String.valueOf(Integer.MAX_VALUE);
        }
    }

    /**
     * Parse a string to get the respectful version it represents
     * */
    public static double[] versionParse(String ver){
        if(ver == null){
            Bukkit.getLogger().info(ChatColor.DARK_RED + ItemsConstants.CHAT_PREFIX +
                    "Unknown version format, please manually check if this is up to date! " + GlobalConstants.GITHUB_REPO);
            return null;
        }

        if(ver.split("\\.").length != 3){
            Bukkit.getLogger().info(ChatColor.DARK_RED + ItemsConstants.CHAT_PREFIX +
                    "Unknown version format, please manually check if this is up to date! " + GlobalConstants.GITHUB_REPO);
            return null;
        }
        return new double[]{
                Double.parseDouble(ver.split("\\.")[0]),
                Double.parseDouble(ver.split("\\.")[1]),
                Double.parseDouble(ver.split("\\.")[2])
        };
    }

}
