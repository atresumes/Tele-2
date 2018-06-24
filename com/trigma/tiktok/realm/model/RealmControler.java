package com.trigma.tiktok.realm.model;

import io.realm.Realm;
import io.realm.RealmAsyncTask;

public class RealmControler {
    public static Realm realm;
    public static RealmAsyncTask realmB;

    public static Realm getRealm() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }
}
