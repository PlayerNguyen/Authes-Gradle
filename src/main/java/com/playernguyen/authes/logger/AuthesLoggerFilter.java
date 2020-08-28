package com.playernguyen.authes.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class AuthesLoggerFilter extends AbstractFilter {

    public void registerFilter() {
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(this);
    }

    @Override
    public Result filter(LogEvent event) {
        if (event == null) {
            return Result.NEUTRAL;
        }

        return validateMessage(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return validateMessage(msg.getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        if (msg == null) {
            return Result.NEUTRAL;
        }

        return validateMessage(msg.toString());
    }


    private Result validateMessage(String message) {
        if (message == null) {
            return Result.NEUTRAL;
        }

        boolean shouldBeOmitted = false; // Implement

        if (message.contains(": /login")
                || message.contains(": /l")
                || message.contains(": /register")
                || message.contains(": /reg")
                || message.contains(": /r")
                || message.contains(": /changeemail")
                || message.contains(": /ce")
                || message.contains(": /changepassword")
                || message.contains(": /recoverypassword")
                || message.contains(": /rpass")
        ) {
            shouldBeOmitted = true;
        }

        return shouldBeOmitted ? Result.DENY : Result.NEUTRAL;
    }

}
