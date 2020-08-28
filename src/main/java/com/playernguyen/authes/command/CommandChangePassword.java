package com.playernguyen.authes.command;

import com.playernguyen.authes.config.ConfigurationFlag;
import com.playernguyen.authes.config.LanguageFlag;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandChangePassword extends CommandAbstract{
    public CommandChangePassword() {
        super("changepassword", "<oldPassword> <newPassword> <confirmPassword>", "Change current password");
    }

    @Override
    public CommandState onCommand(CommandSender sender, List<String> arguments) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Missing arguments
            if (arguments.size() < 3) {
                showHelp(sender);
                return CommandState.NOTHING;
            }
            String oldPassword = arguments.get(0);
            String newPassword = arguments.get(1);
            String confirmPassword = arguments.get(2);
            // Check old password
            if (!getSQLAccountManager().login(player.getUniqueId(), oldPassword)) {
                player.sendMessage(getLanguage().get(LanguageFlag.WRONG_PASSWORD));
                return CommandState.NOTHING;
            }

            // Short password
            if (newPassword.length() < getConfiguration().getInt(ConfigurationFlag.PASSWORD_MIN_SIZE)) {
                player.sendMessage(getLanguage().get(LanguageFlag.PASSWORD_TOO_SHORT));
                return CommandState.NOTHING;
            }

            // Matching confirm
            if (!(newPassword.equalsIgnoreCase(confirmPassword))) {
                player.sendMessage(getLanguage().get(LanguageFlag.PASSWORD_NOT_MATCH));
                return CommandState.NOTHING;
            }


            // Change password fail
            if (!getSQLAccountManager().changePassword(player.getUniqueId(), newPassword)) {
                player.sendMessage(getLanguage().get(LanguageFlag.CHANGE_PASSWORD_FAIL));
                return CommandState.NOTHING;
            }

            player.sendMessage(getLanguage().get(LanguageFlag.CHANGE_PASSWORD_SUCCESS));
            return CommandState.NOTHING;
         }

        return CommandState.INVALID_SENDER;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> arguments) {
        return null;
    }
}
