package com.xli.study.chapter3;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class ReenterLock implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();
    public static int  i= 0;
    @Override
    public void run() {
        for (int i=0;i<100000000;i++){
            lock.lock();
            try {
                i++;
            }finally {
                lock.unlock();;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        ReenterLock t = new ReenterLock();
//        Thread t1 = new Thread(t);
//        Thread t2 = new Thread(t);
//        t1.start();
//        t2.start();
//        t1.join();
//        t2.join();
//        System.out.println(i);

        // List<List<String>> res = new ArrayList<>();
//        Set<String> foods = new HashSet<>();
//        Map<String, Map<String,Integer>> totalTable = new HashMap<>();
//
//        for(List order:orders){
//            String tableNum = (String)order.get(1);
//            String foodName = (String)order.get(2);
//
//            foods.add(foodName);
//            Map<String,Integer>  table = totalTable.get(tableNum);
//            if(null == table){
//                table = new HashMap<>();
//                table.put(foodName,0);
//            }else{
//                Integer count = table.get(foodName);
//                count++;
//                table.put(foodName,count);
//            }
//        }
//        // system.out.println(JSON.toString(foods));
//        for(String temp:foods){
//            System.out.println(temp);
//        }
//
//        for(String temp:foods){
//            System.out.println(temp);
//        }
    }
//
//    public String reformat(String s) {
//        Integer l = s.length();
//        Integer charCount = 0;
//        Integer numCount= 0;
//
//        byte[] ss= new byte[];
//        s.toCharArray();
//        StringBuilder chars = new StringBuilder();
//        StringBuilder nums = new StringBuilder();
//        for(int i=0;i<l;i++){
//            s.charAt(i
//            if('0' <= s.charAt(i) <= '9'){
//                numCount ++;
//                nums.append(s.indexof(i));
//            }
//            if('a' <= s.indexof(i) <= 'z') {
//                charCount++;
//                chars.append(s.indexof(i));
//            }
//        }
//
//
//        System.out.println(chars.toString());
//        System.out.println(nums.toString());
//        // System.out.println(s);
//        return s;
//
//    }

    public List<List<String>> displayTable(List<List<String>> orders) {
        // List<List<String>> res = new ArrayList<>();
        Set<String> foods = new HashSet<>();
        Map<String, Map<String,Integer>> totalTable = new HashMap<>();

        for(List order:orders){
            String tableNum = (String)order.get(1);
            String foodName = (String)order.get(2);

            foods.add(foodName);
            Map<String,Integer>  table = totalTable.get(tableNum);
            if(null == table){
                table = new HashMap<>();
                table.put(foodName,0);
            }else{
                Integer count = table.get(foodName);
                count++;
                table.put(foodName,count);
            }
        }
        // system.out.println(JSON.toString(foods));
        for(String temp:foods){
            System.out.println(temp);
        }

        for(String temp:foods){
            System.out.println(temp);
        }
        return orders;
    }
}
