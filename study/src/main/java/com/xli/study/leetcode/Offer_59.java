package com.xli.study.leetcode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author: 李翔  lixiang1@baozun.com
 * @time: 2020-12-31 12:59
 */
public class Offer_59{



        public static  int[] maxSlidingWindow(int[] nums, int k) {
            if(nums == null || k==0){
                return new int[0];
            }
            int length = nums.length;
            int[] res = new int[length-k+1];
            Deque<Integer> deque = new LinkedList<>();//deque 非严格递减，deque.getFirst() 就是窗口内最大值

            int initmax = nums[0];
            deque.addFirst(initmax);

            for (int i=1; i<k;i++){
//                if(deque.getFirst().equals(nums[i-1])){
//                    deque.removeFirst();
//                }
                // 移入 nums[j] 小于 dequeFirst ,追加到 dequelast, 大于等于dequeFirst 追加到 dequeFirst 删除小于的元素
                while(!deque.isEmpty() && deque.getLast() < nums[i]){
                    deque.removeLast();
                }
                deque.addLast(nums[i]);
            }
            res[0] = deque.getFirst();
            for(int i = 0,j=k-1;j<length;i++,j++){
                if(i>0){
                    // 移出 nums[i] deque中小于 nums[i] 的元素全部移除
                    if(deque.getFirst().equals(nums[i-1])){
                        deque.removeFirst();
                    }

                    // 移入 nums[j] 小于 dequeFirst ,追加到 dequelast, 大于等于dequeFirst 追加到 dequeFirst 删除小于的元素
                    while(!deque.isEmpty() && deque.getLast() < nums[j]){
                        deque.removeLast();
                    }
                    deque.addLast(nums[j]);
                    // 获取窗口 i,j 内的最大值r
                    res[i] = deque.getFirst();
                }
            }
            return res;
    }


    public static void main(String[] args) {
        int[] nums = new int[]{9,10,9,-7,-4,-8,2,-6};
        int k = 5;
        int[] res = maxSlidingWindow(nums,k);
        System.out.println(res);
    }

}
