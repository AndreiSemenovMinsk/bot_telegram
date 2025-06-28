package ru.skidoz.service.initializers;


import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.service.command.CommandName.ACTION_RATE_WITHDRAW_BASIC;
import static ru.skidoz.service.command.CommandName.ADD_BOT;
import static ru.skidoz.service.command.CommandName.ADD_GOODS;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_DESCRIPTION;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_END;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_NAME;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_PHOTO;
import static ru.skidoz.service.command.CommandName.ADD_GOODS_PRICE;
import static ru.skidoz.service.command.CommandName.ADD_TAXI_BOT;
import static ru.skidoz.service.command.CommandName.CONSTRUCT;
import static ru.skidoz.service.command.CommandName.CONSTRUCT_ADD;
import static ru.skidoz.service.command.CommandName.CONSTRUCT_MIN_BILL_SHARE;
import static ru.skidoz.service.command.CommandName.CONSTRUCT_SARAFAN_SHARE;
import static ru.skidoz.service.command.CommandName.EDIT_BUTTON_NAME;
import static ru.skidoz.service.command.CommandName.EDIT_MESSAGE;
import static ru.skidoz.service.command.CommandName.INITIALIZE;
import static ru.skidoz.service.command.CommandName.MULTI_ACTION_LEVEL_BASIC;
import static ru.skidoz.service.command.CommandName.MULTI_LEVEL_QUESTION_BASIC;
import static ru.skidoz.service.command.CommandName.MULTI_LEVEL_RATE_BASIC;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_BUTTON;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_END_BUTTON;
import static ru.skidoz.service.command.CommandName.NEW_LEVEL_INPUT_BUTTON;
import static ru.skidoz.service.command.CommandName.ONE_LEVEL_RATE_BASIC;
import static ru.skidoz.service.command.CommandName.TAXI_LOCATION;
import static ru.skidoz.service.command.CommandName.TAXI_SUBMIT;

import com.google.zxing.WriterException;
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
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.MenuCreator;
import ru.skidoz.util.MenuTypeEnum;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class SkidozonaStartLevelInitializer {

    public static User Users = null;

    public static Shop SHOP = null;

    public static CategorySuperGroup bigCategorySuperGroup = null;

    public static CategoryGroup bigCategoryGroup = null;

    public static Category bigCategory = null;

    @Autowired
    private MenuCreator menuCreator;
    @Autowired
    private LevelCacheRepository levelRepository;
    @Autowired
    private ShopCacheRepository shopRepository;
    @Autowired
    private ButtonRowCacheRepository buttonRowRepository;
    @Autowired
    private UserCacheRepository userRepository;
    @Autowired
    private ButtonCacheRepository buttonRepository;
    @Autowired
    private MessageCacheRepository messageRepository;
    @Autowired
    private CategorySuperGroupCacheRepository categorySuperGroupRepository;
    @Autowired
    private CategoryGroupCacheRepository categoryGroupRepository;
    @Autowired
    private CategoryCacheRepository categoryRepository;
    @Autowired
    private InitialLevel initialLevel;

    private static final String MANAGEMENT_FILE = "Book.xlsx";

    public void initLevels() {


        try {
            Users = userRepository.findByChatId(1L);
            if (Users == null) {
                Users = new User(e -> {
                    e.setChatId(1L);
                    e.setName("SkidoBOT");
                    e.setSessionId("1");
                    e.setShopOwner(true);
                    e.setLanguage(RU);
                });
                userRepository.save(Users);
            }

            Shop SHOP = shopRepository.findById(1);
            if (SHOP == null) {
                SHOP = new Shop();
                SHOP.setName("DEFAULT");
                SHOP.setAdminUser(Users.getId());
                SHOP.setChatId(1L);
                shopRepository.save(SHOP);

                System.out.println("SHOP----------" + SHOP);

                SHOP.getSellerSet().add(Users.getId());

                shopRepository.save(SHOP);

                bigCategorySuperGroup = new CategorySuperGroup();
                bigCategorySuperGroup.setAlias("big");
                categorySuperGroupRepository.save(bigCategorySuperGroup);


                System.out.println("bigCategorySuperGroupDTO+++++++++++++++++++++++" + bigCategorySuperGroup);

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
            }





            //////////СТАРТОВОЕ МЕНЮ для пользователя с магазинами

            initialLevel.level_INITIALIZE.updateLevel(Users, INITIALIZE.name(), initialLevel.level_INITIALIZE222, false);

            levelRepository.cache(initialLevel.level_INITIALIZE);
            Message message0_1 = new Message(initialLevel.level_INITIALIZE, Map.of(RU, "Добро пожаловать в Skidozona"));
            messageRepository.cache(message0_1);
            initialLevel.level_INITIALIZE.addMessage(message0_1);

            ButtonRow row0_00 = new ButtonRow(initialLevel.level_INITIALIZE);
            buttonRowRepository.cache(row0_00);
            initialLevel.level_INITIALIZE.addRow(row0_00);
            Button button0_0_00 = new Button(row0_00, Map.of(RU, "Мониторить цену в WB, Ozone, LaModa.."), initialLevel.level_MONITOR.getIdString());
            buttonRepository.cache(button0_0_00);
            row0_00.add(button0_0_00);

            ButtonRow row0_0 = new ButtonRow(initialLevel.level_INITIALIZE);
            buttonRowRepository.cache(row0_0);
            initialLevel.level_INITIALIZE.addRow(row0_0);
            Button button0_0_0 = new Button(row0_0, Map.of(RU, "Кешбеки"), initialLevel.level_CASHBACKS.getIdString());
            buttonRepository.cache(button0_0_0);
            row0_0.add(button0_0_0);
            Button button0_0_1 = new Button(row0_0, Map.of(RU, "Закладки"), initialLevel.level_BOOKMARKS.getIdString());
            buttonRepository.cache(button0_0_1);
            row0_0.add(button0_0_1);
            System.out.println("D+++" + row0_0);
//                buttonRowRepository.cache(row0_0);
            System.out.println("E+++" + row0_0);
            ButtonRow row0_1 = new ButtonRow(initialLevel.level_INITIALIZE);
            buttonRowRepository.cache(row0_1);
            initialLevel.level_INITIALIZE.addRow(row0_1);
            Button button0_1_0 = new Button(row0_1, Map.of(RU, "Корзина"), initialLevel.level_BASKET.getIdString());
            buttonRepository.cache(button0_1_0);
            row0_1.add(button0_1_0);
            Button button0_1_1 = new Button(row0_1, Map.of(RU, "Поиск"), initialLevel.level_SEARCH.getIdString());
            buttonRepository.cache(button0_1_1);
            row0_1.add(button0_1_1);
//                buttonRowRepository.cache(row0_1);
            ButtonRow row0_2 = new ButtonRow(initialLevel.level_INITIALIZE);
            buttonRowRepository.cache(row0_2);
            initialLevel.level_INITIALIZE.addRow(row0_2);
            Button button0_2_0 = new Button(row0_2, Map.of(RU, "Соединить"), initialLevel.level_CONNECT.getIdString());
            buttonRepository.cache(button0_2_0);
            row0_2.add(button0_2_0);
            Button button0_2_1 = new Button(row0_2, Map.of(RU, "Админка"), initialLevel.level_ADMIN.getIdString());
            buttonRepository.cache(button0_2_1);
            row0_2.add(button0_2_1);
            ButtonRow row0_3 = new ButtonRow(initialLevel.level_INITIALIZE);
            buttonRowRepository.cache(row0_3);
            Button button0_3_1 = new Button(row0_3, Map.of(RU, "EN/DE/RU"), initialLevel.level_LANGUAGES.getIdString());
            buttonRepository.cache(button0_3_1);
            row0_3.add(button0_3_1);
            Button button0_1_0_11 = new Button(row0_3, Map.of(RU, "Архив Корзин"), initialLevel.level_BASKET_ARCHIVE.getIdString());
            buttonRepository.cache(button0_1_0_11);
            row0_3.add(button0_1_0_11);















                System.out.println("PRE addFinalButton");

                initialLevel.addFinalButton(initialLevel.level_INITIALIZE, initialLevel.level_INITIALIZE);

                System.out.println("POST addFinalButton");

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
