package com.playernguyen.authes;

import com.playernguyen.authes.account.Account;
import com.playernguyen.authes.account.AccountManager;
import com.playernguyen.authes.account.SQLAccountManager;
import com.playernguyen.authes.account.SessionManager;
import com.playernguyen.authes.command.*;
import com.playernguyen.authes.config.AuthesConfiguration;
import com.playernguyen.authes.config.AuthesLanguage;
import com.playernguyen.authes.config.ConfigurationFlag;
import com.playernguyen.authes.listener.*;
import com.playernguyen.authes.logger.AuthesLoggerFilter;
import com.playernguyen.authes.mail.MailSender;
import com.playernguyen.authes.schedule.AuthesForceLogin;
import com.playernguyen.authes.sql.MySQLEstablishment;
import com.playernguyen.authes.sql.SQLEstablishment;
import com.playernguyen.authes.util.FileUtils;
import com.playernguyen.authes.util.MySQLUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Authes extends JavaPlugin {

    private static Authes instance;
    private AuthesConfiguration configuration;
    private SQLEstablishment establishment;
    private com.playernguyen.authes.account.SQLAccountManager SQLAccountManager;
    private ListenerManager listenerManager;
    private AccountManager accountManager;
    private SessionManager sessionManager;
    private AuthesLanguage authesLanguage;
    private CommandManager commandManager;
    private MailSender mailSender;



    @Override
    public void onEnable() {
        instance = this;
        // Set up file
        setupConfiguration();
        // Set up the connection
        setupConnection();
        // Set up accounts
        setupAccount();
        // Set up listener
        setupListener();
        // Session
        setupSession();
        // Set up command
        setupCommand();
        // Set up resource
        setupResource();
        // Set up mail services
        setupMail();
        // Set up logger
        setupLogger();
    }

    private void setupLogger() {
        // Set up logger
        new AuthesLoggerFilter().registerFilter();
    }

    private void setupMail() {
        this.mailSender = new MailSender();
    }

    @Override
    public void onDisable() {
        // Session delete, for secure
        getSessionManager().getContainer().clear();
    }

    private void setupCommand() {
        this.commandManager = new CommandManager();
        // Append command
        commandManager.add(new CommandRegister());
        commandManager.add(new CommandLogin());
        commandManager.add(new CommandChangeEmail());
        commandManager.add(new CommandChangePassword());
        commandManager.add(new CommandForgotPassword());
        commandManager.add(new CommandRecoveryPassword());
        // Register command
        commandManager.forEach(e -> {
            String command = e.getCommand();
            PluginCommand pluginCommand = Bukkit.getPluginCommand(command);
            // If not found
            if (pluginCommand == null) {
                throw new NullPointerException(String.format(
                        "Cannot found plugin command %s",
                        command
                ));
            }

            // Set executor
            pluginCommand.setExecutor(e);
        });
    }

    private void setupSession() {
        sessionManager = new SessionManager();
        // Request all player who don't have session to logged in
    }

    private void setupAccount() {
        this.SQLAccountManager = new SQLAccountManager();
        this.accountManager = new AccountManager();

        // Which /reload, add all online player :)
        Bukkit.getOnlinePlayers().forEach( player -> {
            Account account = getSQLAccountManager().getAccount(player.getUniqueId());
            getAccountManager().add(account);

            // Authes force login run
            AuthesForceLogin forceLogin = new AuthesForceLogin(player);
            forceLogin.runTaskTimer(this, 20, 20);
        });
    }

    private void setupConfiguration() {
        try {
            this.configuration = new AuthesConfiguration();
            this.authesLanguage = new AuthesLanguage();
        } catch (IOException e) {
            this.getLogger().severe("Cannot saving config.yml...");
            e.printStackTrace();
        }
    }

    private void setupConnection() {
        // Set up the connection
        try {
            this.establishment = new MySQLEstablishment(
                    getConfiguration().getString(ConfigurationFlag.MYSQL_HOST),
                    getConfiguration().getString(ConfigurationFlag.MYSQL_USERNAME),
                    getConfiguration().getString(ConfigurationFlag.MYSQL_PASSWORD),
                    getConfiguration().getString(ConfigurationFlag.MYSQL_DATABASE),
                    getConfiguration().getString(ConfigurationFlag.MYSQL_PORT)
            );
        } catch (ClassNotFoundException e) {
            this.getLogger().severe("Not found driver class of MySQL...");
            e.printStackTrace();
        }
        // Hit the tester & handle table
        this.getLogger().info("Connecting to the MySQL server...");
        try (Connection connection = getEstablishment().openConnection()) {
            this.getLogger().info("Success connect to the MySQL server");
            // Initial the table
            // If not found account table. Initial one
            if (!MySQLUtil.hasTable(connection,
                    getConfiguration().getString(ConfigurationFlag.MYSQL_PREFIX) +
                            getConfiguration().getString(ConfigurationFlag.MYSQL_TABLE_ACCOUNT))
            ) {
                this.getLogger().info("Not found account table. Create the new one...");
                // Create account table. Which storage all players information
                PreparedStatement preparedStatement = connection.prepareStatement(String.format(
                        "CREATE TABLE `%s` (`id` INT(32) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                                "`username` VARCHAR(255) NOT NULL," +
                                "`uuid` VARCHAR(255) NOT NULL," +
                                "`hash` VARCHAR(255) NOT NULL," +
                                "`email` VARCHAR(255) NOT NULL DEFAULT ''," +
                                "`isLogged` VARCHAR(255) NOT NULL DEFAULT '0'," +
                                "`address` VARCHAR(255) NOT NULL," +
                                "`lastLogin` VARCHAR(255) NOT NULL, " +
                                "`recoveryKey` VARCHAR(255) NOT NULL);",
                        getConfiguration().getString(ConfigurationFlag.MYSQL_PREFIX) +
                        getConfiguration().getString(ConfigurationFlag.MYSQL_TABLE_ACCOUNT)
                ));
                // Execute
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            this.getLogger().severe("SQLException. Disable...StackTrace: ");
            e.printStackTrace();
            // catch error and disable plugin
            this.getPluginLoader().disablePlugin(this);
        }

    }

    private void setupListener() {
        this.listenerManager = new ListenerManager();
        // Append event here
        getListenerManager().add(new PlayerJoinListener());
        getListenerManager().add(new PlayerMoveListener());
        getListenerManager().add(new PlayerInteractListener());
        getListenerManager().add(new PlayerQuitListener());
        getListenerManager().add(new PlayerAsyncChatListener());
        getListenerManager().add(new PlayerUseCommandListener());
        // Register listener
        getListenerManager().forEach(e
                -> Bukkit.getServer().getPluginManager().registerEvents(e, this));
    }

    private void setupResource() {
        // email.html
        InputStream resource = this.getResource("email.html");
        File file = new File(getDataFolder(), "email.html");
        try {
            if (!file.exists() && file.createNewFile()) {
                assert resource != null;
                FileUtils.resourceToFile(resource, file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public SQLAccountManager getSQLAccountManager() {
        return SQLAccountManager;
    }

    public AuthesConfiguration getConfiguration() {
        return configuration;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public SQLEstablishment getEstablishment() {
        return establishment;
    }

    public AuthesLanguage getLanguage() {
        return authesLanguage;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public static Authes getInstance() {
        return instance;
    }

    public MailSender getMailSender() {
        return mailSender;
    }
}
