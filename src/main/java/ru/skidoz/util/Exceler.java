package ru.skidoz.util;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;
import ru.skidoz.aop.repo.CategoryCacheRepository;
import ru.skidoz.aop.repo.CategoryFilterProductCacheRepository;
import ru.skidoz.aop.repo.CategoryGroupCacheRepository;
import ru.skidoz.aop.repo.CategorySuperGroupCacheRepository;
import ru.skidoz.aop.repo.FilterOptionCacheRepository;
import ru.skidoz.aop.repo.FilterPointCacheRepository;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.mapper.search.FilterPointMapper;
import ru.skidoz.mapper.side.ShopMapper;
import ru.skidoz.model.pojo.search.menu.CategoryFilterProduct;
import ru.skidoz.model.pojo.search.menu.FilterOption;
import ru.skidoz.model.pojo.search.menu.FilterPoint;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.repository.ShopRepository;
import ru.skidoz.service.ScheduleService;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.category.Category;
import ru.skidoz.model.pojo.category.CategoryGroup;
import ru.skidoz.model.pojo.category.CategorySuperGroup;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.service.search.SearchService;

import static ru.skidoz.util.TextParser.textParser;

/**
 * @author andrey.semenov
 */
@RequiredArgsConstructor
@Component
public class Exceler {


    private final CategoryFilterProductCacheRepository categoryFilterProductRepository;

    private final FilterPointCacheRepository filterPointRepository;

    private final FilterOptionCacheRepository filterOptionRepository;

    private final ShopCacheRepository shopCacheRepository;

    private final ScheduleService scheduleService;

    private final ProductCacheRepository productRepository;

    private final CategoryCacheRepository categoryRepository;

    private final CategoryGroupCacheRepository categoryGroupRepository;

    private final CategorySuperGroupCacheRepository categorySuperGroupRepository;

    private final FilterPointMapper filterPointMapper;

    private final ShopMapper shopMapper;

    private final SearchService searchService;

    public Map<String, Map<String, Set<String>>> categoryOptionsMapMap = new HashMap<>();

    public void processExcel(InputStream excel, User users) throws IOException {

        System.out.println("processExcel()***************" + excel);
        System.out.println("users*****" + users);

        byte[] excelBA = IOUtils.toByteArray(excel);

        XSSFWorkbook workbook = null;
        try {
//            NPOIFSFileSystem pfs = new NPOIFSFileSystem(excel);
//            EncryptionInfo info = new EncryptionInfo(pfs);
//            Decryptor decryptor = Decryptor.getInstance(info);
//            if (!decryptor.verifyPassword("passwordForFile")) {
//                System.out.println("Incorrect password!");
//            } else {
//                System.out.println("Correct password!");
//                InputStream dataStream = decryptor.getDataStream(pfs);
            workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBA));

            for (Iterator<Sheet> sheetIterator = workbook.sheetIterator(); sheetIterator.hasNext(); ) {

                String sheetName = sheetIterator.next().getSheetName();

                LanguageEnum language = LanguageEnum.valueByCode(sheetName);
                if (language != null) {

                    XSSFSheet sheet = workbook.getSheet(sheetName);

                    Shop shop = shopCacheRepository.findByNameAndAdminUser_Id(sheet.getRow(0).getCell(0).getStringCellValue(), users.getId());

                    /*if (shop.getId() < 0) {
                        scheduleService.store(shopCacheRepository, shopRepository, shopMapper);

                        shop = shopCacheRepository.findByNameAndAdminUser_Id(sheet.getRow(0).getCell(0).getStringCellValue(), users.getId());
                    }*/
                    XSSFDrawing patriarch = sheet.createDrawingPatriarch();
                    List<XSSFShape> shapes = patriarch.getShapes();

                    List<Picture> imageByLocations = shapes.stream()
                            .filter(Picture.class::isInstance)
                            .map(s -> (Picture) s)
                            .collect(Collectors.toList());

                    int rows = sheet.getPhysicalNumberOfRows();

                    List<Product>  productList = new ArrayList<>();
                    List<Category>  categoryList = new ArrayList<>();

                    if (rows > 1) {
                        for (int i = 1; i < rows; i++) {
                            if (sheet.getRow(i).getCell(1) != null
                                    && !"".equals(sheet.getRow(i).getCell(1).getStringCellValue())) {

                                Product product = null;
                                if (sheet.getRow(i).getCell(7) != null) {

                                    product = productRepository.findAllByShop_IdAndArticle(shop.getId(), sheet.getRow(i).getCell(7).toString());
                                }
                                if (product == null) {

                                    if (sheet.getRow(i).getCell(8) != null) {
                                        product = productRepository.findAllByShop_IdAndAlias(shop.getId(), sheet.getRow(i).getCell(8).toString());
                                    } else if (sheet.getRow(i).getCell(9) != null) {
                                        product = productRepository.findAllByShop_IdAndAlias(shop.getId(), sheet.getRow(i).getCell(9).toString());
                                    }
                                }
                                if (product == null) {
                                    product = new Product ();
                                }
                                if (sheet.getRow(i).getCell(7) != null) {
                                    product.setArticle(sheet.getRow(i).getCell(7).toString());
                                }

                                if (sheet.getRow(i).getCell(8) != null) {
                                    product.setAlias(sheet.getRow(i).getCell(8).toString());
                                } else if (sheet.getRow(i).getCell(9) != null) {
                                    product.setAlias(sheet.getRow(i).getCell(9).toString());
                                }

                                CategorySuperGroup categorySuperGroup = categorySuperGroupRepository.findByAlias(sheet.getRow(i).getCell(2).getStringCellValue());

                                CategoryGroup categoryGroup = categoryGroupRepository
                                        .findByAliasAndCategorySuperGroup(sheet.getRow(i).getCell(4).getStringCellValue(), categorySuperGroup.getId());

                                Category category = categoryRepository
                                        .findByAliasAndCategoryGroup(sheet.getRow(i).getCell(6).getStringCellValue(), categoryGroup.getId());


                                product.setCategorySuperGroup(categorySuperGroup.getId());
                                product.setCategoryGroup(categoryGroup.getId());
                                product.setCategory(category.getId());
//
//                        product.addCategorySuperGroup(categorySuperGroup);
//                        product.addCategoryGroup(categoryGroup);
//                        product.addCategory(category);
//
//                        if (!sheet.getCell(4, i).getContents().equals("")) {
//                            CatSG categorySuperGroup2 = categorySuperGroupRepository.findByAlias(sheet.getCell(4, i).getContents());
//                            CatG categoryGroup2 = categoryGroupRepository.findByAliasAndCategorySuperGroup(sheet.getCell(5, i).getContents(), categorySuperGroup2);
//                            Cat category2 = categoryRepository.findByAliasAndCategoryGroup(sheet.getCell(6, i).getContents(), categoryGroup2);
//
//                            System.out.println("2categorySuperGroup" + categorySuperGroup2.getId() + categorySuperGroup2.getName());
//                            System.out.println("2categoryGroup" + categoryGroup2.getId() + categoryGroup2.getName());
//                            System.out.println("2category" + category2.getId() + category2.getName());
//
//                            product.addCategorySuperGroup(categorySuperGroup2);
//                            product.addCategoryGroup(categoryGroup2);
//                            product.addCategory(category2);
//                        }
//

                                if (sheet.getRow(i).getCell(8) != null) {
                                    product.setAlias(sheet.getRow(i).getCell(8).getStringCellValue());
                                } else {
                                    product.addName(sheet.getRow(i).getCell(9).getStringCellValue(), language);
                                }
                                product.addName(sheet.getRow(i).getCell(9).getStringCellValue(), language);
                                product.setShortText(sheet.getRow(i).getCell(10).getStringCellValue());
                                product.setBigText(sheet.getRow(i).getCell(11).getStringCellValue());
                                product.setPrice(BigDecimal.valueOf(sheet.getRow(i).getCell(12).getNumericCellValue()));
                                product.setDiscount(BigDecimal.valueOf(sheet.getRow(i).getCell(13).getNumericCellValue()));
                                product.setProductService(sheet.getRow(i).getCell(14).getBooleanCellValue());


                                if (sheet.getRow(i).getCell(15) != null) {
                                    product.setDuration(((Double) sheet.getRow(i).getCell(15).getNumericCellValue()).longValue() / 4);
                                }

                                int pic = 0;
                                for (Picture picture : imageByLocations) {

                                    ClientAnchor anchor = picture.getClientAnchor();
                                    if (anchor.getCol1() == 16 && anchor.getRow1() == i) {
                                        product.setImage(imageByLocations.get(pic).getPictureData().getData());
                                        break;
                                    }
                                    pic++;
                                }

                                product.setShop(shop);
                                product.setActive(true);
                                product.setChatId(users.getChatId());
//                            productRepository.save(product); 4.12.22
                                productList.add(product);
                                categoryList.add(category);
                            }
                        }

                        productList.forEach(product -> {
                            productRepository.save(product);
                        });

                        shop.setExcel(excelBA);
                        shopCacheRepository.save(shop);

                        int prdI = 0;
                        for (int i = 1; i < rows; i++) {

                            int columns = sheet.getRow(i).getPhysicalNumberOfCells();
                            if (sheet.getRow(i).getCell(1) != null
                                    && !"".equals(sheet.getRow(i).getCell(1).getStringCellValue())) {

                                Product product = productList.get(prdI);
                                Category category = categoryList.get(prdI);
                                prdI++;

                                List<String> productWords = textParser(product.getName(language));


                                productWords.addAll(textParser(product.getShortText()));

                                searchService.nameWordMapper(productWords, product);

                                int finalProductId = product.getId();
                                int j = 17;
                                XSSFCell cell = sheet.getRow(i).getCell(j);

                                for (; j < columns - 1
                                        && cell != null
                                        && cell.getCellType().equals(CellType.FORMULA);
                                     j += 2, cell = sheet.getRow(i).getCell(j)) {

                                    cell.setCellType(CellType.STRING);

                                    String filterPointName = cell.getRichStringCellValue().toString();

                                    if (!"0".equals(filterPointName)) {

                                        String optionValue = sheet.getRow(i).getCell(j + 1).toString();

                                        FilterPoint filterPoint = filterPointRepository
                                                .findByCategoryAndUnitNameRu(category.getId(),/* language,*/ filterPointName);

                                        if (filterPoint != null) {

                                            if (filterPoint.getInputType() == 2) {

                                                FilterOption filterOption = filterOptionRepository.findByFilterPointAndName(filterPoint.getId(), optionValue);

                                                if (filterOption != null) {

                                                    CategoryFilterProduct categoryFilterProduct = categoryFilterProductRepository.findByFilterPoint_IdAndProduct_Id(filterPoint.getId(), finalProductId);

                                                    if (categoryFilterProduct == null) {
                                                        categoryFilterProduct = new CategoryFilterProduct(e -> {
                                                            e.setFilterPoint(filterPoint);
                                                            e.setProduct(finalProductId);
                                                            e.setValue(filterOption.getId().intValue());
                                                        });
                                                    }

                                                    categoryFilterProductRepository.save(categoryFilterProduct);
                                                } else {

                                                    if (!categoryOptionsMapMap.containsKey(category.getName(language))) {

                                                        categoryOptionsMapMap.put(category.getName(language), new HashMap<>());
                                                    }
                                                    if (!categoryOptionsMapMap.get(category.getName(language)).containsKey(filterPointName)) {

                                                        categoryOptionsMapMap.get(category.getName(language)).put(filterPointName, new HashSet<>());
                                                    }

                                                    categoryOptionsMapMap.get(category.getName(language)).get(filterPointName).add(optionValue);
                                                }
                                            } else if (filterPoint.getInputType() == 1) {

                                                BigDecimal value = BigDecimal.valueOf(Double.parseDouble(optionValue));

                                                CategoryFilterProduct categoryFilterProduct = categoryFilterProductRepository.findByFilterPoint_IdAndProduct_Id(filterPoint.getId(), finalProductId);

                                                if (categoryFilterProduct == null) {
                                                    categoryFilterProduct = new CategoryFilterProduct(e -> {
                                                        e.setFilterPoint(filterPoint);
                                                        e.setProduct(finalProductId);
                                                        e.setRawValue(value);
                                                    });
                                                }

                                                categoryFilterProductRepository.save(categoryFilterProduct);
                                            }
                                        } else {

                                            if (!categoryOptionsMapMap.containsKey(category.getName(language))) {

                                                categoryOptionsMapMap.put(category.getName(language), new HashMap<>());
                                            }
                                            if (!categoryOptionsMapMap.get(category.getName(language)).containsKey(filterPointName)) {

                                                categoryOptionsMapMap.get(category.getName(language)).put(filterPointName, new HashSet<String>());
                                            }

                                            categoryOptionsMapMap.get(category.getName(language)).get(filterPointName).add(optionValue);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }   finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

}
