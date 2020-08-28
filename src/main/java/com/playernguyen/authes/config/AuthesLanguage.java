package com.playernguyen.authes.config;

import com.playernguyen.authes.AuthesInstance;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AuthesLanguage extends AuthesInstance {

    private final File file;
    private final FileConfiguration fileConfiguration;

    public AuthesLanguage() throws IOException {
        this.file = new File(getInstance().getDataFolder(), "language.yml");
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);

        // Capture the flags
        for (LanguageFlag value : LanguageFlag.values()) {
            if (!fileConfiguration.contains(value.getPath())) {
                fileConfiguration.set(value.getPath(), value.getDeclare());
            }
        }

        // Save item
        this.save();
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public String get(LanguageFlag flag) {
        return ChatColor.translateAlternateColorCodes('&',
                Objects.requireNonNull(getFileConfiguration().getString(flag.getPath())));
    }

    private void save() throws IOException {
        this.fileConfiguration.save(this.file);
    }
}
