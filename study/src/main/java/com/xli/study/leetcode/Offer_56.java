package com.xli.study.leetcode;

/**
 * @author: 李翔  lixiang1@baozun.com
 * @time: 2020-12-31 14:53
 */
public class Offer_56{

    /**
     * 一个数组中，除了一个数字以外，其余数字都出现 m 次 的通用问题
     * 考虑数字的二进制形式，汇总每一位的汉明重量， 然后将汉明重量取余m, 将剩余的汉明重量 数组，恢复成到一个数字上（利用左移 + 或）
     */
    class Solution {
        public int singleNumber(int[] nums) {
            int[] bitCount = new int[32];
            for(int i=0;i<nums.length; i++){
                for(int j=0;j<32;j++){
                    bitCount[j] +=nums[i]&1;
                    nums[i]>>=1;
                }
            }


            int res  = 0,m = 3;
            for(int i=0;i<32; i++){
                res <<= 1;
                res |= (bitCount[31-i] % m);
            }
            return res;
        }
    }

}
