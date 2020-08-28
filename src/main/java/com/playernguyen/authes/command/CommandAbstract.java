package com.playernguyen.authes.command;

import com.playernguyen.authes.AuthesInstance;
import com.playernguyen.authes.config.LanguageFlag;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Arrays;
import java.util.List;

public abstract class CommandAbstract extends AuthesInstance implements TabExecutor {

    private final String command;
    private final String parameter;
    private final String description;

    public CommandAbstract(String command) {
        this.command = command;
        this.parameter = "";
        this.description = "";
    }

    public CommandAbstract(String command, String parameter) {
        this.command = command;
        this.parameter = parameter;
        this.description = "";
    }

    public CommandAbstract(String command, String parameter, String description) {
        this.command = command;
        this.parameter = parameter;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public abstract CommandState onCommand(CommandSender sender, List<String> arguments);

    public abstract List<String> onTab(CommandSender sender, List<String> arguments);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // No permission detect
        if (!sender.hasPermission("authes.*")
                || !sender.hasPermission("authes.".concat(getCommand()))) {
            sender.sendMessage(getLanguage().get(LanguageFlag.NO_PERMISSION_COMMAND));
            return true;
        }

        CommandState commandState = onCommand(sender, Arrays.asList(args));
        switch (commandState) {
            case NO_PERMISSION: {
                sender.sendMessage(getLanguage().get(LanguageFlag.NO_PERMISSION_COMMAND));
                return true;
            }

            case INVALID_SENDER: {
                sender.sendMessage(getLanguage().get(LanguageFlag.INVALID_SENDER));
                return true;
            }

            case NOTHING: {
                return true;
            }

            default:
                throw new IllegalStateException("Unexpected value: " + commandState);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return onTab(sender, Arrays.asList(args));
    }

    public String buildHelp() {
        return ChatColor.GOLD  + " /"+ command + " " + ChatColor.LIGHT_PURPLE + parameter + ": " + ChatColor.GRAY + description;
    }

    public void showHelp(CommandSender sender){
        sender.sendMessage(buildHelp());
    }
}
