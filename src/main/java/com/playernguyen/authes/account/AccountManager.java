package com.playernguyen.authes.account;

import com.playernguyen.authes.manager.ManagerList;

import java.util.UUID;

public class AccountManager extends ManagerList<Account> {

    public Account getAccountFromUUID (UUID uuid) {
        for (Account account : getContainer()) {
            if (account.getUniqueId().equals(uuid)) return account;
        }
        return null;
    }

}
