package ru.skidoz.service.batch;

import java.awt.Color;
import java.awt.Label;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.category.Category;
import ru.skidoz.model.pojo.category.CategoryGroup;
import ru.skidoz.model.pojo.category.CategorySuperGroup;
import ru.skidoz.model.pojo.search.menu.FilterOption;
import ru.skidoz.model.pojo.search.menu.FilterPoint;
import ru.skidoz.aop.repo.*;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.skidoz.util.Exceler;
import ru.skidoz.util.Structures;

import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.service.ScheduleService.timePoint;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class CategoryOptionsTasklet implements Tasklet {

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
    @Autowired
    private Exceler exceler;

    private static final String MANAGEMENT_FILE = "Book.xlsx";

    @Override
    public void execute() {

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++CategoryOptionsTasklet+++++++++++++++++++++++++++++");

//        WritableWorkbook workbook = null;
//        Workbook templateWorkbook = null;
        try {
//             templateWorkbook = Workbook.getWorkbook((new ClassPathResource(MANAGEMENT_FILE)).getInputStream());
//            workbook = Workbook.createWorkbook(new File(MANAGEMENT_FILE));

            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(MANAGEMENT_FILE);

//            File file1 = new ClassPathResource(MANAGEMENT_FILE).getInputStream();
//            System.out.println("file1***" + file1);

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            //workbook = Workbook.getWorkbook(getClass().getClassLoader().getResource(MANAGEMENT_FILE).openStream());
//            WritableSheet sheet = workbook.getSheet(0);

            for (Iterator<Sheet> sheetIterator = workbook.sheetIterator(); sheetIterator.hasNext(); ) {

                //workbook = Workbook.getWorkbook(getClass().getClassLoader().getResource(MANAGEMENT_FILE).openStream());
                Sheet sheet = sheetIterator.next();
                String sheetName = sheet.getSheetName();

                LanguageEnum language = Objects.requireNonNullElse(LanguageEnum.valueByCode(sheetName), RU);

                int rows = sheet.getPhysicalNumberOfRows();
                int columns = sheet.getRow(0).getPhysicalNumberOfCells();


                CategoryGroup categoryGroup = null;
                CategorySuperGroup categorySuperGroup = null;

                Map<String, Map<String, Set<String>>> categoryOptionsMapMap = exceler.categoryOptionsMapMap;

                XSSFCellStyle style = workbook.createCellStyle();
                XSSFColor color = new XSSFColor(Color.YELLOW);
                style.setFillBackgroundColor(color);

                if (rows >= 1 && columns >= 1) {

                    for (int i = 0; i < rows; i++) {

                        if (sheet.getRow(i) == null) {
                            continue;
                        }

                        if (sheet.getRow(i).getCell(0) != null
                                && !sheet.getRow(i).getCell(0).toString().isEmpty()) {

                            String categorySuperGroupAlias = sheet.getRow(i).getCell(0).toString();
                            categorySuperGroup = categorySuperGroupCacheRepository.findByAlias(categorySuperGroupAlias);


//                            System.out.println(categorySuperGroup + " @categorySuperGroupAlias--- " + categorySuperGroupAlias);

                        } else if (sheet.getRow(i).getCell(2) != null
                                && !sheet.getRow(i).getCell(2).toString().isEmpty()) {

                            String categoryGroupAlias = sheet.getRow(i).getCell(2).toString();

//                            System.out.println("categoryGroupAlias--- " + categoryGroupAlias);

                            categoryGroup = categoryGroupCacheRepository.findByAliasAndCategorySuperGroup(categoryGroupAlias, categorySuperGroup.getId());

                        } else if (sheet.getRow(i).getCell(4) != null
                                && !sheet.getRow(i).getCell(4).toString().isEmpty()) {

                            String categoryAlias = sheet.getRow(i).getCell(4).toString();


                            //System.out.println("categoryGroupDTO+++++++++++" + categoryGroup + " +categoryAlias+ " + categoryAlias);

                            Category categoryEntry = categoryCacheRepository.findByAliasAndCategoryGroup(categoryAlias, categoryGroup.getId());

//                            if (categoryEntry == null) {
//                                System.out.println(categoryAlias + "@*****categoryAlias+++++++" + categoryGroup.getId());
//                            }

                            Map<String, Set<String>> optionsSetMap = categoryOptionsMapMap.get(categoryEntry.getAlias());

                            int j = 6;
                            //начинать можем с начала, так как магазин может добавить новые опшены к существующим поинтам
                            for (; j < columns && !sheet.getRow(i).getCell(j).toString().equals(""); j++) {

                                //раз уж мы читаем этот файл, значит этот point точно существует в базе
                                if (sheet.getRow(i).getCell(j).toString().equals("&")) {

                                    String excelPointName = sheet.getRow(i).getCell(j + 1).toString();

//                                System.out.println("excelPointName++++" + excelPointName);

                                    Set<String> excelOptions = optionsSetMap.get(excelPointName);

                                    Set<String> newFilterOptionSet = new HashSet<>();

                                    FilterPoint filterPoint = filterPointCacheRepository.findByCategoryAndNameRU(
                                            categoryEntry.getId(),
                                            //language,
                                            excelPointName);

                                    List<FilterOption> filterOptionsBase = filterOptionCacheRepository.findAllByFilterPoint(filterPoint.getId());

                                    Set<String> namesFilterOptionBase = filterOptionsBase.stream().map(FilterOption::getName).collect(Collectors.toSet());

                                /* newFilterOptionSet.add(new FilterOption(e -> {
                                                e.setFilterPoint(filterPoint);
                                                e.addName(excelOption);
                                            }));*/
                                    excelOptions.stream()
                                            .filter(excelOption -> !namesFilterOptionBase.contains(excelOption))
                                            .forEach(excelOption -> {
                                                newFilterOptionSet.add(excelOption);
                                                System.out.println("newFilterOptionSet.add(" + excelOption + ");");
                                            });
//         надо потом отдельно вставлять нужные значения в Book и тогда они добавятся   filterOptionRepository.save(newFilterOptionSet);

                                    int finalJ = j + 4;
                                    int finalI = i;
                                    if (filterPoint.getInputType() == 2) {

                                        List<String> cells = new ArrayList<>();
                                        for (int jj = finalJ; jj < columns && !sheet.getRow(i).getCell(jj).toString().equals(""); jj++) {

                                            cells.add(sheet.getRow(i).getCell(jj).toString());
                                        }

                                        for (int jjj = cells.size() - 1; jjj >= 0; jjj--) {

                                            sheet.getRow(i).getCell(jjj + finalJ + newFilterOptionSet.size()).setCellValue(cells.get(jjj));
                                        }

                                        AtomicInteger jjjj = new AtomicInteger();

                                        newFilterOptionSet.forEach(newOption -> {

                                            System.out.println("newOption+++++" + newOption);

                                            Cell newCell = sheet.getRow(finalI).getCell(finalJ + jjjj.get());

                                            System.out.println("newCell.getType()+++" + newCell.getCellType());

                                            if (newCell.getCellType() == CellType.STRING) {
                                                Label lc = (Label) newCell;
                                                lc.setText(newOption);
                                                newCell.setCellStyle(style);
                                            }
                                            jjjj.getAndIncrement();
                                        });

                                    } else if (filterPoint.getInputType() == 1) {

                                        newFilterOptionSet.forEach(newOption -> {

                                            System.out.println("newOption+++++" + newOption);

                                            Cell minCell = sheet.getRow(finalI).getCell(finalJ);
                                            Cell maxCell = sheet.getRow(finalI).getCell(finalJ + 1);

                                            int minCellValue = ((Double) Double.parseDouble(minCell.toString())).intValue();
                                            int maxCellValue = ((Double) Double.parseDouble(maxCell.toString())).intValue();
                                            int newOptionValue = Integer.parseInt(newOption);

                                            System.out.println("minCell+" + minCell + "+++maxCell+" + maxCell);

                                            if (newOptionValue < minCellValue) {
                                                Label lc = (Label) minCell;
                                                lc.setText(newOption);
                                                minCell.setCellStyle(style);
                                            } else if (newOptionValue > maxCellValue) {
                                                Label lc = (Label) maxCell;
                                                lc.setText(newOption);
                                                maxCell.setCellStyle(style);
                                            }
                                        });
                                    }

                                    optionsSetMap.remove(sheet.getRow(i).getCell(finalJ + 1).toString());
                                }
                            }

                            if (optionsSetMap != null) {

                                for (Map.Entry<String, Set<String>> entry : optionsSetMap.entrySet()) {
                                    String pointName = entry.getKey();
                                    Set<String> optionNames = entry.getValue();

                                    boolean numeric = true;
                                    long max = Long.MIN_VALUE;
                                    long min = Long.MAX_VALUE;
                                    for (String optionName : optionNames) {
                                        try {
                                            long l = Structures.parseLong(optionName);
                                            if (l > max) {
                                                max = l;
                                            } else if (l < min) {
                                                min = l;
                                            }
                                        } catch (Exception e) {
                                            numeric = false;
                                            e.printStackTrace();
                                        }
                                    }

                                    Cell newCell = sheet.getRow(i).getCell(j);
                                    sheet.getRow(i).getCell(j).setCellValue("&");
                                    newCell.setCellStyle(style);
//                            sheet.addCell(newCell);

                                    System.out.println("numeric+++++" + numeric);

                                    if (numeric) {

                                        sheet.getRow(i).getCell(++j).setCellValue(String.valueOf(Math.floor(Math.random() * 10000000)));
                                        newCell.setCellStyle(style);
//                                sheet.addCell(newCell);

                                        sheet.getRow(i).getCell(++j).setCellValue(pointName);
                                        newCell.setCellStyle(style);
//                                sheet.addCell(newCell);

                                        sheet.getRow(i).getCell(++j).setCellValue("1");
                                        newCell.setCellStyle(style);
//                                sheet.addCell(newCell);

                                        sheet.getRow(i).getCell(++j).setCellValue(String.valueOf(min));
                                        newCell.setCellStyle(style);
//                                sheet.addCell(newCell);

                                        sheet.getRow(i).getCell(++j).setCellValue(String.valueOf(max));
                                        newCell.setCellStyle(style);
//                                sheet.addCell(newCell);
                                    } else {

                                        sheet.getRow(i).getCell(++j).setCellValue(String.valueOf(Math.floor(Math.random() * 10000000)));
                                        newCell.setCellStyle(style);
//                                sheet.addCell(newCell);

                                        sheet.getRow(i).getCell(++j).setCellValue(pointName);
                                        newCell.setCellStyle(style);
//                                sheet.addCell(newCell);

                                        sheet.getRow(i).getCell(++j).setCellValue("2");
                                        newCell.setCellStyle(style);
//                                sheet.addCell(newCell);

                                        for (String optionName : optionNames) {

                                            j++;

                                            sheet.getRow(i).getCell(++j).setCellValue(optionName);
                                            newCell.setCellStyle(style);
//                                    sheet.addCell(newCell);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                exceler.categoryOptionsMapMap.clear();
            }

            final ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            workbook.write(bytesOut);
            workbook.close();

            //final byte[] byteArray = bytesOut.toByteArray();
            //bytesOut.close();
//                File file = new ClassPathResource(MANAGEMENT_FILE).getFile();

            //File file = new File(timePoint + MANAGEMENT_FILE);
            // commons-io
            //FileUtils.copyInputStreamToFile(inputStream, file);

            //final FileOutputStream fileOut = new FileOutputStream(file);
            //fileOut.write(byteArray);
            //fileOut.close();

            System.out.println("----------------------------CategoryOptionsTasklet finish-------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
