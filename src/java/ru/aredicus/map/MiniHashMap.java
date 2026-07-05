package ru.aredicus.map;

import java.util.Objects;

public class MiniHashMap<K, V> {
    private Node<K, V> buckets[];
    private int capacity;
    private int size;
    private double loadFactor;


    public MiniHashMap() {
        capacity = 16;
        size = 0;
        loadFactor = 3.0 / 4.0;
        buckets = new Node[capacity];
    }

    public MiniHashMap(double loadFactor) {
        capacity = 16;
        size = 0;
        this.loadFactor = loadFactor;
        buckets = new Node[capacity];
    }

    public MiniHashMap(int capacity) {
        this.capacity = capacity;
        size = 0;
        this.loadFactor = 3.0 / 4.0;
        buckets = new Node[capacity];
    }

    public MiniHashMap(int capacity, double loadFactor) {
        this.capacity = capacity;
        size = 0;
        this.loadFactor = loadFactor;
        buckets = new Node[capacity];
    }

    public void put(K key, V value) {
        if (capacity * loadFactor <= size) {
            resize();
        }
        putIn(key, value);

    }

    private void putIn(K key, V value) {
        SearchResult<K, V> searchResult = findeNode(key);
        int hash = hash(key);
        if (searchResult.current == null) {
            if (searchResult.prev == null) {
                int index = index(hash);
                buckets[index] = new Node<>(hash, key, value);
                size++;
            } else {
                searchResult.prev.setNext(new Node<>(hash, key, value));
                size++;
            }
        } else {
            searchResult.current.setValue(value);
        }
    }

    private void resize() {
        Node<K, V>[] tmp = new Node[capacity <<= 1];
        var swap = buckets;
        buckets = tmp;
        tmp = swap;
        size = 0;

        for (Node<K, V> kvNode : tmp) {
            Node<K, V> now = kvNode;

            while (now != null) {
                this.putIn(now.getKey(), now.getValue());
                now = now.getNext();
            }
        }
    }

    public V get(K key) {
        Node<K, V> node = findeNode(key).current;
        return node == null ? null : node.getValue();
    }

    public void remove(K key) {
        SearchResult<K, V> searchResult = findeNode(key);
        if (searchResult.current != null) {
            if (searchResult.prev != null) {
                searchResult.prev.setNext(searchResult.current.next);
                searchResult.current.setNext(null);
                size--;
            } else {
                int hash = hash(key);
                int index = index(hash);
                buckets[index] = searchResult.current.getNext();
                searchResult.current.setNext(null);
                size--;
            }
        }
    }

    private SearchResult<K, V> findeNode(K key) {
        int hash = hash(key);
        int index = index(hash);
        Node<K, V> res = buckets[index];
        Node<K, V> prev = null;
        if (res != null) {
            while (res != null) {
                if (res.getHash() == hash && (res.getKey() == key || Objects.equals(key, res.getKey()))) {
                    break;
                }
                prev = res;
                res = res.getNext();
            }
        }
        return new SearchResult<>(prev, res);
    }


    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int index(int hash) {
        return hash & (capacity - 1);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSize() {
        return size;
    }

    private static class SearchResult<K, V> {
        public SearchResult(Node<K, V> prev, Node<K, V> current) {
            this.prev = prev;
            this.current = current;
        }

        Node<K, V> prev;

        Node<K, V> current;

    }

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        public int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }


        public V getValue() {
            return value;
        }

        public Node<K, V> getNext() {
            return next;
        }
    }
}
