package ru.skidoz.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author andrey.semenov
 */

public class TextParser {

    private static final int otstup = 2;
    private static final int max_size_part = 5;
    private static final int size_len = max_size_part - otstup;
    private static int pairsSize;
    private static int nameStringsLength;

    private static final List<String> excludeList = List.of("для", "от", "с", "к", "и", "без", "против", "вместе", "отдельно");
    private static final List<String> extn = List.of("ы", "и", "а", "у", "ю", "ая", "ый", "ой", "ым", "ого", "ому", "е", "ым", "ых", "ий", "им", "ом", "ими", "ыми", "ую", "юю", "ей", "л", "ли", "ли");

    static List<List<String>> get_look(String filter) {

        List<List<String>> arrs = new ArrayList<>();
        for (int j = 0; j < size_len; j++) {
            arrs.add(new ArrayList<>());
        }

        for (int j = 0; j < size_len; j++) {
            for (int k = 0; k < filter.length() - j - otstup; k++) {
                String sub = filter.substring(k, k + j + 1 + otstup);
                if (!arrs.get(j).contains(sub)) {
                    arrs.get(j).add(sub);
                }
            }
        }

        return arrs;
    }

    static void get_parts(String[] names, List<Map<String, List<Integer>>> parts) {

        for (int j = 0; j < size_len; j++) {
            parts.add(new HashMap<>());
        }
        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < size_len; j++) {
                for (int k = 0; k < names[i].length() - j - otstup; k++) {
                    String sub = names[i].substring(k, k + j + 1 + otstup);
                    List<Integer> indexes = parts.get(j).get(sub);
                    if (indexes == null) {
                        parts.get(j).put(sub, new ArrayList<>(List.of(i)));
                    } else {
                        indexes.add(i);
                    }
                }
            }
        }
        //return parts;
    }

    private static List<Integer>[] getRes(
            List<List<String>> look,
            List<Map<String, List<Integer>>> parts,
            int max_result) {

        List<Integer>[] score = new List[101];
        for (int i = 0; i < 100; i++) {
            score[i] = new ArrayList<>();
        }
        int[] counter = new int[nameStringsLength + max_result];
//    List<Integer> indexes = new ArrayList<>();
        for (int j = size_len - 1; j > -1; j--) {

            counter = liner(look, parts.get(j), counter, j, max_result);

            int maxInd = nameStringsLength;
            int e = counter[maxInd];
            if (e != 0) {
                List<Integer> maxResultList = new ArrayList<>();
                maxResultList.add(e);

                maxInd++;
                max_result += nameStringsLength;

                while (maxInd < max_result && e != 0) {

                    e = counter[maxInd];
                    maxResultList.add(e);
                    maxInd++;
                }
                score[100] = maxResultList;
                return score;
            }
        }
//    indexes.forEach(ind -> score[counter[ind]].add(ind));
        for (int ind = 0; ind < counter.length; ind++) {
            score[counter[ind]].add(ind);
        }
        return score;
    }

    public static List<Integer> sortWithIndexes(List<Integer>[] score, int max_result) {

        List<Integer> result = new ArrayList<>(max_result);

        for (int i = 99; i > -1; i--) {
            final List<Integer> integers = score[i];
            int size = integers.size();
            if (size > 0) {
                if (max_result > size) {
                    result.addAll(integers);
                    max_result -= size;
                } else {
//          System.out.println(i + " --- " + integers.subList(0, max_result));
                    result.addAll(integers.subList(0, max_result));
                    break;
                }
            }
        }
        return result;
    }

    public static List<List<Integer>> init(List<String> pairs, List<Map<String, List<Integer>>> parts) {

        Map<String, List<Integer>> names = getNames(pairs.toArray(new String[0]));
        String[] nameStrings1 = names.keySet().toArray(new String[0]);
        get_parts(nameStrings1, parts);
        pairsSize = pairs.size();

        String[] nameStrings = names.keySet().toArray(new String[0]);

        nameStringsLength = nameStrings.length;
        List<List<Integer>> wordPhraseList = new ArrayList<>(nameStringsLength);

        for (String s : nameStrings) {
            wordPhraseList.add(names.get(s));
        }
        return wordPhraseList;
    }

    private static Map<String, List<Integer>> getNames(String[] pairs) {

        Map<String, List<Integer>> names = new HashMap<>();
        for (int i = 0; i < pairs.length; i++) {
            String[] a = pairs[i].split(" ");
            for (String word : a) {
                List<Integer> indexes = names.get(word);
                if (indexes == null) {
                    names.put(word, new ArrayList<>(List.of(i)));
                } else {
                    indexes.add(i);
                }
            }
        }
        return names;
    }

    public static List<Integer> search(String filter,
                                       List<Map<String, List<Integer>>> parts,
                                       List<List<Integer>> wordPhraseList) {

        //String[] filterPart = filter.split(" ");

        System.out.println("FILTER: " + filter);
        System.out.println(" parts " + parts);
        System.out.println("wordPhraseList: " + wordPhraseList);

        List<String> filterPart = textParser(filter);

        int[] counterPhrase = new int[pairsSize];
        List<List<Integer>> selectedWordsPhraseList = new ArrayList<>();

        int max_result = 3;

        for (int i = 0; i < filterPart.size(); i++) {
            final List<List<String>> look = get_look(filter);

            List<Integer>[] res = getRes(look, parts, max_result);

            List<Integer> sortRes;
            if (res[100] != null) {

                System.out.println("A");

                sortRes = res[100];
                if (selectedWordsPhraseList.size() == 0) {
                    int countRes = 0;

                    List<Integer> result = new ArrayList<>();
                    for (int wordInd : sortRes) {
                        List<Integer> integers = wordPhraseList.get(wordInd);
                        for (int integersI = 0; integersI < integers.size()
                                && countRes < max_result; countRes++, integersI++) {
                            int phraseInd = integers.get(integersI);
                            result.add(phraseInd);
                            //result.add(pairs.get(phraseInd));
                        }
                    }
                    return result;
                }
                break;
            } else {

                System.out.println("B " + max_result);

                sortRes = sortWithIndexes(res, max_result);
            }

            System.out.println(wordPhraseList);
            System.out.println(sortRes);

            sortRes.forEach(wordInd -> selectedWordsPhraseList.add(wordPhraseList.get(wordInd)));

            counterPhrase = phaser(selectedWordsPhraseList, counterPhrase, 1);
        }

        List<Integer>[] score = new List[100];
        for (int i = 0; i < 100; i++) {
            score[i] = new ArrayList<>();
        }
        for (int ind = 0; ind < counterPhrase.length; ind++) {
            score[counterPhrase[ind]].add(ind);
        }
        return sortWithIndexes(score, max_result);
        /*List<Integer> sortPhrase = sortWithIndexes(score, max_result);
        List<String> result = new ArrayList<>(sortPhrase.size());
        sortPhrase.forEach(i -> result.add(pairs.get(i)));
        return result;*/
    }


    /*private static int[] liner(
            List<List<String>> look,
            Map<String, List<Integer>> part,
            int[] counter,
            int j) {

//    System.out.println(look);

        for (int i = 0; i < look.get(j).size(); i++) {
            String key = look.get(j).get(i);
            if (key != null) {
                List<Integer> literal_word_ind = part.get(key);

                if (literal_word_ind != null) {
                    for (Integer ind : literal_word_ind) {
//            if (counter[ind] == 0){
//              indexes.add(ind);
//            }
                        counter[ind] += j;
                    }

                    for (int k = j - 1; k > -1; k--) {
                        for (int l = 0; l <= j - k; l++) {
//            System.out.println(look.get(k).get(i + l));
                            look.get(k).set(i + l, null);
                        }
                    }

                }
            }
        }
        return counter;
    }*/

    private static int[] liner(
            List<List<String>> look,
            Map<String, List<Integer>> part,
            int[] counter,
            int j,
            int max_result) {
        if (look.get(j).size() == 1) {
            String key0 = look.get(j).get(0);
            if (key0 != null) {
                List<Integer> literal_word_ind = part.get(key0);

                if (literal_word_ind != null) {

                    if (literal_word_ind.size() < max_result) {
                        max_result = literal_word_ind.size();
                    }

                    for (int m = 0; m < max_result; m++) {
                        counter[nameStringsLength + m] = literal_word_ind.get(m);
                    }
                    return counter;
                }
            }
        }

        for (int i = 0; i < look.get(j).size(); i++) {
            String key = look.get(j).get(i);
            if (key != null) {
                List<Integer> literal_word_ind = part.get(key);

                if (literal_word_ind != null) {

                    for (Integer ind : literal_word_ind) {

                        counter[ind] += j;
                    }

                    for (int k = j - 1; k > -1; k--) {
                        for (int l = 0; l <= j - k; l++) {
                            look.get(k).set(i + l, null);
                        }
                    }
                }
            }
        }
        return counter;
    }

    private static int[] phaser(
            List<List<Integer>> part,
            int[] counter,
            int j) {

        for (int i = 0; i < part.size(); i++) {
            List<Integer> literal_word_ind = part.get(i);

            if (literal_word_ind != null) {
                for (Integer ind : literal_word_ind) {
                    counter[ind] += j;
                }
            }
        }
        return counter;
    }

    public static Integer[] sortWithIndexes(Map<Integer, Integer> map) {
        MapIndexComparator comparator = new MapIndexComparator(map);
        Integer[] indexes = comparator.createIndexArray().toArray(new Integer[0]);
        Arrays.sort(indexes, comparator);
        return indexes;
    }

    public static class MapIndexComparator implements Comparator<Integer> {
        private Map<Integer, Integer> map;
        public MapIndexComparator(Map<Integer, Integer> param) {
            map = param;
        }
        public Set<Integer> createIndexArray() {
            return map.keySet();
        }
        @Override
        public int compare(Integer index1, Integer index2) {   // Autounbox from Integer to int to use as array indexes
            return map.get(index1) < map.get(index2) ? -1 : 1;
        }
    }

    public static List<String> textParser(String text) {

        if (text != null) {
            String[] textArr = text.split(" ");

            if (textArr.length > 0) {
                return Arrays.stream(textArr)
                        .filter(e -> !excludeList.contains(e))
                        .map(searchWord ->
                                extn.stream()
                                        .filter(searchWord::endsWith)
                                        .findFirst()
                                        .map(ext -> searchWord.substring(0, searchWord.length() - ext.length()))
                                        .orElse(searchWord))
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

}