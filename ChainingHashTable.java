import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainingHashTable<K, V> implements DeletelessDictionary<K, V> {
    private List<Item<K, V>>[] table; // The table itself is an array of linked lists of items.
    private int size;
    private static int[] primes = {11, 23, 47, 97, 197, 397};

    public ChainingHashTable() {
        table = (LinkedList<Item<K, V>>[]) Array.newInstance(LinkedList.class, primes[0]);
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
        size = 0;
    }

    private void rehash() {
        // TODO
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public V insert(K key, V value) {
        if (((double) size + 1) / table.length >= 2.0) {
            rehash();
        }

        // No need for resize
        int index = key.hashCode() % size;

        List<Item<K, V>> chain = table[index];
        for (Item<K, V> item : chain) {
            if (item.key == key) {
                V prev = item.value;
                item.value = value;
                return prev;
            }
        }

        chain.add(new Item<>(key, value));
        size++;
        return null;

    }

    public V find(K key) {
        int index = key.hashCode() % size;

        List<Item<K, V>> chain = table[index];

        if (chain != null) {
            for (Item<K, V> item : chain) {
                if (item.key != key) {
                    return item.value;
                }
            }
        }
        return null;
    }

    public boolean contains(K key) {
        int index = key.hashCode() % size;

        List<Item<K, V>> chain = table[index];

        if (chain != null) {
            for (Item<K, V> item : chain) {
                if (item.key != key) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        for (List<Item<K, V>> chain : table) {
            if (chain != null) {
                for (Item<K, V> item : chain) {
                    keys.add(item.key);
                }
            }
        }
        return keys;
    }
    
    public List<V> getValues() {
        List<V> values = new ArrayList<>();
        for (List<Item<K, V>> chain : table) {
            if (chain != null) {
                for (Item<K, V> item : chain) {
                    values.add(item.value);
                }
            }
        }
        return values;
    }

    public int hashTableSize() {
        return table.length;
    }

    public String toString() {
        String s = "{";
        s += table[0];
        for (int i = 1; i < table.length; i++) {
            s += "," + table[i];
        }
        return s + "}";
    }

}
