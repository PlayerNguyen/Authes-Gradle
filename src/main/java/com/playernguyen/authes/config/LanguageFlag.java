package com.playernguyen.authes.config;

public enum LanguageFlag {

    PREFIX ("prefix", "&7[&cAuthes&7]"),
    REQUIRE_REGISTER("require-register", "&cPlease register by using /register <password> <confirmPassword>"),
    REQUIRE_LOGIN("require-login", "&cPlease login by using /login <password>"),
    KICK_REASON("kick-reason", "&cYou has been kicked because not register/login"),
    NO_PERMISSION_COMMAND("no-permission-command", "&cYou has no permission to do this"),
    ALREADY_REGISTERED("already-registered", "&cYou are ready registered!"),
    MISSING_REGISTER_COMMAND("missing-register-command", "&cMissing arguments: &6/register <password> <confirmPassword>"),
    PASSWORD_NOT_MATCH("password-not-match", "&cPassword doesn't match with confirm password"),
    REGISTER_SUCCESS("register-success", "&aRegister success."),
    REGISTER_FAILED("register-failed", "&cFailed to register."),
    INVALID_SENDER("invalid-sender", "&cYou cannot use this command on console/remote!"),
    NOT_REGISTER("not-register", "&cYou aren't register yet. Using &6/register <password> <confirmPassword>"),
    MISSING_LOGIN_COMMAND("missing-login-command", "&cMissing arguments: &6/login <password>"),
    LOGIN_SUCCESS("login-success", "&aLogin success"),
    LOGIN_FAIL("login-fail", "&cLogin fail!"),
    WRONG_PASSWORD("wrong-password", "&cWrong password. Please try again!"),
    PASSWORD_TOO_SHORT("password-too-short", "&cYour password is too short, please try again with logger password"),
    INVALID_EMAIL_FORMAT("invalid-email-format", "&cInvalid email format, please try again"),
    CHANGE_EMAIL_FAIL("change-email-fail", "&cFailed to change email, contact to admin for more"),
    CHANGE_EMAIL_SUCCESS("change-email-success", "&aSucceeded to change email," +
            " /fogotpassword will use your current email"),
    ALREADY_LOGGED_IN("already-logged-in", "&cYou are logged in, don't need to re-login."),
    CHANGE_PASSWORD_FAIL("change-password-fail", "&cFailed to change password, please content to admin."),
    CHANGE_PASSWORD_SUCCESS("change-password-success", "&aSucceeded to change password"),
    RECOVERY_PASSWORD_SENT("recovery-password-sent", "&6&bRecovery password has sent to your mail!"),
    RECOVERY_CODE_INVALID("recovery-code-invalid", "&cRecovery code's invalid!"),
    COMPONENT_REGISTER_TEXT("components-register-text", "your password"),
    COMPONENT_LOGIN_TEXT("components-login-text", "your password"),
    COMPONENT_RECONFIRM_REGISTER_TEXT("components-reconfirm-register-text", "confirm password"),
    COMPONENT_WRONG_PASSWORD("components-wrong-password", "wrong password"),
    ;

    private final String path;
    private final Object declare;

    LanguageFlag(String path, Object declare) {
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
