package com.playernguyen.authes.command;

import com.playernguyen.authes.config.LanguageFlag;
import org.apache.commons.mail.EmailException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class CommandForgotPassword extends CommandAbstract {

    public CommandForgotPassword() {
        super("forgotpassword", "", "Forgot your password");
    }

    @Override
    public CommandState onCommand(CommandSender sender, List<String> arguments) {

        if (sender instanceof Player) {

            Player player = (Player) sender;
            Bukkit.getScheduler().runTaskAsynchronously(getInstance(), () -> {
                try {
                    getSQLAccountManager().regenerateRecoveryKey(player.getUniqueId());
                    getMailSender().sendRecoveryMail(player.getUniqueId());
                    player.sendMessage(getLanguage().get(LanguageFlag.RECOVERY_PASSWORD_SENT));

                    Bukkit.getScheduler().runTask(getInstance(), () -> player.kickPlayer(getLanguage().get(LanguageFlag.RECOVERY_PASSWORD_SENT)));
                } catch (EmailException | IOException e) {
                    e.printStackTrace();
                }
            });

            return CommandState.NOTHING;
        }
        return CommandState.INVALID_SENDER;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> arguments) {
        return null;
    }
}
