package by.htp.hermanovich.airline.managers;

import by.htp.hermanovich.airline.constants.ConfigConstant;
import java.util.ResourceBundle;

/**
 * Description: This class works with pathpages config-property file.
 * Created by Yauheni Hermanovich on 14.07.2017.
 */
public class ConfigManagerPages {
    private volatile static ConfigManagerPages instance;
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle(ConfigConstant.PATHPAGES_PROPERTIES_SOURCE);

    public ConfigManagerPages() {
    }

    /**
     * Singleton realization with "Double Checked Locking & Volatile" principle for high performance and thread safety.
     *
     * @return       - an instance of the class.
     */
    public static ConfigManagerPages getInstance() {
        if (instance == null) {
            synchronized (ConfigManagerPages.class) {
                if (instance == null) {
                    instance = new ConfigManagerPages();
                }
            }
        }
        return instance;
    }

    /**
     * This method returns path page from pathpages.properties managers files.
     *
     * @param key   - an incoming key to define a property.
     * @return      - path page.
     */
    public String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
