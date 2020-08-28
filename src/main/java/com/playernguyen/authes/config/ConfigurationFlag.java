package com.playernguyen.authes.config;

public enum ConfigurationFlag {

    SERVER_NAME("settings.server-name", "Server Name"),

    MYSQL_HOST("mysql.host", "localhost"),
    MYSQL_USERNAME("mysql.username", "nguyen"),
    MYSQL_PASSWORD("mysql.password", ""),
    MYSQL_DATABASE("mysql.database", "authes"),
    MYSQL_PORT("mysql.port", "3306"),
    MYSQL_TABLE_ACCOUNT("mysql.table.account", "account"),
    MYSQL_PREFIX("mysql.prefix", "authes_"),

    OPTIONAL_BCRYPT_SALT ("settings.bcrypt.salt-round", 7),
    PASSWORD_MIN_SIZE("settings.password-min-size", 6),
    KICK_AFTER_LOGIN("settings.kick-after-login", 15),
    LOGIN_AFTER_REGISTER("settings.login-after-register", true),

    EMAIL_SMTP_AUTH("settings.email.smtp.auth", true),
    EMAIL_SMTP_TLS("settings.email.smtp.tls.enable", true),
    EMAIL_SMTP_HOST("settings.email.smtp.host", "smtp.mailtrap.io"),
    EMAIL_SMTP_PORT("settings.email.smtp.port", 25),
    EMAIL_SMTP_SSL_ENABLE("settings.email.smtp.ssl.enable", false),
    EMAIL_SMTP_USERNAME("settings.email.smtp.auth.username", ""),
    EMAIL_SMTP_PASSWORD("settings.email.smtp.auth.password", ""),
    EMAIL_SEND_FROM_EMAIL("settings.email.send-from", "example@email.com"),
    EMAIL_SEND_FROM_NAME("settings.email.send-from", "Name to send"),
    EMAIL_SEND_SUBJECT("settings.email.subject", "Recovery Code"),
    EMAIL_SEND_TEXT_NON_HTML("settings.email.non-html", "Your email client doesn't support HTML!")

    ;

    private final String path;
    private final Object declare;

    ConfigurationFlag(String path, Object declare) {
        this.path = path;
        this.declare = declare;
    }

    public String getPath() {
        return path;
    }

    public Object getDeclare() {
        return declare;
    }
}
