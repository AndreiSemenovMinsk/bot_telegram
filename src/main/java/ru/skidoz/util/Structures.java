package ru.skidoz.util;


import java.util.ArrayList;
import java.util.List;

/**
 * @author andrey.semenov
 */
public class Structures {


    public static String stringBeautifier(Object text) {

        if (text == null) {
            return "";
        }
        return text.toString();
    }

    public static Long parseLong(String s) {
        int i = s.indexOf("-");
        if (i == -1) {
            return Long.parseLong(s);
        } else {
            return Long.parseLong(s.substring(i));
        }
    }

    public static Integer parseInt(String s) {
        int i = s.indexOf("-");
        if (i == -1) {
            return Integer.parseInt(s);
        } else {
            return Integer.parseInt(s.substring(i));
        }
    }

    public static void sortTwoArrays(List<Integer> orderList, List<Integer> targetList) {
        for (int left = 0; left < orderList.size(); left++) {
            int minInd = left;
            for (int i = left; i < orderList.size(); i++) {
                if (orderList.get(i).compareTo(orderList.get(minInd)) < 0) {

                    minInd = i;
                }
            }
            swap(orderList, left, minInd);
            swap(targetList, left, minInd);
        }
    }

    private static <T> void swap(List<T> orderList, int ind1, int ind2) {
        T temp = orderList.get(ind1);
        orderList.set(ind1, orderList.get(ind2));
        orderList.set(ind2, temp);
    }

    public static <T> List<T> makeEmptyArrayList(int n) {
        var list = new ArrayList<T>();
        for (int i = 0; i < n; i++) {
            list.add(null);
        }
        return list;
    }

}
