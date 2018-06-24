package io.realm;

import io.realm.internal.ManagableObject;
import java.util.Collection;
import java.util.Date;

public interface RealmCollection<E extends RealmModel> extends Collection<E>, ManagableObject {
    double average(String str);

    boolean contains(Object obj);

    boolean deleteAllFromRealm();

    boolean isLoaded();

    boolean isManaged();

    boolean isValid();

    boolean load();

    Number max(String str);

    Date maxDate(String str);

    Number min(String str);

    Date minDate(String str);

    Number sum(String str);

    RealmQuery<E> where();
}
