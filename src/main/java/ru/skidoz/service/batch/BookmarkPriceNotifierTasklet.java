package ru.skidoz.service.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


import ru.skidoz.aop.repo.BookmarkCacheRepository;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.TelegramBotWebhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class BookmarkPriceNotifierTasklet implements Tasklet {

    @Autowired
    public TelegramBotWebhook telegramBot;
    @Autowired
    public BookmarkCacheRepository bookmarkRepository;
    @Autowired
    public ProductCacheRepository productRepository;
    @Autowired
    public UserCacheRepository userRepository;
    @Autowired
    public ShopCacheRepository shopRepository;

    @Override
    public void execute() {

        System.out.println(
                "+++++++++++++++++++++++++++++++++++++++++++BookmarkPriceNotifierTasklet+++++++++++++++++++++++++++++++++++");

        bookmarkRepository.findAllByPriceStrikeAndPriceUpdated(true, false).forEach(bookmark -> {
            Product prd = productRepository.findById(bookmark.getProduct());

            Level level = new Level();
            Message message = new Message(level,
                    Map.of(
                            LanguageEnum.RU,
                            "Цена по закладке "
                                    + prd.getAlias()
                                    + " достигла цели "
                                    + prd.getPrice()));
            level.addMessage(message);
            final LevelDTOWrapper levelDTOWrapper = new LevelDTOWrapper();
            levelDTOWrapper.setLevel(level);

            User user = userRepository.findById(bookmark.getUser());

            final String runner = shopRepository.findById(user.getFirstRunnerShop()).getSecretId();
            telegramBot.addAsync(
                    new LevelResponse(
                            new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
                                e.setLevel(levelDTOWrapper); //убрать так же в оригинальном методе
                                e.setChatId(user.getChatId());
                            }))), null, runner));

            bookmark.setPriceUpdated(true);
            bookmarkRepository.save(bookmark);
        });


        System.out.println(
                "+++++++++++++++++++++++++++++++++++++++++++BookmarkPriceNotifierTasklet finish+++++++++++++++++++++++++++++++++++");
    }
}
