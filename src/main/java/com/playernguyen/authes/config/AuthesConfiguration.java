package com.playernguyen.authes.config;

import com.playernguyen.authes.AuthesInstance;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AuthesConfiguration extends AuthesInstance {

    private final File file;
    private final YamlConfiguration fileConfiguration;

    public AuthesConfiguration() throws IOException {
        // Load configuration
        this.file = new File(getInstance().getDataFolder(), "config.yml");
        // Load configuration
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
        // Valuable set default
        for (ConfigurationFlag value : ConfigurationFlag.values()) {
            if (!fileConfiguration.contains(value.getPath())) {
                // Set
                this.fileConfiguration.set(value.getPath(), value.getDeclare());
            }
        }
        // Save configuration
        this.save();
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public Object getByFlag(ConfigurationFlag flag) {
        return getFileConfiguration().get(flag.getPath());
    }

    public String getString(ConfigurationFlag flag) {
        return (String) getByFlag(flag);
    }

    public int getInt(ConfigurationFlag flag) {
        return (int) getByFlag(flag);
    }

    public boolean getBoolean(ConfigurationFlag flag) {
        return (boolean) getByFlag(flag);
    }

    public void save() throws IOException {
        this.fileConfiguration.save(this.file);
    }

}
