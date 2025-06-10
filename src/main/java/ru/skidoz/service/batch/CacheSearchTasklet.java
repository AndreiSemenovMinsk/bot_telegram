package ru.skidoz.service.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ru.skidoz.aop.repo.CategoryFilterProductCacheRepository;
import ru.skidoz.aop.repo.CategorySuperGroupCacheRepository;
import ru.skidoz.aop.repo.CategoryGroupCacheRepository;
import ru.skidoz.aop.repo.CategoryCacheRepository;
import ru.skidoz.aop.repo.NameWordCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.FilterPointCacheRepository;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.NameWordProductCacheRepository;
import ru.skidoz.model.pojo.AbstractDTO;
import ru.skidoz.model.pojo.category.Category;
import ru.skidoz.model.pojo.category.CategoryGroup;
import ru.skidoz.model.pojo.category.CategorySuperGroup;
import ru.skidoz.model.pojo.search.menu.CategoryFilterProduct;
import ru.skidoz.model.pojo.search.menu.FilterPoint;
import ru.skidoz.model.pojo.side.NameWord;
import ru.skidoz.model.pojo.side.NameWordProduct;
import ru.skidoz.model.pojo.side.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.skidoz.service.search.GoodsSearchHandler;
import ru.skidoz.util.TextParser;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class CacheSearchTasklet implements Tasklet {

    @Autowired
    private NameWordCacheRepository nameWordRepository;
    @Autowired
    private NameWordProductCacheRepository nameWordProductRepository;
    @Autowired
    private CategorySuperGroupCacheRepository categorySuperGroupRepository;
    @Autowired
    private CategoryGroupCacheRepository categoryGroupRepository;
    @Autowired
    private CategoryCacheRepository categoryCacheRepository;
    @Autowired
    private ShopCacheRepository shopRepository;
    @Autowired
    private FilterPointCacheRepository filterPointRepository;
    @Autowired
    private CategoryFilterProductCacheRepository categoryFilterProductRepository;
    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private ProductCacheRepository productRepository;


    public Map<Integer, TreeMap<Integer, Integer>> filterPointTreeHashCodeProduct = new HashMap<>();
    public Map<String, Set<Integer>> nameWordProductSet = new TreeMap<>();
    public List<String> nameWordText = new ArrayList<>();
    public List<Set<Integer>> nameWordProductDTOS = new ArrayList<>();
    public Map<Integer, Set<Integer>> abstractCategoryProductSet = new HashMap<>();
    public TreeMap<Integer, Integer> productPrice = new TreeMap<>();
    public TreeMap<Integer, TreeMap<Integer, List<Integer>>> productGeo = new TreeMap<>();


    @Override
    public void execute() {

        System.out.println("----------------------------------CacheSearchTasklet start--------------------------------------");

        filterPointTreeHashCodeProduct = new HashMap<>();
        nameWordProductSet = new HashMap<>();
        nameWordText = new ArrayList<>();
        nameWordProductDTOS = new ArrayList<>();
        productPrice = new TreeMap<>();
        productGeo = new TreeMap<>();


        setFilterPointTreeHashCodeProduct();

        setNameWordProductSet();

        GoodsSearchHandler.pairs = nameWordText;
        GoodsSearchHandler.wordPhraseList = TextParser.init(GoodsSearchHandler.pairs, GoodsSearchHandler.parts);

        setPriceProductSet();

        setProductGeoSet();

        System.out.println("----------------------------------CacheSearchTasklet finish--------------------------------------");
    }

    private void setProductGeoSet() {
        shopRepository.findAll().forEach(shop -> {
            if (shop.getLat() == null || shop.getLng() == null) {
                return;
            }
            Integer lat = ((Double) (shop.getLat() * 10_000_000)).intValue();
            Integer lng = ((Double) (shop.getLng() * 10_000_000)).intValue();

            List<Integer> products = productCacheRepository.findAllByShop_Id(shop.getId()).stream().map(AbstractDTO::getId).collect(Collectors.toList());

            if (productGeo.containsKey(lat)){
                if (productGeo.get(lat).containsKey(lng)){
                    productGeo.get(lat).get(lng).addAll(products);
                } else {
                    productGeo.get(lat).put(lng, products);
                }
            } else {
                productGeo.put(lat, new TreeMap<>(Map.of(lng, products)));
            }
        });
    }

    private void setPriceProductSet() {

        List<Product> products = productRepository.findAll();
        products.forEach(product -> productPrice.put(product.getPriceHash(), product.getId()));
    }

    private void setNameWordProductSet() {

        List<NameWord> nameWords = nameWordRepository.findAll();


        for (NameWord nameWord : nameWords) {

//            System.out.println("nameWord+++" + nameWord);

            List<NameWordProduct> nameWordProducts = nameWordProductRepository.findByNameWord(nameWord.getId());

//            System.out.println("nameWordProducts+++" + nameWordProducts);

//            Product                                          productMapper.toDto(nwp.getProduct()
            Set<Integer> productDTOS = nameWordProducts.stream().map(nwp -> nwp.getProduct()).collect(Collectors.toSet());

            nameWordProductSet.put(nameWord.getText(), productDTOS);

            nameWordText.add(nameWord.getText());
            nameWordProductDTOS.add(productDTOS);
        }
    }

    private void setFilterPointTreeHashCodeProduct() {


        System.out.println("setFilterPointTreeHashCodeProduct--------");

        Set<Integer> categorySuperGroupProductIds = new HashSet<>();

        List<CategorySuperGroup>  catSGList = categorySuperGroupRepository.findAll();
        catSGList.forEach(categorySuperGroup -> {

            List<CategoryGroup>  catGList = categoryGroupRepository.findAllByCategorySuperGroup_Id(categorySuperGroup.getId());
            categorySuperGroup.setCategoryGroupSet(new ArrayList<>(catGList));

            Set<Integer> categoryGroupProductIds = new HashSet<>();

            catGList.forEach(categoryGroup -> {

                List<Category>  catList = categoryCacheRepository.findByCategoryGroup_Id(categoryGroup.getId());

                categoryGroup.setCategorySet(new ArrayList<>(catList));
                catList.forEach(category -> {

                    //System.out.println("category.getId()@@@ " + category.getId());

                    List<Product> productDTOs = productCacheRepository.findAllByCategory_IdAndActive(category.getId(), true);

                    // где это используется
                    category.setProductSet(new ArrayList<>(productDTOs));

//                    System.out.println("category.getId()--------- " + category.getId());

//                    filterPointRepository.findAllByCategory_Id(category.getId()).forEach(e -> System.out.println("filterPoint***" + e.getId()));

//                    System.out.println(category + " *-*-*-*-*-* " + category.getId() + " " + productDTOs.size());

                    List<FilterPoint>  filterPointDTOS = filterPointRepository.findAllByCategory_Id(category.getId());
                                                                                //getParentCategoryId

                    // где это используется
                    category.setFilterPointList(filterPointDTOS);


                    Set<Integer> categoryProductIds = new HashSet<>(productDTOs.size());

                    productDTOs.forEach(product -> {

                        System.out.println("product.getId()***" + product.getId());

                        categoryProductIds.add(product.getId());
                        categoryGroupProductIds.add(product.getId());
                                categorySuperGroupProductIds.add(product.getId());

                        filterPointDTOS.forEach(filterPoint -> {

//                            System.out.println(filterPoint.getId() + "@@@@@@@@@" + product.getId());

                            CategoryFilterProduct categoryFilterProduct
                                    = categoryFilterProductRepository.findByFilterPoint_IdAndProduct_Id(filterPoint.getId(), product.getId());

                            if (categoryFilterProduct != null) {

//                                System.out.println("filterPoint.getId()+++" + filterPoint.getId());
//                                System.out.println("categoryFilterProductDTO-----" + categoryFilterProduct);
//                                System.out.println("categoryFilterProduct.getHashCode()+++" + categoryFilterProduct.getHashCode() + "***" + categoryFilterProduct.getProduct());

                                filterPointTreeHashCodeProduct.put(filterPoint.getId(), new TreeMap<>());

                                filterPointTreeHashCodeProduct
                                        .get(filterPoint.getId())
                                        .put(categoryFilterProduct.getHashCode(), categoryFilterProduct.getProduct());
                            } else {
                                System.out.println(filterPoint.getId() + "@@@@@@@@@" + product.getId());
                                System.out.println(filterPoint.getNameRU() + "----------" + product.getNameRU());
                            }

                        });
                    });

                    abstractCategoryProductSet.put(category.getId(), categoryProductIds);
//                    System.out.println("abstractCategoryProductSet.put(" + category.getId() + ", " + categoryProductIds);
                });

                abstractCategoryProductSet.put(categoryGroup.getId(), categoryGroupProductIds);
//                System.out.println("abstractCategoryProductSet.put(" + categoryGroup.getId() + ", " + categoryGroupProductIds);
            });

            abstractCategoryProductSet.put(categorySuperGroup.getId(), categorySuperGroupProductIds);
//            System.out.println("abstractCategoryProductSet.put(" + categorySuperGroup.getId() + ", " + categorySuperGroupProductIds);

        });
    }
}
