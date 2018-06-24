package com.esotericsoftware.kryo.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class ObjectMap<K, V> {
    private static final int PRIME2 = -1105259343;
    private static final int PRIME3 = -1262997959;
    private static final int PRIME4 = -825114047;
    static Random random = new Random();
    int capacity;
    private int hashShift;
    private boolean isBigTable;
    K[] keyTable;
    private float loadFactor;
    private int mask;
    private int pushIterations;
    public int size;
    private int stashCapacity;
    int stashSize;
    private int threshold;
    V[] valueTable;

    private static class MapIterator<K, V> {
        int currentIndex;
        public boolean hasNext;
        final ObjectMap<K, V> map;
        int nextIndex;

        public MapIterator(ObjectMap<K, V> map) {
            this.map = map;
            reset();
        }

        public void reset() {
            this.currentIndex = -1;
            this.nextIndex = -1;
            advance();
        }

        void advance() {
            this.hasNext = false;
            K[] keyTable = this.map.keyTable;
            int n = this.map.capacity + this.map.stashSize;
            do {
                int i = this.nextIndex + 1;
                this.nextIndex = i;
                if (i >= n) {
                    return;
                }
            } while (keyTable[this.nextIndex] == null);
            this.hasNext = true;
        }

        public void remove() {
            if (this.currentIndex < 0) {
                throw new IllegalStateException("next must be called before remove.");
            }
            if (this.currentIndex >= this.map.capacity) {
                this.map.removeStashIndex(this.currentIndex);
                this.nextIndex = this.currentIndex - 1;
                advance();
            } else {
                this.map.keyTable[this.currentIndex] = null;
                this.map.valueTable[this.currentIndex] = null;
            }
            this.currentIndex = -1;
            ObjectMap objectMap = this.map;
            objectMap.size--;
        }
    }

    public static class Entries<K, V> extends MapIterator<K, V> implements Iterable<Entry<K, V>>, Iterator<Entry<K, V>> {
        Entry<K, V> entry = new Entry();

        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Entries(ObjectMap<K, V> map) {
            super(map);
        }

        public Entry<K, V> next() {
            if (this.hasNext) {
                K[] keyTable = this.map.keyTable;
                this.entry.key = keyTable[this.nextIndex];
                this.entry.value = this.map.valueTable[this.nextIndex];
                this.currentIndex = this.nextIndex;
                advance();
                return this.entry;
            }
            throw new NoSuchElementException();
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public Iterator<Entry<K, V>> iterator() {
            return this;
        }
    }

    public static class Entry<K, V> {
        public K key;
        public V value;

        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    public static class Keys<K> extends MapIterator<K, Object> implements Iterable<K>, Iterator<K> {
        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Keys(ObjectMap<K, ?> map) {
            super(map);
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public K next() {
            if (this.hasNext) {
                K key = this.map.keyTable[this.nextIndex];
                this.currentIndex = this.nextIndex;
                advance();
                return key;
            }
            throw new NoSuchElementException();
        }

        public Iterator<K> iterator() {
            return this;
        }

        public ArrayList<K> toArray() {
            ArrayList array = new ArrayList(this.map.size);
            while (this.hasNext) {
                array.add(next());
            }
            return array;
        }
    }

    public static class Values<V> extends MapIterator<Object, V> implements Iterable<V>, Iterator<V> {
        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Values(ObjectMap<?, V> map) {
            super(map);
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public V next() {
            if (this.hasNext) {
                V value = this.map.valueTable[this.nextIndex];
                this.currentIndex = this.nextIndex;
                advance();
                return value;
            }
            throw new NoSuchElementException();
        }

        public Iterator<V> iterator() {
            return this;
        }

        public ArrayList<V> toArray() {
            ArrayList array = new ArrayList(this.map.size);
            while (this.hasNext) {
                array.add(next());
            }
            return array;
        }

        public void toArray(ArrayList<V> array) {
            while (this.hasNext) {
                array.add(next());
            }
        }
    }

    public ObjectMap() {
        this(32, 0.8f);
    }

    public ObjectMap(int initialCapacity) {
        this(initialCapacity, 0.8f);
    }

    public ObjectMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity);
        } else if (initialCapacity > 1073741824) {
            throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity);
        } else {
            this.capacity = nextPowerOfTwo(initialCapacity);
            if (loadFactor <= 0.0f) {
                throw new IllegalArgumentException("loadFactor must be > 0: " + loadFactor);
            }
            this.loadFactor = loadFactor;
            this.isBigTable = (this.capacity >>> 16) != 0;
            this.threshold = (int) (((float) this.capacity) * loadFactor);
            this.mask = this.capacity - 1;
            this.hashShift = 31 - Integer.numberOfTrailingZeros(this.capacity);
            this.stashCapacity = Math.max(3, ((int) Math.ceil(Math.log((double) this.capacity))) * 2);
            this.pushIterations = Math.max(Math.min(this.capacity, 8), ((int) Math.sqrt((double) this.capacity)) / 8);
            this.keyTable = new Object[(this.capacity + this.stashCapacity)];
            this.valueTable = new Object[this.keyTable.length];
        }
    }

    public ObjectMap(ObjectMap<? extends K, ? extends V> map) {
        this(map.capacity, map.loadFactor);
        this.stashSize = map.stashSize;
        System.arraycopy(map.keyTable, 0, this.keyTable, 0, map.keyTable.length);
        System.arraycopy(map.valueTable, 0, this.valueTable, 0, map.valueTable.length);
        this.size = map.size;
    }

    public V put(K key, V value) {
        if (key != null) {
            return put_internal(key, value);
        }
        throw new IllegalArgumentException("key cannot be null.");
    }

    private V put_internal(K key, V value) {
        K[] keyTable = this.keyTable;
        int mask = this.mask;
        boolean isBigTable = this.isBigTable;
        int hashCode = key.hashCode();
        int index1 = hashCode & mask;
        K key1 = keyTable[index1];
        if (key.equals(key1)) {
            V oldValue = this.valueTable[index1];
            this.valueTable[index1] = value;
            return oldValue;
        }
        int index2 = hash2(hashCode);
        K key2 = keyTable[index2];
        if (key.equals(key2)) {
            oldValue = this.valueTable[index2];
            this.valueTable[index2] = value;
            return oldValue;
        }
        int index3 = hash3(hashCode);
        K key3 = keyTable[index3];
        if (key.equals(key3)) {
            oldValue = this.valueTable[index3];
            this.valueTable[index3] = value;
            return oldValue;
        }
        int index4 = -1;
        Object key4 = null;
        if (isBigTable) {
            index4 = hash4(hashCode);
            key4 = keyTable[index4];
            if (key.equals(key4)) {
                oldValue = this.valueTable[index4];
                this.valueTable[index4] = value;
                return oldValue;
            }
        }
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (key.equals(keyTable[i])) {
                oldValue = this.valueTable[i];
                this.valueTable[i] = value;
                return oldValue;
            }
            i++;
        }
        int i2;
        if (key1 == null) {
            keyTable[index1] = key;
            this.valueTable[index1] = value;
            i2 = this.size;
            this.size = i2 + 1;
            if (i2 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        } else if (key2 == null) {
            keyTable[index2] = key;
            this.valueTable[index2] = value;
            i2 = this.size;
            this.size = i2 + 1;
            if (i2 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        } else if (key3 == null) {
            keyTable[index3] = key;
            this.valueTable[index3] = value;
            i2 = this.size;
            this.size = i2 + 1;
            if (i2 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        } else if (isBigTable && key4 == null) {
            keyTable[index4] = key;
            this.valueTable[index4] = value;
            i2 = this.size;
            this.size = i2 + 1;
            if (i2 >= this.threshold) {
                resize(this.capacity << 1);
            }
            return null;
        } else {
            push(key, value, index1, key1, index2, key2, index3, key3, index4, key4);
            return null;
        }
    }

    public void putAll(ObjectMap<K, V> map) {
        ensureCapacity(map.size);
        Iterator it = map.entries().iterator();
        while (it.hasNext()) {
            Entry<K, V> entry = (Entry) it.next();
            put(entry.key, entry.value);
        }
    }

    private void putResize(K key, V value) {
        int hashCode = key.hashCode();
        int index1 = hashCode & this.mask;
        K key1 = this.keyTable[index1];
        int i;
        if (key1 == null) {
            this.keyTable[index1] = key;
            this.valueTable[index1] = value;
            i = this.size;
            this.size = i + 1;
            if (i >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int index2 = hash2(hashCode);
        K key2 = this.keyTable[index2];
        if (key2 == null) {
            this.keyTable[index2] = key;
            this.valueTable[index2] = value;
            i = this.size;
            this.size = i + 1;
            if (i >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int index3 = hash3(hashCode);
        K key3 = this.keyTable[index3];
        if (key3 == null) {
            this.keyTable[index3] = key;
            this.valueTable[index3] = value;
            i = this.size;
            this.size = i + 1;
            if (i >= this.threshold) {
                resize(this.capacity << 1);
                return;
            }
            return;
        }
        int index4 = -1;
        Object key4 = null;
        if (this.isBigTable) {
            index4 = hash4(hashCode);
            key4 = this.keyTable[index4];
            if (key4 == null) {
                this.keyTable[index4] = key;
                this.valueTable[index4] = value;
                i = this.size;
                this.size = i + 1;
                if (i >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
        }
        push(key, value, index1, key1, index2, key2, index3, key3, index4, key4);
    }

    private void push(K insertKey, V insertValue, int index1, K key1, int index2, K key2, int index3, K key3, int index4, K key4) {
        K[] keyTable = this.keyTable;
        V[] valueTable = this.valueTable;
        int mask = this.mask;
        boolean isBigTable = this.isBigTable;
        int i = 0;
        int pushIterations = this.pushIterations;
        int n = isBigTable ? 4 : 3;
        while (true) {
            K evictedKey;
            V evictedValue;
            switch (random.nextInt(n)) {
                case 0:
                    evictedKey = key1;
                    evictedValue = valueTable[index1];
                    keyTable[index1] = insertKey;
                    valueTable[index1] = insertValue;
                    break;
                case 1:
                    evictedKey = key2;
                    evictedValue = valueTable[index2];
                    keyTable[index2] = insertKey;
                    valueTable[index2] = insertValue;
                    break;
                case 2:
                    evictedKey = key3;
                    evictedValue = valueTable[index3];
                    keyTable[index3] = insertKey;
                    valueTable[index3] = insertValue;
                    break;
                default:
                    evictedKey = key4;
                    evictedValue = valueTable[index4];
                    keyTable[index4] = insertKey;
                    valueTable[index4] = insertValue;
                    break;
            }
            int hashCode = evictedKey.hashCode();
            index1 = hashCode & mask;
            key1 = keyTable[index1];
            int i2;
            if (key1 == null) {
                keyTable[index1] = evictedKey;
                valueTable[index1] = evictedValue;
                i2 = this.size;
                this.size = i2 + 1;
                if (i2 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            index2 = hash2(hashCode);
            key2 = keyTable[index2];
            if (key2 == null) {
                keyTable[index2] = evictedKey;
                valueTable[index2] = evictedValue;
                i2 = this.size;
                this.size = i2 + 1;
                if (i2 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            index3 = hash3(hashCode);
            key3 = keyTable[index3];
            if (key3 == null) {
                keyTable[index3] = evictedKey;
                valueTable[index3] = evictedValue;
                i2 = this.size;
                this.size = i2 + 1;
                if (i2 >= this.threshold) {
                    resize(this.capacity << 1);
                    return;
                }
                return;
            }
            if (isBigTable) {
                index4 = hash4(hashCode);
                key4 = keyTable[index4];
                if (key4 == null) {
                    keyTable[index4] = evictedKey;
                    valueTable[index4] = evictedValue;
                    i2 = this.size;
                    this.size = i2 + 1;
                    if (i2 >= this.threshold) {
                        resize(this.capacity << 1);
                        return;
                    }
                    return;
                }
            }
            i++;
            if (i == pushIterations) {
                putStash(evictedKey, evictedValue);
                return;
            } else {
                insertKey = evictedKey;
                insertValue = evictedValue;
            }
        }
    }

    private void putStash(K key, V value) {
        if (this.stashSize == this.stashCapacity) {
            resize(this.capacity << 1);
            put_internal(key, value);
            return;
        }
        int index = this.capacity + this.stashSize;
        this.keyTable[index] = key;
        this.valueTable[index] = value;
        this.stashSize++;
        this.size++;
    }

    public V get(K key) {
        int hashCode = key.hashCode();
        int index = hashCode & this.mask;
        if (!key.equals(this.keyTable[index])) {
            index = hash2(hashCode);
            if (!key.equals(this.keyTable[index])) {
                index = hash3(hashCode);
                if (!key.equals(this.keyTable[index])) {
                    if (!this.isBigTable) {
                        return getStash(key);
                    }
                    index = hash4(hashCode);
                    if (!key.equals(this.keyTable[index])) {
                        return getStash(key);
                    }
                }
            }
        }
        return this.valueTable[index];
    }

    private V getStash(K key) {
        K[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (key.equals(keyTable[i])) {
                return this.valueTable[i];
            }
            i++;
        }
        return null;
    }

    public V get(K key, V defaultValue) {
        int hashCode = key.hashCode();
        int index = hashCode & this.mask;
        if (!key.equals(this.keyTable[index])) {
            index = hash2(hashCode);
            if (!key.equals(this.keyTable[index])) {
                index = hash3(hashCode);
                if (!key.equals(this.keyTable[index])) {
                    if (!this.isBigTable) {
                        return getStash(key, defaultValue);
                    }
                    index = hash4(hashCode);
                    if (!key.equals(this.keyTable[index])) {
                        return getStash(key, defaultValue);
                    }
                }
            }
        }
        return this.valueTable[index];
    }

    private V getStash(K key, V defaultValue) {
        K[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (key.equals(keyTable[i])) {
                return this.valueTable[i];
            }
            i++;
        }
        return defaultValue;
    }

    public V remove(K key) {
        int hashCode = key.hashCode();
        int index = hashCode & this.mask;
        if (key.equals(this.keyTable[index])) {
            this.keyTable[index] = null;
            V oldValue = this.valueTable[index];
            this.valueTable[index] = null;
            this.size--;
            return oldValue;
        }
        index = hash2(hashCode);
        if (key.equals(this.keyTable[index])) {
            this.keyTable[index] = null;
            oldValue = this.valueTable[index];
            this.valueTable[index] = null;
            this.size--;
            return oldValue;
        }
        index = hash3(hashCode);
        if (key.equals(this.keyTable[index])) {
            this.keyTable[index] = null;
            oldValue = this.valueTable[index];
            this.valueTable[index] = null;
            this.size--;
            return oldValue;
        }
        if (this.isBigTable) {
            index = hash4(hashCode);
            if (key.equals(this.keyTable[index])) {
                this.keyTable[index] = null;
                oldValue = this.valueTable[index];
                this.valueTable[index] = null;
                this.size--;
                return oldValue;
            }
        }
        return removeStash(key);
    }

    V removeStash(K key) {
        K[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (key.equals(keyTable[i])) {
                V oldValue = this.valueTable[i];
                removeStashIndex(i);
                this.size--;
                return oldValue;
            }
            i++;
        }
        return null;
    }

    void removeStashIndex(int index) {
        this.stashSize--;
        int lastIndex = this.capacity + this.stashSize;
        if (index < lastIndex) {
            this.keyTable[index] = this.keyTable[lastIndex];
            this.valueTable[index] = this.valueTable[lastIndex];
            this.valueTable[lastIndex] = null;
            return;
        }
        this.valueTable[index] = null;
    }

    public void shrink(int maximumCapacity) {
        if (maximumCapacity < 0) {
            throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity);
        }
        if (this.size > maximumCapacity) {
            maximumCapacity = this.size;
        }
        if (this.capacity > maximumCapacity) {
            resize(nextPowerOfTwo(maximumCapacity));
        }
    }

    public void clear(int maximumCapacity) {
        if (this.capacity <= maximumCapacity) {
            clear();
            return;
        }
        this.size = 0;
        resize(maximumCapacity);
    }

    public void clear() {
        K[] keyTable = this.keyTable;
        V[] valueTable = this.valueTable;
        int i = this.capacity + this.stashSize;
        while (true) {
            int i2 = i - 1;
            if (i > 0) {
                keyTable[i2] = null;
                valueTable[i2] = null;
                i = i2;
            } else {
                this.size = 0;
                this.stashSize = 0;
                return;
            }
        }
    }

    public boolean containsValue(Object value, boolean identity) {
        V[] valueTable = this.valueTable;
        int i;
        int i2;
        if (value != null) {
            if (!identity) {
                i = this.capacity + this.stashSize;
                while (true) {
                    i2 = i - 1;
                    if (i <= 0) {
                        break;
                    } else if (value.equals(valueTable[i2])) {
                        return true;
                    } else {
                        i = i2;
                    }
                }
            } else {
                i = this.capacity + this.stashSize;
                while (true) {
                    i2 = i - 1;
                    if (i <= 0) {
                        break;
                    } else if (valueTable[i2] == value) {
                        return true;
                    } else {
                        i = i2;
                    }
                }
            }
        } else {
            K[] keyTable = this.keyTable;
            i = this.capacity + this.stashSize;
            while (true) {
                i2 = i - 1;
                if (i <= 0) {
                    break;
                } else if (keyTable[i2] != null && valueTable[i2] == null) {
                    return true;
                } else {
                    i = i2;
                }
            }
        }
        return false;
    }

    public boolean containsKey(K key) {
        int hashCode = key.hashCode();
        if (!key.equals(this.keyTable[hashCode & this.mask])) {
            if (!key.equals(this.keyTable[hash2(hashCode)])) {
                if (!key.equals(this.keyTable[hash3(hashCode)])) {
                    if (!this.isBigTable) {
                        return containsKeyStash(key);
                    }
                    if (!key.equals(this.keyTable[hash4(hashCode)])) {
                        return containsKeyStash(key);
                    }
                }
            }
        }
        return true;
    }

    private boolean containsKeyStash(K key) {
        K[] keyTable = this.keyTable;
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (key.equals(keyTable[i])) {
                return true;
            }
            i++;
        }
        return false;
    }

    public K findKey(Object value, boolean identity) {
        V[] valueTable = this.valueTable;
        int i;
        int i2;
        if (value != null) {
            if (!identity) {
                i = this.capacity + this.stashSize;
                while (true) {
                    i2 = i - 1;
                    if (i <= 0) {
                        break;
                    } else if (value.equals(valueTable[i2])) {
                        return this.keyTable[i2];
                    } else {
                        i = i2;
                    }
                }
            } else {
                i = this.capacity + this.stashSize;
                while (true) {
                    i2 = i - 1;
                    if (i <= 0) {
                        break;
                    } else if (valueTable[i2] == value) {
                        return this.keyTable[i2];
                    } else {
                        i = i2;
                    }
                }
            }
        } else {
            K[] keyTable = this.keyTable;
            i = this.capacity + this.stashSize;
            while (true) {
                i2 = i - 1;
                if (i <= 0) {
                    break;
                } else if (keyTable[i2] != null && valueTable[i2] == null) {
                    return keyTable[i2];
                } else {
                    i = i2;
                }
            }
        }
        return null;
    }

    public void ensureCapacity(int additionalCapacity) {
        int sizeNeeded = this.size + additionalCapacity;
        if (sizeNeeded >= this.threshold) {
            resize(nextPowerOfTwo((int) (((float) sizeNeeded) / this.loadFactor)));
        }
    }

    private void resize(int newSize) {
        boolean z;
        int oldEndIndex = this.capacity + this.stashSize;
        this.capacity = newSize;
        this.threshold = (int) (((float) newSize) * this.loadFactor);
        this.mask = newSize - 1;
        this.hashShift = 31 - Integer.numberOfTrailingZeros(newSize);
        this.stashCapacity = Math.max(3, ((int) Math.ceil(Math.log((double) newSize))) * 2);
        this.pushIterations = Math.max(Math.min(newSize, 8), ((int) Math.sqrt((double) newSize)) / 8);
        if ((this.capacity >>> 16) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isBigTable = z;
        K[] oldKeyTable = this.keyTable;
        V[] oldValueTable = this.valueTable;
        this.keyTable = new Object[(this.stashCapacity + newSize)];
        this.valueTable = new Object[(this.stashCapacity + newSize)];
        int oldSize = this.size;
        this.size = 0;
        this.stashSize = 0;
        if (oldSize > 0) {
            for (int i = 0; i < oldEndIndex; i++) {
                K key = oldKeyTable[i];
                if (key != null) {
                    putResize(key, oldValueTable[i]);
                }
            }
        }
    }

    private int hash2(int h) {
        h *= PRIME2;
        return ((h >>> this.hashShift) ^ h) & this.mask;
    }

    private int hash3(int h) {
        h *= PRIME3;
        return ((h >>> this.hashShift) ^ h) & this.mask;
    }

    private int hash4(int h) {
        h *= PRIME4;
        return ((h >>> this.hashShift) ^ h) & this.mask;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String toString() {
        /*
        r8 = this;
        r7 = 61;
        r6 = r8.size;
        if (r6 != 0) goto L_0x000a;
    L_0x0006:
        r6 = "{}";
    L_0x0009:
        return r6;
    L_0x000a:
        r0 = new java.lang.StringBuilder;
        r6 = 32;
        r0.<init>(r6);
        r6 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r0.append(r6);
        r4 = r8.keyTable;
        r5 = r8.valueTable;
        r1 = r4.length;
        r2 = r1;
    L_0x001c:
        r1 = r2 + -1;
        if (r2 <= 0) goto L_0x0058;
    L_0x0020:
        r3 = r4[r1];
        if (r3 != 0) goto L_0x0026;
    L_0x0024:
        r2 = r1;
        goto L_0x001c;
    L_0x0026:
        r0.append(r3);
        r0.append(r7);
        r6 = r5[r1];
        r0.append(r6);
        r2 = r1;
    L_0x0032:
        r1 = r2 + -1;
        if (r2 <= 0) goto L_0x004e;
    L_0x0036:
        r3 = r4[r1];
        if (r3 != 0) goto L_0x003c;
    L_0x003a:
        r2 = r1;
        goto L_0x0032;
    L_0x003c:
        r6 = ", ";
        r0.append(r6);
        r0.append(r3);
        r0.append(r7);
        r6 = r5[r1];
        r0.append(r6);
        r2 = r1;
        goto L_0x0032;
    L_0x004e:
        r6 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r0.append(r6);
        r6 = r0.toString();
        goto L_0x0009;
    L_0x0058:
        r2 = r1;
        goto L_0x0032;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.kryo.util.ObjectMap.toString():java.lang.String");
    }

    public Entries<K, V> entries() {
        return new Entries(this);
    }

    public Values<V> values() {
        return new Values(this);
    }

    public Keys<K> keys() {
        return new Keys(this);
    }

    public static int nextPowerOfTwo(int value) {
        if (value == 0) {
            return 1;
        }
        value--;
        value |= value >> 1;
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        return (value | (value >> 16)) + 1;
    }
}
