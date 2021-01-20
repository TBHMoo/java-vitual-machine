package com.xli.study.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class leetcode84 {
    class Solution {
        public int largestRectangleArea(int[] heights) {
            int area= 0;
            int len = heights.length;
            if(len ==0)return 0;
            if(len ==1) return heights[0];
            Deque<Integer> stack = new ArrayDeque<>();
            for(int i=0;i<len;i++){
                while(!stack.isEmpty() && heights[stack.peekLast()] > heights[i]){
                    int height = heights[stack.removeLast()];
                    while(!stack.isEmpty() && heights[stack.peekLast()] == height){
                        stack.removeLast();
                    }
                    int with;
                    if(stack.isEmpty()){
                        with = i;
                    }else{
                        with = i- stack.removeLast() -1;
                    }
                    area = Math.max(area,height*with);
                }
                stack.addLast(i);
            }

            while(!stack.isEmpty()){
                int height = heights[stack.removeLast()];
                while(!stack.isEmpty() && heights[stack.peekLast()] == height){
                    stack.removeLast();
                }
                int with;
                if(stack.isEmpty()){
                    with = len;
                }else{
                    with = len- stack.removeLast() -1;
                }
                area = Math.max(area,height*with);
            }
            return area;

        }
    }
}
