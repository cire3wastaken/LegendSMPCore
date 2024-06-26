package legendsmpcore.core.utils;

import legendsmpcore.core.LegendCore;

import java.util.List;

/**
 * Methods to work with configurations, and support getXXXorElse()
 * */
public class ConfigurationHelper {
    public static List<String> getStringList(String path, List<String> def){
        List<String> val = LegendCore.getInstance().getConfig().getStringList(path);
        return val != null && !val.isEmpty()? val : def;
    }
}
