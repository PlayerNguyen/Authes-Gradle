package com.playernguyen.authes.account;

import java.util.UUID;

public class Session {

    private final UUID uniqueId;

    public Session(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }


}
