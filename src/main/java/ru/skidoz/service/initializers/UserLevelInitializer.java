package ru.skidoz.service.initializers;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.zxing.WriterException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

import static ru.skidoz.model.entity.category.LanguageEnum.DE;
import static ru.skidoz.model.entity.category.LanguageEnum.EN;
import static ru.skidoz.model.entity.category.LanguageEnum.RU;
import static ru.skidoz.service.command.CommandName.INITIALIZE0;
import static ru.skidoz.service.command.CommandName.LANGUAGER;
import static ru.skidoz.service.command.CommandName.LANGUAGES;
import static ru.skidoz.util.TelegramElementsUtil.qrInputStream;
import static ru.skidoz.service.command.CommandName.ADD_BASKET;
import static ru.skidoz.service.command.CommandName.ADD_BOOKMARK;
import static ru.skidoz.service.command.CommandName.BASKET;
import static ru.skidoz.service.command.CommandName.BASKET_ARCHIVE;
import static ru.skidoz.service.command.CommandName.BOOKMARKS;
import static ru.skidoz.service.command.CommandName.CASHBACKS;
import static ru.skidoz.service.command.CommandName.CONNECT;
import static ru.skidoz.service.command.CommandName.CONNECT_SHOP;
import static ru.skidoz.service.command.CommandName.MONITOR;
import static ru.skidoz.service.command.CommandName.MONITOR_PRICE;
import static ru.skidoz.service.command.CommandName.MONITOR_RESP;
import static ru.skidoz.service.command.CommandName.MY_SHOPS;
import static ru.skidoz.service.command.CommandName.P2NOP;
import static ru.skidoz.service.command.CommandName.P2NOP_RESP;
import static ru.skidoz.service.command.CommandName.P2P;
import static ru.skidoz.service.command.CommandName.P2P_RESP;
import static ru.skidoz.service.command.CommandName.PSHARE2P;
import static ru.skidoz.service.command.CommandName.SEARCH;
import static ru.skidoz.service.command.CommandName.SEARCH_RESULT;
import static ru.skidoz.service.command.CommandName.SEARCH_RESULT_PRODUCT;

/**
 * @author andrey.semenov
 */
@Component
public class UserLevelInitializer {

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

                //////////СТАРТОВОЕ МЕНЮ для пользователя без магазинов

                initialLevel.level_INITIALIZE0.updateLevel(Users, INITIALIZE0.name(), initialLevel.level_INITIALIZE, true);

                levelRepository.cache(initialLevel.level_INITIALIZE0);
                Message message00_1 = new Message(initialLevel.level_INITIALIZE0, Map.of(RU, "Добро пожаловать в Skidozona"));
                messageRepository.cache(message00_1);
                initialLevel.level_INITIALIZE0.addMessage(message00_1);
                ButtonRow row00_0 = new ButtonRow(initialLevel.level_INITIALIZE0);
                buttonRowRepository.cache(row00_0);
                initialLevel.level_INITIALIZE0.addRow(row00_0);
                Button button00_0_0 = new Button(row00_0, Map.of(RU, "Кешбеки"), initialLevel.level_CASHBACKS.getIdString());
                buttonRepository.cache(button00_0_0);
                row00_0.add(button00_0_0);
                Button button00_0_1 = new Button(row00_0, Map.of(RU, "Закладки"), initialLevel.level_BOOKMARKS.getIdString());
                buttonRepository.cache(button00_0_1);
                row00_0.add(button00_0_1);
//                buttonRowRepository.cache(row00_0);
                ButtonRow row00_1 = new ButtonRow(initialLevel.level_INITIALIZE0);
                buttonRowRepository.cache(row00_1);
                initialLevel.level_INITIALIZE0.addRow(row00_1);
                Button button00_1_0 = new Button(row00_1, Map.of(RU, "Корзина"), initialLevel.level_BASKET.getIdString());
                buttonRepository.cache(button00_1_0);
                row00_1.add(button00_1_0);
                Button button00_1_1 = new Button(row00_1, Map.of(RU, "Поиск"), initialLevel.level_SEARCH.getIdString());
                buttonRepository.cache(button00_1_1);
                row00_1.add(button00_1_1);
//                buttonRowRepository.cache(row00_1);
                ButtonRow row00_2 = new ButtonRow(initialLevel.level_INITIALIZE0);
                buttonRowRepository.cache(row00_2);
                initialLevel.level_INITIALIZE0.addRow(row00_2);
                Button button00_2_0 = new Button(row00_2, Map.of(RU, "Соединить"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button00_2_0);
                row00_2.add(button00_2_0);
                Button button00_2_1 = new Button(row00_2, Map.of(RU, "Мои магазины"), initialLevel.level_MY_SHOPS.getIdString());
                buttonRepository.cache(button00_2_1);
                row00_2.add(button00_2_1);
//                buttonRowRepository.cache(row00_2);
                ButtonRow row00_3 = new ButtonRow(initialLevel.level_INITIALIZE0);
                buttonRowRepository.cache(row00_3);
                initialLevel.level_INITIALIZE0.addRow(row00_3);
                Button button00_3_0 = new Button(row00_3, Map.of(RU, "Создать магазин/сервис"), initialLevel.level_CONSTRUCT.getIdString());
                buttonRepository.cache(button00_3_0);
                row00_3.add(button00_3_0);
                Button button00_3_1 = new Button(row00_3, Map.of(RU, "EN/DE/RU"), initialLevel.level_LANGUAGES.getIdString());
                buttonRepository.cache(button00_3_1);
                row00_3.add(button00_3_1);
                Button button000_1_0_11 = new Button(row00_3, Map.of(RU, "Архив Корзин"), initialLevel.level_BASKET_ARCHIVE.getIdString());
                buttonRepository.cache(button000_1_0_11);
                row00_3.add(button000_1_0_11);
//                buttonRowRepository.cache(row00_3);

                //////////ОБЩЕЕ ДЛЯ ЯЗЫКОВ
                initialLevel.level_LANGUAGES.updateLevel(Users, LANGUAGES.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_LANGUAGES);
                Message message00_1_ = new Message(initialLevel.level_LANGUAGES, Map.of(RU, "Choose another language"));
                messageRepository.cache(message00_1_);
                initialLevel.level_LANGUAGES.addMessage(message00_1_);

                ButtonRow row0_2_lang = new ButtonRow(initialLevel.level_LANGUAGES);
                buttonRowRepository.cache(row0_2_lang);
                initialLevel.level_LANGUAGES.addRow(row0_2_lang);
                Button button0_2_0_lang = new Button(row0_2_lang, Map.of(RU, "RU", EN,  "RU", DE,  "RU"),  initialLevel.level_LANGUAGER.getIdString() + "RU");
                buttonRepository.cache(button0_2_0_lang);
                row0_2_lang.add(button0_2_0_lang);
                Button button0_2_1_lang = new Button(row0_2_lang, Map.of(RU, "EN", EN,  "EN", DE,  "EN"),  initialLevel.level_LANGUAGER.getIdString() + "EN");
                buttonRepository.cache(button0_2_1_lang);
                row0_2_lang.add(button0_2_1_lang);
                Button button0_2_2_lang = new Button(row0_2_lang, Map.of(RU, "DE", EN,  "DE", DE,  "DE"),  initialLevel.level_LANGUAGER.getIdString() + "DE");
                buttonRepository.cache(button0_2_2_lang);
                row0_2_lang.add(button0_2_2_lang);

                //////////ОБРАБОТЧИК ВЫБОРА ЯЗЫКОВ
                initialLevel.level_LANGUAGER.updateLevel(Users, LANGUAGER.name(), initialLevel.level_LANGUAGES, false);

                levelRepository.cache(initialLevel.level_LANGUAGER);
                Message message00_1_r = new Message(initialLevel.level_LANGUAGER, Map.of(RU, "Language successfully changed"));
                messageRepository.cache(message00_1_r);
                initialLevel.level_LANGUAGER.addMessage(message00_1_r);


                //////////Монитор

                initialLevel.level_MONITOR.updateLevel(Users, MONITOR.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_MONITOR);
                Message message123_10 = new Message(initialLevel.level_MONITOR, Map.of(RU, "Пришлите ссылку из карточки товара на Wildberries, Ozon, LaModa, 21 Век"));
                messageRepository.cache(message123_10);
                initialLevel.level_MONITOR.addMessage(message123_10);


                initialLevel.level_MONITOR_PRICE.updateLevel(Users, MONITOR_PRICE.name(), initialLevel.level_MONITOR, true);

                levelRepository.cache(initialLevel.level_MONITOR_PRICE);
                Message message123_11 = new Message(initialLevel.level_MONITOR_PRICE, Map.of(RU, "Товар успешно найден. Текущая цена ЦЦЦ."));
                messageRepository.cache(message123_11);
                initialLevel.level_MONITOR_PRICE.addMessage(message123_11);
                Message message123_12 = new Message(initialLevel.level_MONITOR_PRICE, Map.of(RU, "Пришлите целевую цену, при достижении которой будет отправлено уведомление"));
                messageRepository.cache(message123_12);
                initialLevel.level_MONITOR_PRICE.addMessage(message123_12);


                initialLevel.level_MONITOR_RESP.updateLevel(Users, MONITOR_RESP.name(), initialLevel.level_MONITOR_PRICE, true);

                levelRepository.cache(initialLevel.level_MONITOR_RESP);//Цена на ТТТ снизилась до ППП
                Message message123_13 = new Message(initialLevel.level_MONITOR_RESP, Map.of(RU, "Закладка сохранена!"));
                messageRepository.cache(message123_13);
                initialLevel.level_MONITOR_RESP.addMessage(message123_13);


                //////////КЭШБЕКИ

                initialLevel.level_CASHBACKS.updateLevel(Users, CASHBACKS.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_CASHBACKS);
                Message message1_1 = new Message(initialLevel.level_CASHBACKS, Map.of(RU, "Список активных кэшбеков:"));
                messageRepository.cache(message1_1);
                initialLevel.level_CASHBACKS.addMessage(message1_1);
                ButtonRow row01_0 = new ButtonRow(initialLevel.level_CASHBACKS);
                buttonRowRepository.cache(row01_0);
                Button button01_0_0 = new Button(row01_0, Map.of(RU, "Рекомендовать/ запрос кэшбека"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button01_0_0);
                row01_0.add(button01_0_0);

                initialLevel.level_CASHBACKS.addRow(row01_0);


                //////////СВЯЗАТЬ

                initialLevel.level_CONNECT.updateLevel(Users, CONNECT.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_CONNECT);
                Message message2_1 = new Message(initialLevel.level_CONNECT, 0, Map.of(RU, "ссылка"), IOUtils.toByteArray(qrInputStream("ссылка")), "ссылка");
                messageRepository.cache(message2_1);
                initialLevel.level_CONNECT.addMessage(message2_1);
                Message message2_2 = new Message(initialLevel.level_CONNECT, Map.of(RU, "Или перешлите ссылку:"));
                messageRepository.cache(message2_2);
                initialLevel.level_CONNECT.addMessage(message2_2);
                Message message2_3 = new Message(initialLevel.level_CONNECT, Map.of(RU, "https://t.me/Skido_Bot?start=userId"));
                messageRepository.cache(message2_3);
                initialLevel.level_CONNECT.addMessage(message2_3);


                //////////СВЯЗАТЬ С МАГАЗИНОМ
                initialLevel.level_CONNECT_SHOP.updateLevel(Users, CONNECT_SHOP.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_CONNECT_SHOP);

                //////////ЗАКЛАДКИ

                initialLevel.level_BOOKMARKS.updateLevel(Users, BOOKMARKS.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_BOOKMARKS);
                Message message3_1 = new Message(initialLevel.level_BOOKMARKS, Map.of(RU, "Список закладок:"));
                messageRepository.cache(message3_1);
                initialLevel.level_BOOKMARKS.addMessage(message3_1);
                ButtonRow row3_0 = new ButtonRow(initialLevel.level_BOOKMARKS);
                buttonRowRepository.cache(row3_0);
                Button button3_0_0 = new Button(row3_0, Map.of(RU, "Показать магазину"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button3_0_0);
                row3_0.add(button3_0_0);
                initialLevel.level_BOOKMARKS.addRow(row3_0);


                initialLevel.level_ADD_BOOKMARK.updateLevel(Users, ADD_BOOKMARK.name(), initialLevel.level_SEARCH, false);

                levelRepository.cache(initialLevel.level_ADD_BOOKMARK);
                Message message3_1_1 = new Message(initialLevel.level_ADD_BOOKMARK, Map.of(RU, "Закладка на товар добавлена!"));
                messageRepository.cache(message3_1_1);
                initialLevel.level_ADD_BOOKMARK.addMessage(message3_1_1);
                ButtonRow row3_1 = new ButtonRow(initialLevel.level_ADD_BOOKMARK);
                buttonRowRepository.cache(row3_1);
                Button button3_1_1 = new Button(row3_1, Map.of(RU, "К поиску"), initialLevel.level_SEARCH.getIdString());
                buttonRepository.cache(button3_1_1);
                row3_1.add(button3_1_1);
                initialLevel.level_ADD_BOOKMARK.addRow(row3_1);


                //////////КОРЗИНА

                initialLevel.level_BASKET.updateLevel(Users, BASKET.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_BASKET);
                Message message4_1 = new Message(initialLevel.level_BASKET, Map.of(RU, "Товары в корзине:"));
                messageRepository.cache(message4_1);
                initialLevel.level_BASKET.addMessage(message4_1);
                ButtonRow row4_0 = new ButtonRow(initialLevel.level_BASKET);
                buttonRowRepository.cache(row4_0);
                Button button4_0_0 = new Button(row4_0, Map.of(RU, "Показать магазину"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button4_0_0);
                row4_0.add(button4_0_0);
                initialLevel.level_BASKET.addRow(row4_0);


                ////////// АРХИВ КОРЗИН

                initialLevel.level_BASKET_ARCHIVE.updateLevel(Users, BASKET_ARCHIVE.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_BASKET_ARCHIVE);
                Message message400_1 = new Message(initialLevel.level_BASKET_ARCHIVE, Map.of(RU, "Архив корзин:"));
                messageRepository.cache(message400_1);
                initialLevel.level_BASKET_ARCHIVE.addMessage(message400_1);
                ButtonRow row400_0 = new ButtonRow(initialLevel.level_BASKET_ARCHIVE);
                buttonRowRepository.cache(row400_0);
                Button button400_0_0 = new Button(row400_0, Map.of(RU, "Показать магазину"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button400_0_0);
                row400_0.add(button400_0_0);
                initialLevel.level_BASKET_ARCHIVE.addRow(row400_0);




                initialLevel.level_ADD_BASKET.updateLevel(Users, ADD_BASKET.name(), initialLevel.level_SEARCH, false);

                levelRepository.cache(initialLevel.level_ADD_BASKET);
                Message message4_1_1 = new Message(initialLevel.level_ADD_BASKET, Map.of(RU, "Товар добавлен в корзину!"));
                messageRepository.cache(message4_1_1);
                initialLevel.level_ADD_BASKET.addMessage(message4_1_1);
                ButtonRow row4_1 = new ButtonRow(initialLevel.level_ADD_BASKET);
                buttonRowRepository.cache(row4_1);
                Button button4_1_1 = new Button(row4_1, Map.of(RU, "К поиску"), initialLevel.level_SEARCH.getIdString());
                buttonRepository.cache(button4_1_1);
                row4_1.add(button4_1_1);
                initialLevel.level_ADD_BASKET.addRow(row4_1);


                //////////МАГАЗИНЫ ДЛЯ ПОКУПАТЕЛЯ

                initialLevel.level_MY_SHOPS.updateLevel(Users, MY_SHOPS.name(), initialLevel.level_SEARCH_RESULT_PRODUCT, true);

                levelRepository.cache(initialLevel.level_MY_SHOPS);
                Message message001_1 = new Message(initialLevel.level_MY_SHOPS, Map.of(RU, "Список моих магазинов"));
                messageRepository.cache(message001_1);
                initialLevel.level_MY_SHOPS.addMessage(message001_1);
                ButtonRow row001_0 = new ButtonRow(initialLevel.level_MY_SHOPS);
                buttonRowRepository.cache(row001_0);
                Button button001_0_0 = new Button(row001_0, Map.of(RU, "Рекомендовать/ запрос кэшбека"), initialLevel.level_CONNECT.getIdString());
                buttonRepository.cache(button001_0_0);
                row001_0.add(button001_0_0);
//                Button button001_0_1 = new Button(initialLevel.level_CONSTRUCT_ADD, "Мои боты", initialLevel.level_SHOP_BOTS.getIdString());
//                buttonRepository.cache(button001_0_1);
//                row001_0.add(button001_0_1);
                initialLevel.level_MY_SHOPS.addRow(row001_0);


                //////////ПОИСК ТОВАРА ПО КАТЕГОРИЯМ

                initialLevel.level_SEARCH.updateLevel(Users, SEARCH.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_SEARCH);
                Message message15_1 = new Message(initialLevel.level_SEARCH, Map.of(RU, "Введите название для поиска:"));
                messageRepository.cache(message15_1);
                initialLevel.level_SEARCH.addMessage(message15_1);

                menuCreator.createMenu(initialLevel.level_SEARCH, MenuTypeEnum.SEARCH_LEVEL_CHOICER, Users);

                initialLevel.level_SEARCH_RESULT.updateLevel(Users, SEARCH_RESULT.name(), initialLevel.level_SEARCH, false);

                levelRepository.cache(initialLevel.level_SEARCH_RESULT);
                Message message52_1_1 = new Message(initialLevel.level_SEARCH_RESULT, Map.of(RU, "Результат поиска"));
                messageRepository.cache(message52_1_1);
                initialLevel.level_SEARCH_RESULT.addMessage(message52_1_1);


                initialLevel.level_SEARCH_RESULT_PRODUCT.updateLevel(Users, SEARCH_RESULT_PRODUCT.name(), initialLevel.level_SEARCH_RESULT, false);

                levelRepository.cache(initialLevel.level_SEARCH_RESULT_PRODUCT);
                Message message521_1_1 = new Message(initialLevel.level_SEARCH_RESULT_PRODUCT, Map.of(RU, "Подробнее: "));
                messageRepository.cache(message521_1_1);
                initialLevel.level_SEARCH_RESULT_PRODUCT.addMessage(message521_1_1);


                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ BK, BM, CB -И ПОЛЛУЧАТЕЛь PI

                initialLevel.level_PSHARE2P.updateLevel(Users, PSHARE2P.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_PSHARE2P);
                Message message19_1 = new Message(initialLevel.level_PSHARE2P, Map.of(RU, "Вы получили рекомендацию от X"));
                messageRepository.cache(message19_1);
                initialLevel.level_PSHARE2P.addMessage(message19_1);

                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ PI -И ПОЛЛУЧАТЕЛЯ PI НЕТ ЕЩЕ В СИСТЕМЕ

                initialLevel.level_P2NOP.updateLevel(Users, P2NOP.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_P2NOP);
                Message message20_1 = new Message(initialLevel.level_P2NOP, Map.of(RU, "Добро пожаловать в систему Skidozona!"));
                messageRepository.cache(message20_1);
                initialLevel.level_P2NOP.addMessage(message20_1);


                initialLevel.level_P2NOP_RESP.updateLevel(Users, P2NOP_RESP.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_P2NOP_RESP);
                Message message20_1_1 = new Message(initialLevel.level_P2NOP_RESP, Map.of(RU, "Пользователю Y успешно добавлен в систему!"));
                messageRepository.cache(message20_1_1);
                initialLevel.level_P2NOP_RESP.addMessage(message20_1_1);


                //////////СООБЩЕНИЕ, КОТОРОЕ ПРИХОДИТ ПОСЛЕ ПОЛУЧЕНИЯ PI -И ПОЛЛУЧАТЕЛь PI УЖЕ ЕСТЬ В СИСТЕМЕ

                initialLevel.level_P2P.updateLevel(Users, P2P.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_P2P);
                Message message21_1 = new Message(initialLevel.level_P2P, Map.of(RU, "Пользователь X пытался добавить Вас в систему"));
                messageRepository.cache(message21_1);
                initialLevel.level_P2P.addMessage(message21_1);


                initialLevel.level_P2P_RESP.updateLevel(Users, P2P_RESP.name(), initialLevel.level_INITIALIZE, false);

                levelRepository.cache(initialLevel.level_P2P_RESP);
                Message message21_1_1 = new Message(initialLevel.level_P2P_RESP, Map.of(RU, "Пользователь Y уже есть в системе"));
                messageRepository.cache(message21_1_1);
                initialLevel.level_P2P_RESP.addMessage(message21_1_1);

                System.out.println("PRE addFinalButton");

                initialLevel.addFinalButton(initialLevel.level_INITIALIZE, initialLevel.level_INITIALIZE);

                System.out.println("POST addFinalButton");

            } catch (IOException | WriterException e) {
                e.printStackTrace();
            }
    }
}
