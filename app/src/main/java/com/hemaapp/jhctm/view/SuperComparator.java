package com.hemaapp.jhctm.view;

import com.hemaapp.jhctm.model.Mall;

import java.util.Comparator;

/**
 * Created by lenovo on 2017/1/9.
 * 超市的拼音
 */
public class SuperComparator implements Comparator<Mall> {
    public int compare(Mall o1, Mall o2) {
        if (o1.getCharindex().equals("@")
                || o2.getCharindex().equals("#")) {
            return -1;
        } else if (o1.getCharindex().equals("#")
                || o2.getCharindex().equals("@")) {
            return 1;
        } else {
            return o1.getCharindex().compareTo(o2.getCharindex());
        }
    }

}
