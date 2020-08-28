package com.playernguyen.authes.account;

import com.playernguyen.authes.AuthesInstance;
import com.playernguyen.authes.config.ConfigurationFlag;
import com.playernguyen.authes.util.NumberGenerator;
import com.playernguyen.authes.util.ResultFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class SQLAccountManager extends AuthesInstance {

    private final String tableName;

    public SQLAccountManager() {
        this.tableName = getConfiguration().getString(ConfigurationFlag.MYSQL_PREFIX) +
                getConfiguration().getString(ConfigurationFlag.MYSQL_TABLE_ACCOUNT);
    }

    /**
     * Check that player found or not
     * @param uuid The uuid of player
     * @return Whether player are existed or not
     */
    public boolean hasPlayer(UUID uuid) {
        try (Connection connection = getEstablishment().openConnection()) {
            // Prepare statement
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(
                    "SELECT * FROM `%s` WHERE `uuid`=?",
                    tableName
            ));
            // Parameter define
            preparedStatement.setString(1, uuid.toString());
            // Execute query
            ResultSet resultSet = preparedStatement.executeQuery();
            // Fetch
            return new ResultFetcher(resultSet).size() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Account getAccount(UUID uuid) {
        try (Connection connection = getEstablishment().openConnection()) {
            // New member
            if (!hasPlayer(uuid)) {
                return new UserAccount(uuid, false);
            }
            // The old one
            PreparedStatement statement = connection.prepareStatement(String.format(
                    "SELECT * FROM `%s` WHERE `uuid`=?",
                    tableName
            ));
            // Parameter set
            statement.setString(1, uuid.toString());
            // Fire in a hole & get hash
            ResultSet resultSet = statement.executeQuery();
            String hash = (String) new ResultFetcher(resultSet).first().get("hash");
            return new UserAccount(uuid, true, hash);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(Player player, UUID uuid, String plaintext) {
        try (Connection connection = getEstablishment().openConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    String.format("INSERT INTO `%s` (`username`, `uuid`, `hash`, `address`, `lastLogin`, `recoveryKey`) " +
                            "VALUES (?,?,?,?,?,?)", tableName)
            );
            // Handle information
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            String hash = BCrypt.hashpw(plaintext, BCrypt.gensalt(getConfiguration()
                    .getInt(ConfigurationFlag.OPTIONAL_BCRYPT_SALT)));
            String address = Objects.requireNonNull(player.getAddress()).getAddress().toString();
            String lastLogged = String.valueOf(System.currentTimeMillis());
            // Set parameters
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.setString(3, hash);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, lastLogged);
            preparedStatement.setString(6, NumberGenerator.generate(6));
            // Execute
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean login(UUID uuid, String plaintext) {
        try (Connection connection = getEstablishment().openConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(
                    "SELECT * FROM `%s` WHERE `uuid`=?",
                    tableName
            ));
            // Set the parameter
            preparedStatement.setString(1, uuid.toString());
            ResultFetcher resultFetcher = new ResultFetcher(preparedStatement.executeQuery());
            // Handle hash check
            String hash = (String) resultFetcher.first().get("hash");

            return BCrypt.checkpw(plaintext, hash);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changeEmail(UUID uuid, String email) {
        try (Connection connection = getEstablishment().openConnection()) {
            // Prepare statement
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(
                    "UPDATE `%s` SET `email`=? WHERE `uuid`=?",
                    tableName
            ));
            // Parameters set
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, uuid.toString());
            // Execute
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setLogged(Player player, UUID uuid, boolean b) {
        try (Connection connection = getEstablishment().openConnection()) {
            // Prepare statement
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(
                    "UPDATE `%s` SET `isLogged`=?, `lastLogin`=?, `address`=? WHERE `uuid`=?",
                    tableName
            ));
            // Parameters
            preparedStatement.setInt(1, (b) ? 1 : 0);
            preparedStatement.setString(2, String.valueOf(System.currentTimeMillis()));
            preparedStatement.setString(3, Objects.requireNonNull(player.getAddress()).toString());
            preparedStatement.setString(4, uuid.toString());
            // Update
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePassword(UUID uuid, String plaintext) {
        try (Connection connection = getEstablishment().openConnection()) {
            // Prepare
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(
                    "UPDATE `%s` SET `hash`=? WHERE `uuid`=?",
                    tableName
            ));
            // Parameters
            String hash = BCrypt.hashpw(plaintext, BCrypt.gensalt(getConfiguration()
                    .getInt(ConfigurationFlag.OPTIONAL_BCRYPT_SALT)));
            preparedStatement.setString(1, hash);
            preparedStatement.setString(2, uuid.toString());
            // Execute and return
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getRecoveryKey(UUID uuid) {
        try (Connection connection = getEstablishment().openConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(
                    "SELECT * FROM `%s` WHERE `uuid`=?",
                    tableName
            ));
            // Set the parameter
            preparedStatement.setString(1, uuid.toString());
            ResultFetcher resultFetcher = new ResultFetcher(preparedStatement.executeQuery());
            // Return
            return (String) resultFetcher.first().get("recoveryKey");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validRecoveryKey(UUID uuid, String recoveryKey) {
        return getRecoveryKey(uuid).equalsIgnoreCase(recoveryKey);
    }

    public String getEmail(UUID uuid) {
        try (Connection connection = getEstablishment().openConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(
                    "SELECT * FROM `%s` WHERE `uuid`=?",
                    tableName
            ));
            // Set the parameter
            preparedStatement.setString(1, uuid.toString());
            ResultFetcher resultFetcher = new ResultFetcher(preparedStatement.executeQuery());
            // Return
            return (String) resultFetcher.first().get("email");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean regenerateRecoveryKey(UUID uuid) {
        try (Connection connection = getEstablishment().openConnection()) {
            // Prepare
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(
                    "UPDATE `%s` SET `recoveryKey`=? WHERE `uuid`=?",
                    tableName
            ));
            // Parameters
            preparedStatement.setString(1, NumberGenerator.generate(6));
            preparedStatement.setString(2, uuid.toString());
            // Execute and return
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
