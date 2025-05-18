package ru.skidoz.service.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skidoz.service.batch.CacheSearchTasklet;
import ru.skidoz.aop.repo.NameWordCacheRepository;
import ru.skidoz.model.pojo.search.search.MenuSearch;
import ru.skidoz.model.pojo.search.search.Search;
import ru.skidoz.model.pojo.search.search.SearchPoint;
import ru.skidoz.model.pojo.side.NameWordProduct;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.side.NameWord;
import ru.skidoz.aop.repo.NameWordProductCacheRepository;
import ru.skidoz.util.TextParser;

/**
 * @author andrey.semenov
 */
@Component
public class SearchService {

    @Autowired
    private NameWordCacheRepository nameWordRepository;
    @Autowired
    private NameWordProductCacheRepository nameWordProductRepository;
    @Autowired
    private NameWordProductCacheRepository nameWordProductCacheRepository;
    @Autowired
    private CacheSearchTasklet cacheSearchTasklet;

    public List<Integer> getProducts(Search data) {
        System.out.println();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++SearchService+++++++++++++++++++++++++++++++++searchMemory");
        System.out.println();
        System.out.println("data***" + data);
        List<Set<Integer>> sortedResults = new ArrayList<>();

        ////////////////////////////////////////////////////////// Category
        Integer categorySuperGroupId = data.getCategorySuperGroupId();
        Integer categoryGroupId = data.getCategoryGroupId();
        Integer categoryId = data.getCategoryId();
        Integer abstractCategoryId = null;
        if (categoryId != null) {
            abstractCategoryId = categoryId;
        } else if (categoryGroupId != null) {
            abstractCategoryId = categoryGroupId;
        } else if (categorySuperGroupId != null) {
            abstractCategoryId = categorySuperGroupId;
        }

        System.out.println("abstractCategoryId***" + abstractCategoryId);

        if (abstractCategoryId != null) {
            Set<Integer> valuesSet = cacheSearchTasklet.abstractCategoryProductSet.get(abstractCategoryId);

            System.out.println("valuesSet+++*****" + valuesSet);

            if (valuesSet == null || valuesSet.size() == 0) {
//                return getEmptyResponse(data.getPageNumber());
                return new ArrayList<>();
            }

            System.out.println("category------------------" + valuesSet.size());

            sortedResults.add(valuesSet);
        }


        System.out.println("sortedResults-----" + sortedResults);

        ////////////////////////////////////////////////////////// Geo
        if (data.getMinLat() != null
                && data.getMaxLat() != null
                && data.getMinLng() != null
                && data.getMaxLng() != null) {

            Integer minLat = ((Double) (data.getMinLat() * 10_000_000)).intValue();
            Integer maxLat = ((Double) (data.getMaxLat() * 10_000_000)).intValue();
            Integer minLng = ((Double) (data.getMinLng() * 10_000_000)).intValue();
            Integer maxLng = ((Double) (data.getMaxLng() * 10_000_000)).intValue();

            Set<TreeMap<Integer, List<Integer>>> lngSet = new HashSet(cacheSearchTasklet.productGeo.subMap(minLat, maxLat).values());

            Set<Integer> valuesSet = lngSet.stream().map(lngStrip -> lngStrip.subMap(minLng, maxLng).values())
                    .flatMap(Collection::stream)
                    .flatMap(Collection::stream).collect(Collectors.toSet());

            if (valuesSet.size() == 0) {
//                getEmptyResponse(data.getPageNumber());
                return new ArrayList<>();
            }

            System.out.println("Geo------------------" + valuesSet.size());

            sortedResults.add(valuesSet);
        }

        ////////////////////////////////////////////////////////// Price
        if ((data.getPriceMin() != null && data.getPriceMin() > 0)
        || (data.getPriceMax() != null && data.getPriceMax() > 0)) {

            int min = Integer.MIN_VALUE;
            if (data.getPriceMin() != null && data.getPriceMin() > 0) {
                min = data.getPriceMin();
            }

            int max = Integer.MAX_VALUE;
            if (data.getPriceMax() != null && data.getPriceMax() > 0) {
                max = data.getPriceMax();
            }

            SortedMap<Integer, Integer> subSortedMap = cacheSearchTasklet.productPrice.subMap(min, max);

            Set<Integer> valuesSet = new HashSet<>(subSortedMap.values());

            if (valuesSet.size() == 0) {
//                getEmptyResponse(data.getPageNumber());
                return new ArrayList<>();
            }
            sortedResults.add(valuesSet);

            System.out.println("Price------------------" + valuesSet.size());

        }

        ////////////////////////////////////////////////////////// NameWordProduct
        String search = data.getSearch();
        if (search != null && !search.equals("")) {

            List<Integer> searchIndxList = TextParser.search(search, GoodsSearchHandler.parts, GoodsSearchHandler.wordPhraseList);

            System.out.println("searchIndxList-------------" + searchIndxList);

            System.out.println("cacheSearchTasklet.nameWordProductSet---" + cacheSearchTasklet.nameWordProductSet);

            Set<Integer> valuesSet = new HashSet<>();

            for (Integer searchIndx : searchIndxList) {
//                Set<Integer> valuesSet = cacheSearchTasklet.nameWordProductSet.get(searchIndx);
                valuesSet.addAll(cacheSearchTasklet.nameWordProductDTOS.get(searchIndx));

                System.out.println("searchIndx---" + searchIndx + "-valuesSet+++" + valuesSet);
            }
            // если есть текст поиска и ни одного совпадения, значит плохой поиск и ничего не выводим
            if (valuesSet == null || valuesSet.size() == 0) {
//                    return getEmptyResponse(data.getPageNumber());
                return new ArrayList<>();
            }
            sortedResults.add(valuesSet);
        }

        /////////////////////////////////////////////////////////// CategoryFilterProduct
        MenuSearch menuSearch = data.getMenuSearch();

        if (menuSearch != null) {
            for (SearchPoint point : menuSearch.getSearchPointList()) {

                point.getOptionIdList().forEach(e -> System.out.println("getOptionIdList*********" + e));

                TreeMap<Integer, Integer> map = cacheSearchTasklet.filterPointTreeHashCodeProduct.get(point.getId());
                SortedMap<Integer, Integer> subSortedMap = new TreeMap<>();
                if (!point.getOptionIdList().isEmpty()) {

                    for (Integer optionId : point.getOptionIdList()) {

                        subSortedMap.putAll(map.subMap(
                                optionId * 2_000_000,
                                (optionId + 1) * 2_000_000));
                    }
                } else if (point.getMaxValue() != null || point.getMinValue() != null) {

                    if (point.getMaxValue() != null && point.getMinValue() != null) {

                        subSortedMap = map.subMap(
                                point.getMinValue(),
                                point.getMaxValue());
                    } else if (point.getMinValue() != null) {

                        subSortedMap = map.subMap(point.getMinValue(), Integer.MAX_VALUE);
                    } else if (point.getMaxValue() != null) {

                        subSortedMap = map.subMap(Integer.MIN_VALUE, point.getMaxValue());
                    }
                }
                Set<Integer> valuesSetOther = new HashSet<>(subSortedMap.values());

                if (valuesSetOther.size() == 0) {
//                    getEmptyResponse(data.getPageNumber());
                    return new ArrayList<>();
                }
                sortedResults.add(valuesSetOther);
            }
        }

        System.out.println("sortedResults+++" + sortedResults);


        /////////////////////////////////////////////////////////// merge
        List<Integer> valuesPointList = new ArrayList<>();

        Map<Integer, Integer> sortedResultsSizes = new HashMap<>();
        for(int i = 0; i < sortedResults.size(); i++) {
            sortedResultsSizes.put(i, sortedResults.get(i).size());
        }

        Integer[] sortWithIndexes = TextParser.sortWithIndexes(sortedResultsSizes);

        System.out.println("sortWithIndexes+++" + Arrays.toString(sortWithIndexes));

        for (int i = 0; i < sortedResults.size(); i++) {

            Set<Integer> valuesSet = sortedResults.get(sortWithIndexes[i]);

            if (i == 0) {
                valuesPointList = new ArrayList<>(valuesSet);
            } else {
                valuesPointList.retainAll(valuesSet);
            }
        }

        return valuesPointList;
    }


    public void nameWordMapper(List<String> productWords, Product product) { //List<NameWord>

        productWords.forEach(text -> {
//            System.out.println("productWords+++++++" + text);

            NameWord nameWord = nameWordRepository.findByText(text);

//            System.out.println("find nameWord+++" + nameWord);

            if (nameWord == null) {
                nameWord = new NameWord(e -> {
                    e.setText(text);
                });
                nameWordRepository.save(nameWord);
            }

            Integer id = nameWord.getId();
//            NameWord finalNameWord = nameWord;
//            System.out.println("finalNameWord+++" + finalNameWord + "+++product.getId()+" + product.getId());
//            NameWordProduct nameWordProduct = new NameWordProduct(e -> {
//                e.setProduct(new Prd(product.getId()));
//                e.setNameWord(finalNameWord);
//            });

            NameWordProduct nameWordProduct = new NameWordProduct(e -> {
                e.setProduct(product.getId());
                e.setNameWord(id);
            });

            nameWordProductCacheRepository.save(nameWordProduct);
//            nameWordProductRepository.save(nameWordProduct);

//            System.out.println("nameWordProduct***" + nameWordProduct);
//            return nameWord;
        });//.collect(Collectors.toList());
    }
//    public ProductResponse filterMenu(String text) {
//
//        List<Product> resultList = new ArrayList<>();
//
//        System.out.println("text***" + text);
//
//        List<String> searchResults = textParser(text);
//        if (searchResults == null) {
//            return null;
//        }
//
//        if (searchResults.size() > 1) {
//
//            List<Product> resultCSG = categorySuperGroupRepository
//                    .findAllByNameContainingAndNameContaining(searchResults.get(0), searchResults.get(1))
//                    .stream().map(e -> productMapper.toDto(e)).collect(Collectors.toList());
//            if (resultCSG.size() > 0) {
//                resultList.addAll(resultCSG);
//            } else {
//                resultList.addAll(categorySuperGroupRepository.findAllByNameContaining(text).stream().map(e -> productMapper.toDto(e)).collect(Collectors.toList()));
//            }
//
//            List<Product> resultCG = categoryGroupRepository
//                    .findAllByNameContainingAndNameContaining(searchResults.get(0), searchResults.get(1))
//                    .stream().map(e -> productMapper.toDto(e)).collect(Collectors.toList());
//            if (resultCG.size() > 0) {
//                resultList.addAll(resultCG);
//            } else {
//                resultList.addAll(categoryGroupRepository.findAllByNameContaining(text).stream().map(e -> productMapper.toDto(e)).collect(Collectors.toList()));
//            }
//
//            List<Product> resultC = categoryRepository
//                    .findAllByNameContainingAndNameContaining(searchResults.get(0), searchResults.get(1))
//                    .stream().map(e -> productMapper.toDto(e)).collect(Collectors.toList());
//            if (resultC.size() > 0) {
//                resultList.addAll(resultC);
//            } else {
//                resultList.addAll(categoryRepository.findAllByNameContaining(text).stream().map(e -> productMapper.toDto(e)).collect(Collectors.toList()));
//            }
//        } else if (text.length() > 2 && !excludeList.contains(text)) {
//            resultList.addAll(categorySuperGroupRepository.findAllByNameContaining(text).stream().map(e -> productMapper.toDto(e)).collect(Collectors.toList()));
//
//            resultList.addAll(categoryGroupRepository.findAllByNameContaining(text).stream().map(e -> productMapper.toDto(e)).collect(Collectors.toList()));
//
//            resultList.addAll(categoryRepository.findAllByNameContaining(text).stream().map(e -> productMapper.toDto(e)).collect(Collectors.toList()));
//        }
//
//        resultList.forEach(e -> System.out.println("+++" + e.getId() + "***" + e.getName()));
//
//        return new ProductResponseDTO(e -> {
//            e.setElementNumber(resultList.size());
//            e.setProductResponseDTOS(resultList);
//        });
//    }
}
