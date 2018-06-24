package io.realm;

import io.realm.internal.Collection;
import io.realm.internal.InvalidRow;
import io.realm.internal.LinkView;
import io.realm.internal.RealmObjectProxy;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import rx.Observable;

public class RealmList<E extends RealmModel> extends AbstractList<E> implements OrderedRealmCollection<E> {
    private static final String NULL_OBJECTS_NOT_ALLOWED_MESSAGE = "RealmList does not accept null values";
    private static final String ONLY_IN_MANAGED_MODE_MESSAGE = "This method is only available in managed mode";
    public static final String REMOVE_OUTSIDE_TRANSACTION_ERROR = "Objects can only be removed from inside a write transaction";
    protected String className;
    protected Class<E> clazz;
    private final Collection collection;
    protected BaseRealm realm;
    private List<E> unmanagedList;
    final LinkView view;

    private class RealmItr implements Iterator<E> {
        int cursor;
        int expectedModCount;
        int lastRet;

        private RealmItr() {
            this.cursor = 0;
            this.lastRet = -1;
            this.expectedModCount = RealmList.this.modCount;
        }

        public boolean hasNext() {
            RealmList.this.realm.checkIfValid();
            checkConcurrentModification();
            return this.cursor != RealmList.this.size();
        }

        public E next() {
            RealmList.this.realm.checkIfValid();
            checkConcurrentModification();
            int i = this.cursor;
            try {
                E next = RealmList.this.get(i);
                this.lastRet = i;
                this.cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkConcurrentModification();
                throw new NoSuchElementException("Cannot access index " + i + " when size is " + RealmList.this.size() + ". Remember to check hasNext() before using next().");
            }
        }

        public void remove() {
            RealmList.this.realm.checkIfValid();
            if (this.lastRet < 0) {
                throw new IllegalStateException("Cannot call remove() twice. Must call next() in between.");
            }
            checkConcurrentModification();
            try {
                RealmList.this.remove(this.lastRet);
                if (this.lastRet < this.cursor) {
                    this.cursor--;
                }
                this.lastRet = -1;
                this.expectedModCount = RealmList.this.modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkConcurrentModification() {
            if (RealmList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class RealmListItr extends RealmItr implements ListIterator<E> {
        RealmListItr(int index) {
            super();
            if (index < 0 || index > RealmList.this.size()) {
                throw new IndexOutOfBoundsException("Starting location must be a valid index: [0, " + (RealmList.this.size() - 1) + "]. Index was " + index);
            }
            this.cursor = index;
        }

        public boolean hasPrevious() {
            return this.cursor != 0;
        }

        public E previous() {
            checkConcurrentModification();
            int i = this.cursor - 1;
            try {
                E previous = RealmList.this.get(i);
                this.cursor = i;
                this.lastRet = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                checkConcurrentModification();
                throw new NoSuchElementException("Cannot access index less than zero. This was " + i + ". Remember to check hasPrevious() before using previous().");
            }
        }

        public int nextIndex() {
            return this.cursor;
        }

        public int previousIndex() {
            return this.cursor - 1;
        }

        public void set(E e) {
            RealmList.this.realm.checkIfValid();
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            checkConcurrentModification();
            try {
                RealmList.this.set(this.lastRet, (RealmModel) e);
                this.expectedModCount = RealmList.this.modCount;
            } catch (IndexOutOfBoundsException e2) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            RealmList.this.realm.checkIfValid();
            checkConcurrentModification();
            try {
                int i = this.cursor;
                RealmList.this.add(i, (RealmModel) e);
                this.lastRet = -1;
                this.cursor = i + 1;
                this.expectedModCount = RealmList.this.modCount;
            } catch (IndexOutOfBoundsException e2) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public RealmList() {
        this.collection = null;
        this.view = null;
        this.unmanagedList = new ArrayList();
    }

    public RealmList(E... objects) {
        if (objects == null) {
            throw new IllegalArgumentException("The objects argument cannot be null");
        }
        this.collection = null;
        this.view = null;
        this.unmanagedList = new ArrayList(objects.length);
        Collections.addAll(this.unmanagedList, objects);
    }

    RealmList(Class<E> clazz, LinkView linkView, BaseRealm realm) {
        this.collection = new Collection(realm.sharedRealm, linkView, null);
        this.clazz = clazz;
        this.view = linkView;
        this.realm = realm;
    }

    RealmList(String className, LinkView linkView, BaseRealm realm) {
        this.collection = new Collection(realm.sharedRealm, linkView, null);
        this.view = linkView;
        this.realm = realm;
        this.className = className;
    }

    public boolean isValid() {
        if (this.realm == null) {
            return true;
        }
        if (this.realm.isClosed()) {
            return false;
        }
        return isAttached();
    }

    public boolean isManaged() {
        return this.realm != null;
    }

    private boolean isAttached() {
        return this.view != null && this.view.isAttached();
    }

    public void add(int location, E object) {
        checkValidObject(object);
        if (isManaged()) {
            checkValidView();
            if (location < 0 || location > size()) {
                throw new IndexOutOfBoundsException("Invalid index " + location + ", size is " + size());
            }
            this.view.insert((long) location, ((RealmObjectProxy) copyToRealmIfNeeded(object)).realmGet$proxyState().getRow$realm().getIndex());
        } else {
            this.unmanagedList.add(location, object);
        }
        this.modCount++;
    }

    public boolean add(E object) {
        checkValidObject(object);
        if (isManaged()) {
            checkValidView();
            this.view.add(((RealmObjectProxy) copyToRealmIfNeeded(object)).realmGet$proxyState().getRow$realm().getIndex());
        } else {
            this.unmanagedList.add(object);
        }
        this.modCount++;
        return true;
    }

    public E set(int location, E object) {
        checkValidObject(object);
        if (!isManaged()) {
            return (RealmModel) this.unmanagedList.set(location, object);
        }
        checkValidView();
        RealmObjectProxy proxy = (RealmObjectProxy) copyToRealmIfNeeded(object);
        E oldObject = get(location);
        this.view.set((long) location, proxy.realmGet$proxyState().getRow$realm().getIndex());
        return oldObject;
    }

    private E copyToRealmIfNeeded(E object) {
        if (object instanceof RealmObjectProxy) {
            RealmObjectProxy proxy = (RealmObjectProxy) object;
            if (proxy instanceof DynamicRealmObject) {
                String listClassName = this.view.getTargetTable().getClassName();
                if (proxy.realmGet$proxyState().getRealm$realm() == this.realm) {
                    if (listClassName.equals(((DynamicRealmObject) object).getType())) {
                        return object;
                    }
                    throw new IllegalArgumentException(String.format(Locale.US, "The object has a different type from list's. Type of the list is '%s', type of object is '%s'.", new Object[]{listClassName, ((DynamicRealmObject) object).getType()}));
                } else if (this.realm.threadId == proxy.realmGet$proxyState().getRealm$realm().threadId) {
                    throw new IllegalArgumentException("Cannot copy DynamicRealmObject between Realm instances.");
                } else {
                    throw new IllegalStateException("Cannot copy an object to a Realm instance created in another thread.");
                }
            } else if (proxy.realmGet$proxyState().getRow$realm() != null && proxy.realmGet$proxyState().getRealm$realm().getPath().equals(this.realm.getPath())) {
                if (this.realm == proxy.realmGet$proxyState().getRealm$realm()) {
                    return object;
                }
                throw new IllegalArgumentException("Cannot copy an object from another Realm instance.");
            }
        }
        Realm realm = this.realm;
        if (realm.getTable(object.getClass()).hasPrimaryKey()) {
            return realm.copyToRealmOrUpdate((RealmModel) object);
        }
        return realm.copyToRealm((RealmModel) object);
    }

    public void move(int oldPos, int newPos) {
        if (isManaged()) {
            checkValidView();
            this.view.move((long) oldPos, (long) newPos);
            return;
        }
        checkIndex(oldPos);
        checkIndex(newPos);
        RealmModel object = (RealmModel) this.unmanagedList.remove(oldPos);
        if (newPos > oldPos) {
            this.unmanagedList.add(newPos - 1, object);
        } else {
            this.unmanagedList.add(newPos, object);
        }
    }

    public void clear() {
        if (isManaged()) {
            checkValidView();
            this.view.clear();
        } else {
            this.unmanagedList.clear();
        }
        this.modCount++;
    }

    public E remove(int location) {
        E removedItem;
        if (isManaged()) {
            checkValidView();
            removedItem = get(location);
            this.view.remove((long) location);
        } else {
            RealmModel removedItem2 = (RealmModel) this.unmanagedList.remove(location);
        }
        this.modCount++;
        return removedItem;
    }

    public boolean remove(Object object) {
        if (!isManaged() || this.realm.isInTransaction()) {
            return super.remove(object);
        }
        throw new IllegalStateException(REMOVE_OUTSIDE_TRANSACTION_ERROR);
    }

    public boolean removeAll(java.util.Collection<?> collection) {
        if (!isManaged() || this.realm.isInTransaction()) {
            return super.removeAll(collection);
        }
        throw new IllegalStateException(REMOVE_OUTSIDE_TRANSACTION_ERROR);
    }

    public boolean deleteFirstFromRealm() {
        if (!isManaged()) {
            throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
        } else if (size() <= 0) {
            return false;
        } else {
            deleteFromRealm(0);
            this.modCount++;
            return true;
        }
    }

    public boolean deleteLastFromRealm() {
        if (!isManaged()) {
            throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
        } else if (size() <= 0) {
            return false;
        } else {
            deleteFromRealm(size() - 1);
            this.modCount++;
            return true;
        }
    }

    public E get(int location) {
        if (!isManaged()) {
            return (RealmModel) this.unmanagedList.get(location);
        }
        checkValidView();
        return this.realm.get(this.clazz, this.className, this.view.getTargetRowIndex((long) location));
    }

    public E first() {
        return firstImpl(true, null);
    }

    public E first(E defaultValue) {
        return firstImpl(false, defaultValue);
    }

    private E firstImpl(boolean shouldThrow, E defaultValue) {
        if (isManaged()) {
            checkValidView();
            if (!this.view.isEmpty()) {
                return get(0);
            }
        } else if (!(this.unmanagedList == null || this.unmanagedList.isEmpty())) {
            return (RealmModel) this.unmanagedList.get(0);
        }
        if (!shouldThrow) {
            return defaultValue;
        }
        throw new IndexOutOfBoundsException("The list is empty.");
    }

    public E last() {
        return lastImpl(true, null);
    }

    public E last(E defaultValue) {
        return lastImpl(false, defaultValue);
    }

    private E lastImpl(boolean shouldThrow, E defaultValue) {
        if (isManaged()) {
            checkValidView();
            if (!this.view.isEmpty()) {
                return get(((int) this.view.size()) - 1);
            }
        } else if (!(this.unmanagedList == null || this.unmanagedList.isEmpty())) {
            return (RealmModel) this.unmanagedList.get(this.unmanagedList.size() - 1);
        }
        if (!shouldThrow) {
            return defaultValue;
        }
        throw new IndexOutOfBoundsException("The list is empty.");
    }

    public RealmResults<E> sort(String fieldName) {
        return sort(fieldName, Sort.ASCENDING);
    }

    public RealmResults<E> sort(String fieldName, Sort sortOrder) {
        if (isManaged()) {
            return where().findAllSorted(fieldName, sortOrder);
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public RealmResults<E> sort(String fieldName1, Sort sortOrder1, String fieldName2, Sort sortOrder2) {
        return sort(new String[]{fieldName1, fieldName2}, new Sort[]{sortOrder1, sortOrder2});
    }

    public RealmResults<E> sort(String[] fieldNames, Sort[] sortOrders) {
        if (isManaged()) {
            return where().findAllSorted(fieldNames, sortOrders);
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public void deleteFromRealm(int location) {
        if (isManaged()) {
            checkValidView();
            this.view.removeTargetRow(location);
            this.modCount++;
            return;
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public int size() {
        if (!isManaged()) {
            return this.unmanagedList.size();
        }
        checkValidView();
        long size = this.view.size();
        return size < 2147483647L ? (int) size : Integer.MAX_VALUE;
    }

    public RealmQuery<E> where() {
        if (isManaged()) {
            checkValidView();
            return RealmQuery.createQueryFromList(this);
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public Number min(String fieldName) {
        if (isManaged()) {
            return where().min(fieldName);
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public Number max(String fieldName) {
        if (isManaged()) {
            return where().max(fieldName);
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public Number sum(String fieldName) {
        if (isManaged()) {
            return where().sum(fieldName);
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public double average(String fieldName) {
        if (isManaged()) {
            return where().average(fieldName);
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public Date maxDate(String fieldName) {
        if (isManaged()) {
            return where().maximumDate(fieldName);
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public Date minDate(String fieldName) {
        if (isManaged()) {
            return where().minimumDate(fieldName);
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public boolean deleteAllFromRealm() {
        if (isManaged()) {
            checkValidView();
            if (size() <= 0) {
                return false;
            }
            this.view.removeAllTargetRows();
            this.modCount++;
            return true;
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public boolean isLoaded() {
        return true;
    }

    public boolean load() {
        return true;
    }

    public boolean contains(Object object) {
        if (!isManaged()) {
            return this.unmanagedList.contains(object);
        }
        this.realm.checkIfValid();
        if ((object instanceof RealmObjectProxy) && ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm() == InvalidRow.INSTANCE) {
            return false;
        }
        Iterator it = iterator();
        while (it.hasNext()) {
            if (((RealmModel) it.next()).equals(object)) {
                return true;
            }
        }
        return false;
    }

    public Iterator<E> iterator() {
        if (isManaged()) {
            return new RealmItr();
        }
        return super.iterator();
    }

    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    public ListIterator<E> listIterator(int location) {
        if (isManaged()) {
            return new RealmListItr(location);
        }
        return super.listIterator(location);
    }

    private void checkValidObject(E object) {
        if (object == null) {
            throw new IllegalArgumentException(NULL_OBJECTS_NOT_ALLOWED_MESSAGE);
        }
    }

    private void checkIndex(int location) {
        int size = size();
        if (location < 0 || location >= size) {
            throw new IndexOutOfBoundsException("Invalid index " + location + ", size is " + size);
        }
    }

    private void checkValidView() {
        this.realm.checkIfValid();
        if (this.view == null || !this.view.isAttached()) {
            throw new IllegalStateException("Realm instance has been closed or this object or its parent has been deleted.");
        }
    }

    public OrderedRealmCollectionSnapshot<E> createSnapshot() {
        if (isManaged()) {
            checkValidView();
            if (this.className != null) {
                return new OrderedRealmCollectionSnapshot(this.realm, new Collection(this.realm.sharedRealm, this.view, null), this.className);
            }
            return new OrderedRealmCollectionSnapshot(this.realm, new Collection(this.realm.sharedRealm, this.view, null), this.clazz);
        }
        throw new UnsupportedOperationException(ONLY_IN_MANAGED_MODE_MESSAGE);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isManaged() ? this.clazz.getSimpleName() : getClass().getSimpleName());
        sb.append("@[");
        if (!isManaged() || isAttached()) {
            for (int i = 0; i < size(); i++) {
                if (isManaged()) {
                    sb.append(((RealmObjectProxy) get(i)).realmGet$proxyState().getRow$realm().getIndex());
                } else {
                    sb.append(System.identityHashCode(get(i)));
                }
                if (i < size() - 1) {
                    sb.append(',');
                }
            }
        } else {
            sb.append("invalid");
        }
        sb.append("]");
        return sb.toString();
    }

    public Observable<RealmList<E>> asObservable() {
        if (this.realm instanceof Realm) {
            return this.realm.configuration.getRxFactory().from((Realm) this.realm, this);
        }
        if (this.realm instanceof DynamicRealm) {
            return this.realm.configuration.getRxFactory().from(this.realm, this);
        }
        throw new UnsupportedOperationException(this.realm.getClass() + " does not support RxJava.");
    }

    private void checkForAddRemoveListener(Object listener, boolean checkListener) {
        if (checkListener && listener == null) {
            throw new IllegalArgumentException("Listener should not be null");
        }
        this.realm.checkIfValid();
        this.realm.sharedRealm.capabilities.checkCanDeliverNotification("Listeners cannot be used on current thread.");
    }

    public void addChangeListener(OrderedRealmCollectionChangeListener<RealmList<E>> listener) {
        checkForAddRemoveListener(listener, true);
        this.collection.addListener((Object) this, (OrderedRealmCollectionChangeListener) listener);
    }

    public void removeChangeListener(OrderedRealmCollectionChangeListener<RealmList<E>> listener) {
        checkForAddRemoveListener(listener, true);
        this.collection.removeListener((Object) this, (OrderedRealmCollectionChangeListener) listener);
    }

    public void addChangeListener(RealmChangeListener<RealmList<E>> listener) {
        checkForAddRemoveListener(listener, true);
        this.collection.addListener((Object) this, (RealmChangeListener) listener);
    }

    public void removeChangeListener(RealmChangeListener<RealmList<E>> listener) {
        checkForAddRemoveListener(listener, true);
        this.collection.removeListener((Object) this, (RealmChangeListener) listener);
    }

    public void removeAllChangeListeners() {
        checkForAddRemoveListener(null, false);
        this.collection.removeAllListeners();
    }
}
