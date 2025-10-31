import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainingHashTable<K, V> implements DeletelessDictionary<K, V> {
    private List<Item<K, V>>[] table; // The table itself is an array of linked lists of items.
    private int size;
    private int primesIdx = 0;
    private static int[] primes = {11, 23, 47, 97, 197, 397};

    public ChainingHashTable() {
        table = (LinkedList<Item<K, V>>[]) Array.newInstance(LinkedList.class, primes[primesIdx]);
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
        size = 0;
    }

    // This method rehashes. It gets a new size based then repopulates the new table
    private void rehash() {
        List<Item<K, V>>[] temp = table;

        int newSize;
        if (primesIdx+1 < primes.length) {
            newSize = primes[++primesIdx];
        } else if (primesIdx+1 == primes.length) {
            primesIdx++;
            newSize = 513;
        } else {
            newSize = (table.length-1) * 2 + 1;
        }

        table = (LinkedList<Item<K, V>>[]) Array.newInstance(LinkedList.class, newSize);
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }

        size = 0;

        for (List<Item<K, V>> chain: temp) {
            for (Item<K, V> item : chain) {
                insert(item.key, item.value);
            }
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    // Rehashes if needed, then looks to see if the key already exists and changes its value. If not, makes a new entry
    public V insert(K key, V value) {
        if (((double) size + 1) / table.length >= 2.0) {
            rehash();
        }

        // No need for resize
        int index = Math.abs(key.hashCode()) % table.length;

        List<Item<K, V>> chain = table[index];
        for (Item<K, V> item : chain) {
            if (item.key.equals(key)) {
                V prev = item.value;
                item.value = value;
                return prev;
            }
        }

        chain.add(new Item<>(key, value));
        size++;
        return null;

    }

    // Scans the chain where the key should be for the desired key, and returns its value if there
    public V find(K key) {
        int index = Math.abs(key.hashCode()) % table.length;

        List<Item<K, V>> chain = table[index];

        for (Item<K, V> item : chain) {
            if (item.key.equals(key)) {
                return item.value;
            }
        }

        return null;
    }

    // Scans the chain where the key should be for the desired key, and returns true if there
    public boolean contains(K key) {
        int index = Math.abs(key.hashCode()) % table.length;

        List<Item<K, V>> chain = table[index];

        for (Item<K, V> item : chain) {
            if (item.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    // Scans the entire table and compiles keys into a list
    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        for (List<Item<K, V>> chain : table) {
            for (Item<K, V> item : chain) {
                keys.add(item.key);
            }
        }
        return keys;
    }


    // Scans the entire table and compiles values into a list
    public List<V> getValues() {
        List<V> values = new ArrayList<>();
        for (List<Item<K, V>> chain : table) {
            for (Item<K, V> item : chain) {
                values.add(item.value);
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
