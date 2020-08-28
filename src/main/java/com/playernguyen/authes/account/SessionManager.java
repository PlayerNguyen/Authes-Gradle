package com.playernguyen.authes.account;

import com.playernguyen.authes.manager.ManagerList;

import java.util.UUID;

public class SessionManager extends ManagerList<Session> {

    /**
     * Linear search to has session or not
     * @param uuid the uuid to search
     * @return Whether has session or not
     */
    public boolean hasSession(UUID uuid) {
        for (Session session : getContainer()) {
            if (session.getUniqueId().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the current session in container
     * @param uuid uuid to find
     * @return null if not found or itself
     */
    public Session getSession(UUID uuid) {
        for (Session session : getContainer()) {
            if (session.getUniqueId().equals(uuid)) {
                return session;
            }
        }
        return null;
    }

    /**
     * Create the new session one when logged in
     * @param uuid uuid of player
     * @return whether created or not
     */
    public boolean createSession(UUID uuid) {
        return getContainer().add(new Session(uuid));
    }

    /**
     * Remove login session
     * @param uuid UUID to remove
     */
    public void removeSession(UUID uuid) {
        Session session = getSession(uuid);
        if (session != null) {
            getContainer().remove(session);
        } else {
            throw new NullPointerException(String.format(
                    "Session not found: %s",
                    uuid.toString()
            ));
        }
    }

}
