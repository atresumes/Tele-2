package com.esotericsoftware.kryo.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IdentityMap<K, V> {
    private static final int PRIME2 = -1105259343;
    private static final int PRIME3 = -1262997959;
    private static final int PRIME4 = -825114047;
    int capacity;
    private Entries entries;
    private int hashShift;
    private boolean isBigTable;
    K[] keyTable;
    private Keys keys;
    private float loadFactor;
    private int mask;
    private int pushIterations;
    public int size;
    private int stashCapacity;
    int stashSize;
    private int threshold;
    V[] valueTable;
    private Values values;

    private static class MapIterator<K, V> {
        int currentIndex;
        public boolean hasNext;
        final IdentityMap<K, V> map;
        int nextIndex;

        public MapIterator(IdentityMap<K, V> map) {
            this.map = map;
            reset();
        }

        public void reset() {
            this.currentIndex = -1;
            this.nextIndex = -1;
            findNextIndex();
        }

        void findNextIndex() {
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
                findNextIndex();
            } else {
                this.map.keyTable[this.currentIndex] = null;
                this.map.valueTable[this.currentIndex] = null;
            }
            this.currentIndex = -1;
            IdentityMap identityMap = this.map;
            identityMap.size--;
        }
    }

    public static class Entries<K, V> extends MapIterator<K, V> implements Iterable<Entry<K, V>>, Iterator<Entry<K, V>> {
        private Entry<K, V> entry = new Entry();

        public /* bridge */ /* synthetic */ void remove() {
            super.remove();
        }

        public /* bridge */ /* synthetic */ void reset() {
            super.reset();
        }

        public Entries(IdentityMap<K, V> map) {
            super(map);
        }

        public Entry<K, V> next() {
            if (this.hasNext) {
                K[] keyTable = this.map.keyTable;
                this.entry.key = keyTable[this.nextIndex];
                this.entry.value = this.map.valueTable[this.nextIndex];
                this.currentIndex = this.nextIndex;
                findNextIndex();
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

        public Keys(IdentityMap<K, ?> map) {
            super(map);
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public K next() {
            K key = this.map.keyTable[this.nextIndex];
            this.currentIndex = this.nextIndex;
            findNextIndex();
            return key;
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

        public Values(IdentityMap<?, V> map) {
            super(map);
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public V next() {
            V value = this.map.valueTable[this.nextIndex];
            this.currentIndex = this.nextIndex;
            findNextIndex();
            return value;
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

    public IdentityMap() {
        this(32, 0.8f);
    }

    public IdentityMap(int initialCapacity) {
        this(initialCapacity, 0.8f);
    }

    public IdentityMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity must be >= 0: " + initialCapacity);
        } else if (this.capacity > 1073741824) {
            throw new IllegalArgumentException("initialCapacity is too large: " + initialCapacity);
        } else {
            this.capacity = ObjectMap.nextPowerOfTwo(initialCapacity);
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

    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null.");
        }
        K[] keyTable = this.keyTable;
        int mask = this.mask;
        boolean isBigTable = this.isBigTable;
        int hashCode = System.identityHashCode(key);
        int index1 = hashCode & mask;
        K key1 = keyTable[index1];
        if (key1 == key) {
            V oldValue = this.valueTable[index1];
            this.valueTable[index1] = value;
            return oldValue;
        }
        int index2 = hash2(hashCode);
        K key2 = keyTable[index2];
        if (key2 == key) {
            oldValue = this.valueTable[index2];
            this.valueTable[index2] = value;
            return oldValue;
        }
        int index3 = hash3(hashCode);
        K key3 = keyTable[index3];
        if (key3 == key) {
            oldValue = this.valueTable[index3];
            this.valueTable[index3] = value;
            return oldValue;
        }
        int index4 = -1;
        Object key4 = null;
        if (isBigTable) {
            index4 = hash4(hashCode);
            key4 = keyTable[index4];
            if (key4 == key) {
                oldValue = this.valueTable[index4];
                this.valueTable[index4] = value;
                return oldValue;
            }
        }
        int i = this.capacity;
        int n = i + this.stashSize;
        while (i < n) {
            if (keyTable[i] == key) {
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

    private void putResize(K key, V value) {
        int hashCode = System.identityHashCode(key);
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
            switch (ObjectMap.random.nextInt(n)) {
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
            int hashCode = System.identityHashCode(evictedKey);
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
            put(key, value);
            return;
        }
        int index = this.capacity + this.stashSize;
        this.keyTable[index] = key;
        this.valueTable[index] = value;
        this.stashSize++;
        this.size++;
    }

    public V get(K key) {
        int hashCode = System.identityHashCode(key);
        int index = hashCode & this.mask;
        if (key != this.keyTable[index]) {
            index = hash2(hashCode);
            if (key != this.keyTable[index]) {
                index = hash3(hashCode);
                if (key != this.keyTable[index]) {
                    if (!this.isBigTable) {
                        return getStash(key, null);
                    }
                    index = hash4(hashCode);
                    if (key != this.keyTable[index]) {
                        return getStash(key, null);
                    }
                }
            }
        }
        return this.valueTable[index];
    }

    public V get(K key, V defaultValue) {
        int hashCode = System.identityHashCode(key);
        int index = hashCode & this.mask;
        if (key != this.keyTable[index]) {
            index = hash2(hashCode);
            if (key != this.keyTable[index]) {
                index = hash3(hashCode);
                if (key != this.keyTable[index]) {
                    if (!this.isBigTable) {
                        return getStash(key, defaultValue);
                    }
                    index = hash4(hashCode);
                    if (key != this.keyTable[index]) {
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
            if (keyTable[i] == key) {
                return this.valueTable[i];
            }
            i++;
        }
        return defaultValue;
    }

    public V remove(K key) {
        int hashCode = System.identityHashCode(key);
        int index = hashCode & this.mask;
        if (this.keyTable[index] == key) {
            this.keyTable[index] = null;
            V oldValue = this.valueTable[index];
            this.valueTable[index] = null;
            this.size--;
            return oldValue;
        }
        index = hash2(hashCode);
        if (this.keyTable[index] == key) {
            this.keyTable[index] = null;
            oldValue = this.valueTable[index];
            this.valueTable[index] = null;
            this.size--;
            return oldValue;
        }
        index = hash3(hashCode);
        if (this.keyTable[index] == key) {
            this.keyTable[index] = null;
            oldValue = this.valueTable[index];
            this.valueTable[index] = null;
            this.size--;
            return oldValue;
        }
        if (this.isBigTable) {
            index = hash4(hashCode);
            if (this.keyTable[index] == key) {
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
            if (keyTable[i] == key) {
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
            resize(ObjectMap.nextPowerOfTwo(maximumCapacity));
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
        int hashCode = System.identityHashCode(key);
        if (key != this.keyTable[hashCode & this.mask]) {
            if (key != this.keyTable[hash2(hashCode)]) {
                if (key != this.keyTable[hash3(hashCode)]) {
                    if (!this.isBigTable) {
                        return containsKeyStash(key);
                    }
                    if (key != this.keyTable[hash4(hashCode)]) {
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
            if (keyTable[i] == key) {
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
            resize(ObjectMap.nextPowerOfTwo((int) (((float) sizeNeeded) / this.loadFactor)));
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
        if (r6 != 0) goto L_0x0009;
    L_0x0006:
        r6 = "[]";
    L_0x0008:
        return r6;
    L_0x0009:
        r0 = new java.lang.StringBuilder;
        r6 = 32;
        r0.<init>(r6);
        r6 = 91;
        r0.append(r6);
        r4 = r8.keyTable;
        r5 = r8.valueTable;
        r1 = r4.length;
        r2 = r1;
    L_0x001b:
        r1 = r2 + -1;
        if (r2 <= 0) goto L_0x0057;
    L_0x001f:
        r3 = r4[r1];
        if (r3 != 0) goto L_0x0025;
    L_0x0023:
        r2 = r1;
        goto L_0x001b;
    L_0x0025:
        r0.append(r3);
        r0.append(r7);
        r6 = r5[r1];
        r0.append(r6);
        r2 = r1;
    L_0x0031:
        r1 = r2 + -1;
        if (r2 <= 0) goto L_0x004d;
    L_0x0035:
        r3 = r4[r1];
        if (r3 != 0) goto L_0x003b;
    L_0x0039:
        r2 = r1;
        goto L_0x0031;
    L_0x003b:
        r6 = ", ";
        r0.append(r6);
        r0.append(r3);
        r0.append(r7);
        r6 = r5[r1];
        r0.append(r6);
        r2 = r1;
        goto L_0x0031;
    L_0x004d:
        r6 = 93;
        r0.append(r6);
        r6 = r0.toString();
        goto L_0x0008;
    L_0x0057:
        r2 = r1;
        goto L_0x0031;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.esotericsoftware.kryo.util.IdentityMap.toString():java.lang.String");
    }

    public Entries<K, V> entries() {
        if (this.entries == null) {
            this.entries = new Entries(this);
        } else {
            this.entries.reset();
        }
        return this.entries;
    }

    public Values<V> values() {
        if (this.values == null) {
            this.values = new Values(this);
        } else {
            this.values.reset();
        }
        return this.values;
    }

    public Keys<K> keys() {
        if (this.keys == null) {
            this.keys = new Keys(this);
        } else {
            this.keys.reset();
        }
        return this.keys;
    }
}
