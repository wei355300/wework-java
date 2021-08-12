package com.qc.utils;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StringUtils {

    /**
     * 并按字符串正序排序后, 以连接符"_"拼接
     * @param s
     * @param l
     * @return
     */
    public static String concat(String s, List<String> l) {
        List<String> ls = Lists.newArrayList();
        ls.add(s);
        ls.addAll(l);
        return sort(ls);
    }

    public static String concat(List<String> list) {
        List<String> ls = Lists.newArrayList(list);
        return sort(ls);
    }

    private String concat(Optional<String> from, Optional<List<String>> toList) {
        List<String> ls = Lists.newArrayList(toList.get());
        ls.add(from.get());
        return sort(ls);
    }

    private static String sort(List<String> list) {
        Collections.sort(list);
        String str = String.join("_", list);
        return str.length() > 100 ? str.substring(0, 100) : str;
    }
}
