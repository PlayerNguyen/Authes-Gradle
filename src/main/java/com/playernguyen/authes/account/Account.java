package com.playernguyen.authes.account;

import java.util.UUID;

public interface Account {

    UUID getUniqueId();

    boolean validate(String password);

    boolean isRegistered();

    void setRegistered(boolean isRegistered);

}
