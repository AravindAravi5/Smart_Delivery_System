package com.sds.util;

import java.util.HashMap;
import java.util.Map;

// Day 30: LRU Cache implementation using HashMap + Doubly Linked List
public class LRUCache<K, V> {
    
    private class Node {
        K key;
        V value;
        Node prev, next;
        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<K, Node> map;
    private Node head, tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
    }

    public synchronized V get(K key) {
        if (!map.containsKey(key)) {
            return null;
        }
        Node node = map.get(key);
        remove(node);
        insertAtHead(node);
        return node.value;
    }

    public synchronized void put(K key, V value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            remove(node);
            insertAtHead(node);
        } else {
            if (map.size() >= capacity) {
                map.remove(tail.key);
                remove(tail);
            }
            Node newNode = new Node(key, value);
            map.put(key, newNode);
            insertAtHead(newNode);
        }
    }

    private void remove(Node node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;
        
        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;
    }

    private void insertAtHead(Node node) {
        node.next = head;
        node.prev = null;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = head;
    }
}
