package com.zhouyou.music.utils;

import java.util.List;

/**
 * Created by zhouyou on 15/6/17.
 */
public class ListUtils {

    public static <E> boolean isNull(List<E> list) {
        return list == null;
    }

    public static <E> boolean isEmpty(List<E> list) {
        return isNull(list) || list.isEmpty();
    }

    public static <E> E addElement(List<E> list, E element) {
        return addElement(list, element, -1);
    }

    public static <E> E addElement(List<E> list, E element, int index) {
        if (element == null) return null;
        if (isNull(list)) return null;
        if (index == -1) {
            list.add(element);
            return element;
        }
        if (index >= 0 && index <= list.size()) {
            list.add(index, element);
            return element;
        }
        return null;
    }

    public static <E> E delElement(List<E> list, int index) {
        if (isEmpty(list)) return null;
        if (index >= 0 && index < list.size()) {
            return list.remove(index);
        }
        return null;
    }

    public static <E> E delElement(List<E> list, E element) {
        if (isEmpty(list)) return null;
        if (element == null) return null;
        boolean b = list.remove(element);
        return b ? element : null;
    }

    public static <E> E getElement(List<E> list, int index) {
        if (isEmpty(list)) return null;
        if (index >= 0 && index < list.size()) return list.get(index);
        return null;
    }

    public static <E> int getCount(List<E> list) {
        if (list != null) return list.size();
        return 0;
    }

    public static <E> boolean isNull(E[] list) {
        return list == null;
    }

    public static <E> boolean isEmpty(E[] list) {
        return isNull(list) || list.length <= 0;
    }

    public static <E> E addElement(E[] list, E element, int index) {
        if (element == null) return null;
        if (isNull(list)) return null;
        if (index >= 0 && index < list.length) {
            list[index] = element;
            return element;
        }
        return null;
    }

    public static <E> E getElement(E[] list, int index) {
        if (isEmpty(list)) return null;
        if (index >= 0 && index < list.length) return list[index];
        return null;
    }

}
