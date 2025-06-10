package ru.skidoz.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Objects;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.category.Category;
import ru.skidoz.model.pojo.category.CategoryGroup;
import ru.skidoz.model.pojo.category.CategorySuperGroup;
import ru.skidoz.model.pojo.search.menu.FilterOption;
import ru.skidoz.model.pojo.search.menu.FilterPoint;
import ru.skidoz.aop.repo.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import static ru.skidoz.model.entity.category.LanguageEnum.RU;

/**
 * @author andrey.semenov
 */
@Component
public class InitialExcelMenu {

    @Autowired
    private CategorySuperGroupCacheRepository categorySuperGroupCacheRepository;
    @Autowired
    private CategoryGroupCacheRepository categoryGroupCacheRepository;
    @Autowired
    private CategoryCacheRepository categoryCacheRepository;
    @Autowired
    private FilterPointCacheRepository filterPointCacheRepository;
    @Autowired
    private FilterOptionCacheRepository filterOptionCacheRepository;

    private static final String MANAGEMENT_FILE = "Book.xlsx";

    //@Transactional
    public void execute() {

        long start = System.currentTimeMillis();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++InitialExcelMenu+++++++++++++++++++++++++++++");
//        Workbook workbook = null;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(getClass()
                    .getClassLoader()
                    .getResourceAsStream(MANAGEMENT_FILE));

            for (Iterator<Sheet> sheetIterator = workbook.sheetIterator(); sheetIterator.hasNext(); ) {

                //workbook = Workbook.getWorkbook(getClass().getClassLoader().getResource(MANAGEMENT_FILE).openStream());

                Category[] parentCats = new Category[6];

                Sheet sheet = sheetIterator.next();
                String sheetName = sheet.getSheetName();

                LanguageEnum language = Objects.requireNonNullElse(LanguageEnum.valueByCode(sheetName), RU);

                int rows = sheet.getPhysicalNumberOfRows();
                CategoryGroup categoryGroup = null;
                CategorySuperGroup categorySuperGroup = null;

                if (rows >= 1) {

                    for (int i = 0; i < rows; i++) {

                        if (sheet.getRow(i) == null) {
                            continue;
                        }

                        int columns = sheet.getRow(i).getLastCellNum();


                        //ystem.out.println("*" + i + "****+_=-="+sheet.getRow(i).getCell(0));


                        if (sheet.getRow(i).getCell(0) != null
                                && !sheet.getRow(i).getCell(0).toString().isEmpty()) {

                            String categorySuperGroupAlias = sheet.getRow(i).getCell(0).toString();
                            String categorySuperGroupName = sheet.getRow(i).getCell(1).toString();

                            categorySuperGroup = categorySuperGroupCacheRepository.findByAlias(categorySuperGroupAlias);

//                            System.out.println(categorySuperGroupAlias + " categorySuperGroupAlias+++++++++++ categorySuperGroup   "
//                                    + categorySuperGroup);

                            if (categorySuperGroup == null) {
                                categorySuperGroup = new CategorySuperGroup(e -> {
                                    e.setAlias(categorySuperGroupAlias);
                                });
                            }
                            categorySuperGroup.addName(categorySuperGroupName, language);

                            categorySuperGroupCacheRepository.save(categorySuperGroup);


                            CategorySuperGroup byAlias = categorySuperGroupCacheRepository
                                    .findByAlias(categorySuperGroupAlias);

                            //System.out.println(categorySuperGroupAlias + " find@*-+- " + byAlias);
                            if (byAlias == null) {
                                System.exit(123);
                            }

                        } else if (columns > 2
                                && sheet.getRow(i).getCell(2) != null
                                && !sheet.getRow(i).getCell(2).toString().isEmpty()) {

                            String categoryGroupAlias = sheet.getRow(i).getCell(2).toString();
                            String categoryGroupName = sheet.getRow(i).getCell(3).toString();

                            //System.out.println(categoryGroupAlias + " @_@@__@ " + categorySuperGroup.getId());

                            categoryGroup = categoryGroupCacheRepository.findByAliasAndCategorySuperGroup(categoryGroupAlias, categorySuperGroup.getId());

                            //System.out.println(categorySuperGroup.getId() + "---------" + categoryGroupAlias + " categoryGroupAlias+++++++++++ categoryGroup   " + categorySuperGroup);

                            if (categoryGroup == null) {
                                categoryGroup = new CategoryGroup(e -> {
                                    e.setAlias(categoryGroupAlias);
                                });
                            }
                            categoryGroup.addName(categoryGroupName, language);

                            //System.out.println("categorySuperGroupDTO+-+-+-+-+-+--+-++-+-+" + categorySuperGroup);

                            categoryGroup.setCategorySuperGroup(categorySuperGroup.getId());
                            categoryGroupCacheRepository.save(categoryGroup);

                        } else if (columns > 4) {

                            int columnCategory = 4;
                            int groupLevel = 0;
                            for (; !(sheet.getRow(i).getCell(columnCategory) != null
                                    && !sheet.getRow(i).getCell(columnCategory).toString().equals(""))
                                    && columnCategory < 14; ) {
                                columnCategory += 2;
                                groupLevel++;
                            }

                            if (sheet.getRow(i).getCell(columnCategory) != null
                                    && !sheet.getRow(i).getCell(columnCategory).toString().equals("")) {

                                String categoryAlias = sheet.getRow(i).getCell(columnCategory).toString();
                                String categoryName = sheet.getRow(i).getCell(columnCategory + 1).toString();

                                Category category = categoryCacheRepository.findByAliasAndCategoryGroup(categoryAlias, categoryGroup.getId());

                                if (category == null) {
                                    category = new Category(e -> {
                                        e.setAlias(categoryAlias);
                                    });
                                    category.setCategoryGroup(categoryGroup.getId());
                                    category.setCategorySuperGroup(categorySuperGroup.getId());
                                    if (groupLevel > 0) {
                                        category.setParentCategory(parentCats[groupLevel - 1].getId());
                                    }

//                                    System.out.println(category + " categoryAlias " + category.getAlias() + " ---"+
//                                            categoryAlias + " categoryGroup.getId()++++++++++++++++++" + categoryGroup.getId());

                                    categoryCacheRepository.save(category);
                                }
                                parentCats[groupLevel] = category;

                                category.addName(categoryName, language);
                                category.setActual(true);
                                categoryCacheRepository.save(category);

                                int inputType = 0;
                                FilterPoint filterPoint = null;

                                for (int j = columnCategory + 2, point_col_index = 0;
                                     j < columns
                                             && sheet.getRow(i).getCell(j) != null
                                             && !sheet.getRow(i).getCell(j).toString().equals("");
                                     j++) {

                                    if (sheet.getRow(i).getCell(j).toString().equals("&")) {

                                        point_col_index = 0;

//                                    System.out.println("category.getId()+++" + category.getId());
//                                    System.out.println("language+++" + language);

/*
                                        try {
                                            String s = sheet.getRow(i).getCell(j + 1).toString();

                                            System.out.println();
                                            System.out.println();
                                            System.out.println("category.getId()+++" + category.getId()
                                                    + "language+++" + language
                                                    + "sheet.getRow(i).getCell(j + 1).toString()+++" + s);
                                            System.out.println();

                                            List<FilterPoint> filterPoints = filterPointRepository.findAllByCategoryAndName_LocaleAndName_Text(
                                                    category.getId(),
                                                    language,
                                                    s);

                                            System.out.println();
                                            System.out.println("-------------------------------------------------------------");
                                            System.out.println();

                                            if (filterPoints.size() > 1) {
                                                System.out.println("+++++++++++++++++++++++++++" + filterPoints.size());
                                                filterPoints.forEach(System.out::println);
                                                System.out.println("---------------------------");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.exit(0);
                                        }*/

                                        filterPoint = filterPointCacheRepository.findByCategoryAndNameRU(
                                                category.getId(),
                                                //language,
                                                sheet.getRow(i).getCell(j + 1).toString());


                                        if (filterPoint == null) {
                                            filterPoint = new FilterPoint();
                                            filterPoint.setCategoryId(category.getId());
                                            filterPoint.addName(sheet.getRow(i).getCell(j + 1).toString(), language);

                                            filterPointCacheRepository.save(filterPoint);
                                        }
                                        filterPoint.addUnitName(sheet.getRow(i).getCell(j + 2).toString(), language);

                                        inputType = ((Double) Double.parseDouble(sheet.getRow(i).getCell(j + 3).toString())).intValue();
                                        filterPoint.setInputType(inputType);


//                                System.out.println("filterPoint-----" + filterPoint + " category.getId()*-*-*" + category.getId());


                                        if (inputType == 1) {
//                                        List<FilterOption> filterOptionList = filterOptionRepository.findAllByFilterPoint(filterPoint);
//                                        filterOptionList.forEach(e->{
//                                            System.out.println(e.getName());
//                                        });
//                                        filterOptionRepository.deleteAll(filterOptionList);
//                                    FilterPoint finalFilterPoint = filterPoint;
//                                    int finalJ = j;
//                                    int finalI = i;
//                                        FilterOption filterOptionMin = new FilterOption(e -> {
//                                            e.setFilterPoint(finalFilterPoint);
//                                            e.addName(sheet.getRow(finalI).getCell(finalJ + 4).toString());
//                                        });
//                                        filterOptionRepository.save(filterOptionMin);
//
//                                        FilterOption filterOptionMax = new FilterOption(e -> {
//                                            e.setFilterPoint(finalFilterPoint);
//                                            e.addName(sheet.getRow(finalI).getCell(finalJ + 5).toString());
//                                        });
//                                        filterOptionRepository.save(filterOptionMax);
                                            BigDecimal min = BigDecimal.valueOf(Double.parseDouble(sheet.getRow(i).getCell(j + 4).toString()));
                                            filterPoint.setMinValue(min);

                                            BigDecimal max = BigDecimal.valueOf(Double.parseDouble(sheet.getRow(i).getCell(j + 5).toString()));
                                            filterPoint.setMaxValue(max);
                                        }

                                        filterPointCacheRepository.save(filterPoint);
                                    }

                                    if (inputType == 2) {

                                        if (point_col_index > 3) {

                                            FilterOption filterOption = filterOptionCacheRepository.findByFilterPointAndName(
                                                    filterPoint.getId(),
                                                    sheet.getRow(i).getCell(j).toString());

                                            if (filterOption == null) {
                                                FilterPoint finalFilterPoint = filterPoint;
                                                int finalJ = j;
                                                int finalI = i;
                                                filterOption = new FilterOption(e -> {

//                                                    System.out.println("finalFilterPoint.getId()++++++++++++++++++" + finalFilterPoint.getId());

                                                    e.setFilterPoint(finalFilterPoint.getId());
                                                    e.setName(sheet.getRow(finalI).getCell(finalJ).toString());
                                                });
                                                filterOptionCacheRepository.save(filterOption);
                                            }
                                        }
                                        point_col_index++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("----------------------------InitialExcelMenu finish-------------------------------" + start);
    }


}
