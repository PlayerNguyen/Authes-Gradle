package com.playernguyen.authes.command;

import com.playernguyen.authes.account.Account;
import com.playernguyen.authes.config.LanguageFlag;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandLogin extends CommandAbstract {

    public CommandLogin() {
        super("login", "<password>", "Login into server");
    }

    @Override
    public CommandState onCommand(CommandSender sender, List<String> arguments) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            Account account = getAccountManager().getAccountFromUUID(player.getUniqueId());
            // If player aren't register
            if (!account.isRegistered()) {
                player.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.NOT_REGISTER));
                return CommandState.NOTHING;
            }
            // If already login
            if (getSessionManager().hasSession(player.getUniqueId())) {
                player.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.ALREADY_LOGGED_IN));
                return CommandState.NOTHING;
            }
            // Not enough condition
            // Arguments
            if (arguments.size() < 1) {
                player.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.MISSING_LOGIN_COMMAND));
                return CommandState.NOTHING;
            }

            String plainPassword = arguments.get(0);
            // Wrong password
            if (!getSQLAccountManager().login(player.getUniqueId(), plainPassword)) {
                player.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.WRONG_PASSWORD));
                return CommandState.NOTHING;
            }

            // Create session
            if (getSessionManager().createSession(player.getUniqueId())
            && getSQLAccountManager().setLogged(player, player.getUniqueId(), true)) {

                player.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.LOGIN_SUCCESS));
            } else {
                player.sendMessage(getLanguage().getLanguageWithPrefix(LanguageFlag.LOGIN_FAIL));
            }
            return CommandState.NOTHING;
        }
        return CommandState.INVALID_SENDER;
    }

    @Override
    public List<String> onTab(CommandSender sender, List<String> arguments) {
        return null;
    }
}
