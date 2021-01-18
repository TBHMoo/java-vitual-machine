package com.xli.study.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author: 李翔  lixiang1@baozun.com
 * @time: 2021-01-14 13:06
 */
public class 三数之和{


    class Solution {
        public List<List<Integer>> threeSum(int[] nums) {
            List<List<Integer>> ret = new ArrayList<>();

            Arrays.sort(nums);
            for(int i=0;i<nums.length;i++){
                int target = -nums[i];
                int high = nums.length-1;
                int low = i+1;
                while(low < high){
                    int sum = nums[low] + nums[high];
                    if(sum == target){
                        List<Integer> list = new ArrayList<>();
                        list.add(nums[i]);
                        list.add(nums[low]);
                        list.add(nums[high]);
                        ret.add(list);
                        low++;
                        high++;
                    }else if(sum < target){
                        while(low<high && nums[low] + nums[high] < target) low++;
                    }else {
                        while(low<high && nums[low] + nums[high] > target ) high--;
                    }
                }
            }
            return ret;
        }
    }
}
