package ru.skidoz.service.initializers;


import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.service.initializers.InitialLevel.SHOP;
import static ru.skidoz.service.initializers.InitialLevel.Users;

import ru.skidoz.aop.repo.ButtonCacheRepository;
import ru.skidoz.aop.repo.ButtonRowCacheRepository;
import ru.skidoz.aop.repo.CategoryCacheRepository;
import ru.skidoz.aop.repo.CategoryGroupCacheRepository;
import ru.skidoz.aop.repo.CategorySuperGroupCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.pojo.category.Category;
import ru.skidoz.model.pojo.category.CategoryGroup;
import ru.skidoz.model.pojo.category.CategorySuperGroup;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class InitializeEntries {

    public static CategorySuperGroup bigCategorySuperGroup = null;

    public static CategoryGroup bigCategoryGroup = null;

    public static Category bigCategory = null;

    @Autowired
    private CategorySuperGroupCacheRepository categorySuperGroupRepository;
    @Autowired
    private CategoryGroupCacheRepository categoryGroupRepository;
    @Autowired
    private CategoryCacheRepository categoryRepository;
    @Autowired
    private ShopCacheRepository shopRepository;
    @Autowired
    private LevelCacheRepository levelRepository;
    @Autowired
    private ButtonRowCacheRepository buttonRowRepository;
    @Autowired
    private ButtonCacheRepository buttonRepository;
    @Autowired
    private InitialLevel initialLevel;

    
    public void initLevels() {

        try {
            SHOP = shopRepository.findById(1);
            if (SHOP == null) {

                var cloneLevel = initialLevel.level_INITIALIZE.clone(Users);
                levelRepository.save(cloneLevel);


                System.out.println();
                System.out.println();
                System.out.println("cloneLevel-*-*-*-*-*-*-*-*-*" + cloneLevel);
                System.out.println();
                System.out.println();

                SHOP = new Shop();
                SHOP.setName("DEFAULT");
                SHOP.setAdminUser(Users.getId());
                SHOP.setChatId(1L);
                SHOP.setInitialLevelId(cloneLevel.getId());
                SHOP.setSecretHash("646773878:AAEk1xQi-YbqxXB9PZ8scR8pyDrLccrkLqk");
                shopRepository.save(SHOP);

                System.out.println("SHOP----------" + SHOP);

                SHOP.getSellerSet().add(Users.getId());

                shopRepository.save(SHOP);

                bigCategorySuperGroup = new CategorySuperGroup();
                bigCategorySuperGroup.setAlias("big");
                categorySuperGroupRepository.save(bigCategorySuperGroup);


                System.out.println("bigCategorySuperGroupDTO+++++++++++++++++++++++"
                        + bigCategorySuperGroup);

                bigCategoryGroup = new CategoryGroup();
                bigCategoryGroup.setCategorySuperGroup(bigCategorySuperGroup.getId());
                bigCategoryGroup.setAlias("big");
                categoryGroupRepository.save(bigCategoryGroup);

                bigCategory = new Category();
                bigCategory.setCategorySuperGroup(bigCategorySuperGroup.getId());
                bigCategory.setCategoryGroup(bigCategoryGroup.getId());
                bigCategory.setActual(true);
                bigCategory.setAlias("big");
                categoryRepository.save(bigCategory);

                Shop lamoda = new Shop();
                lamoda.setName("lamoda");
                lamoda.setAdminUser(Users.getId());
                lamoda.setChatId(1L);
                shopRepository.save(lamoda);
                System.out.println("lamoda----------" + lamoda);
                lamoda.getSellerSet().add(Users.getId());
                shopRepository.save(lamoda);

                Shop wildberries = new Shop();
                wildberries.setName("wildberries");
                wildberries.setAdminUser(Users.getId());
                wildberries.setChatId(1L);
                shopRepository.save(wildberries);
                System.out.println("wildberries----------" + wildberries);
                wildberries.getSellerSet().add(Users.getId());
                shopRepository.save(wildberries);

                Shop ozon = new Shop();
                ozon.setName("ozon");
                ozon.setAdminUser(Users.getId());
                ozon.setChatId(1L);
                shopRepository.save(ozon);
                System.out.println("ozon----------" + ozon);
                ozon.getSellerSet().add(Users.getId());
                shopRepository.save(ozon);


                System.out.println("PRE addFinalButton");

                addFinalButton(initialLevel.level_INITIALIZE, initialLevel.level_INITIALIZE);

                System.out.println("POST addFinalButton");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private  void addFinalButton(Level level, Level initialLevel) {

        List<ButtonRow> buttonRows = buttonRowRepository.findAllByLevel_Id(level.getId());

        if (buttonRows != null && !buttonRows.isEmpty()) {

            ButtonRow backRow = new ButtonRow(level);
            buttonRowRepository.cache(backRow);

            Button backButton = new Button(backRow, Map.of(RU, "В начало*"), initialLevel.getIdString());
            buttonRepository.cache(backButton);
//            backRow.add(backButton);
            buttonRowRepository.cache(backRow);
            //level.addRow(backRow);
        }
        levelRepository.cache(level);

        List<Level> levels = levelRepository.findAllByParentLevelId(level.getId());

        for (Level childLevel : levels) {
            addFinalButton(childLevel, initialLevel);
        }
    }

}
