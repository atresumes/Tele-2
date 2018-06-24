package io.realm;

public interface RealmObjectChangeListener<T extends RealmModel> {
    void onChange(T t, ObjectChangeSet objectChangeSet);
}
