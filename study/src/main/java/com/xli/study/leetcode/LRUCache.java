package com.xli.study.leetcode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 李翔  lixiang1@baozun.com
 * @time: 2021-01-04 11:23
 */
public class LRUCache{
    // LRU 缓存机制，和redis 实现
    // LRU是什么 ，last recently used，使用频率角度内存管理
    // LRU Java实现通过一个 HashMap和 DoubleLinkedList 实现

    private Map<Integer, DoubleLinkedNode> cache = new HashMap<>();
    private Integer capacity;
    private DoubleLinkedNode head,tail;

    public LRUCache(Integer capacity){
        this.capacity = capacity;
        head = new DoubleLinkedNode();
        tail = new DoubleLinkedNode();

        head.pre = null;
        tail.next = null;

        head.next = tail;
        tail.pre = head;
    }

    public Integer get(Integer key){
        // 不存在返回-1
        DoubleLinkedNode node = cache.get(key);
        if (null == node){
            return -1;
        }
        // 调整至双向链表头
        moveToHead(node);
        // 存在 返回map值
        return node.value;
    }

    public void put(Integer key ,Integer value){
        //不存在
        DoubleLinkedNode node = cache.get(key);
        if (null == node){
            node = new DoubleLinkedNode();
            node.key = key;
            node.value = value;
            cache.put(key,node);
            // 调整至双向链表头
            addToHead(node);
//            moveToHead(node);
            // 维持容量不超过上限
            if (cache.size()> capacity){
                DoubleLinkedNode tailNode = popTail();
                cache.remove(tailNode.key);
            }
        }else {        //存在 即更新
            node.value = value;
            moveToHead(node);
        }


    }

    public class DoubleLinkedNode implements Serializable{
        Integer key;
        Integer value;
        DoubleLinkedNode pre;
        DoubleLinkedNode next;

        public DoubleLinkedNode(){
        }

        public DoubleLinkedNode(Integer key,Integer value,DoubleLinkedNode pre,DoubleLinkedNode next){
            this.key = key;
            this.value = value;
            this.pre = pre;
            this.next = next;
        }
    }


    public void removeNode(DoubleLinkedNode node){
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }

    /**
     *
     * @param node
     */
    public void addToHead(DoubleLinkedNode node){
        // 建立新链接
        node.pre = head;
        node.next = head.next;
        // 改变旧链接
        head.next.pre = node;
        head.next = node;
    }

    public void moveToHead(DoubleLinkedNode node){
        this.removeNode(node);
        this.addToHead(node);
    }

    /**
     *
     * @param node
     */
    public void addToTail(DoubleLinkedNode node){
        //建立新链接
        node.pre = tail.pre;
        node.next = tail;

        //改变旧链接
        tail.pre.next = node;
        tail.pre = node;
    }


    public DoubleLinkedNode popTail(){
        DoubleLinkedNode res = tail.pre;
        this.removeNode(res);
        return res;
    }




    public static void main(String[] args){
        LRUCache lRUCache = new LRUCache(2);
//        lRUCache.put(1, 1); // 缓存是 {1=1}
//
//        lRUCache.put(2, 2); // 缓存是 {1=1, 2=2}
//
//        lRUCache.get(1);    // 返回 1
//
//        lRUCache.put(3, 3); // 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
//
//        System.out.println(lRUCache.get(2));    // 返回 -1 (未找到)
//
//        lRUCache.put(4, 4); // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
//        lRUCache.get(1);    // 返回 -1 (未找到)
//        lRUCache.get(3);    // 返回 3
//        lRUCache.get(4);    // 返回 4
//

        lRUCache.put(2,1);
        lRUCache.put(1,1);
        lRUCache.put(2,3);
        lRUCache.put(4,1);
        lRUCache.get(1);
        lRUCache.get(2);
    }


}
