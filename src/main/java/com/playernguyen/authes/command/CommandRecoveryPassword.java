package com.playernguyen.authes.command;

import com.playernguyen.authes.config.LanguageFlag;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandRecoveryPassword extends CommandAbstract {

    public CommandRecoveryPassword() {
        super("recoverypassword", "", "Recovery the password");
    }

    @Override
    public CommandState onCommand(CommandSender sender, List<String> arguments) {

        if (sender instanceof Player) {
            Player player = ((Player) sender);
            if (arguments.size() < 3) {
                showHelp(sender);
                return CommandState.NOTHING;
            }

            // Verify the key code
            String recovery = arguments.get(0);
            String password = arguments.get(1);
            String rePassword = arguments.get(2);
            if (!getSQLAccountManager().validRecoveryKey(player.getUniqueId(), recovery)) {
                player.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.RECOVERY_CODE_INVALID));
                return CommandState.NOTHING;
            }

            // Matching
            if (!password.equalsIgnoreCase(rePassword)) {
                player.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.PASSWORD_NOT_MATCH));
                return CommandState.NOTHING;
            }

            // Fail to change password
            if (!getSQLAccountManager().changePassword(player.getUniqueId(), password)) {
                player.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.CHANGE_PASSWORD_FAIL));
                return CommandState.NOTHING;
            }

            getSQLAccountManager().regenerateRecoveryKey(player.getUniqueId());
            player.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.CHANGE_PASSWORD_SUCCESS));

            return CommandState.NOTHING;
        }

        return CommandState.INVALID_SENDER;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> arguments) {
        return null;
    }
}
