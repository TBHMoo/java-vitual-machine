package com.xli.study.leetcode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: 李翔  lixiang1@baozun.com
 * @time: 2021-01-08 11:54
 */
public class FIFOCache<K,V> extends LinkedHashMap<K, V>{

    private final int cacheSize;

    public FIFOCache(int cacheSize){
        this.cacheSize = cacheSize;
    }

    // 当Entry个数超过cacheSize时，删除最老的Entry
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest){
        return size() > cacheSize;
    }

}
