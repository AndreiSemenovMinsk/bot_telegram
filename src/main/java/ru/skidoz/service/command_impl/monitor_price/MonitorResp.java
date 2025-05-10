package ru.skidoz.service.command_impl.monitor_price;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.BookmarkCacheRepository;
import ru.skidoz.model.pojo.side.Bookmark;
import ru.skidoz.model.pojo.side.RemotedPrice;
import ru.skidoz.aop.repo.RemotedPriceCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.service.InitialLevel;
import ru.skidoz.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.service.InitialLevel.bigCategory;
import static ru.skidoz.service.InitialLevel.bigCategoryGroup;
import static ru.skidoz.service.InitialLevel.bigCategorySuperGroup;


/**
 * @author andrey.semenov
 */
@Component
public class MonitorResp implements Command {

    @Autowired
    public RemotedPriceCacheRepository remotedPriceRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private BookmarkCacheRepository bookmarkCacheRepository;

    @Override
    public List<LevelChat> runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        System.out.println();
        System.out.println("********************MonitorResp******************" + level.getCallName());
        System.out.println();
        System.out.println();

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_MONITOR_RESP,
                true,
                false);

        Long chatId = users.getChatId();

        if (update.getMessage() != null) {

             String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");
            System.out.println("MultiActionLevel***" + inputText);
            try {

                System.out.println();

                RemotedPrice rm = remotedPriceRepository.findById(chatId + "@");

                remotedPriceRepository.delete(rm);

                rm.setId(chatId + "@" + rm.getProductId());
                rm.setBidPrice(Integer.parseInt(inputText));

                remotedPriceRepository.save(rm);

                Product product = new Product();
                product.setAlias(rm.getUrl());
                product.setChatId(users.getChatId());
                product.setShop(new Shop(rm.getShop()));
                product.setActive(true);
                product.setCategory(bigCategory.getId());
                product.setCategoryGroup(bigCategoryGroup.getId());
                product.setCategorySuperGroup(bigCategorySuperGroup.getId());
                product.setPrice(BigDecimal.valueOf(rm.getBidPrice()));
                productCacheRepository.save(product);

                Bookmark newBookmark = new Bookmark(e -> {
                    e.setRadius(50);
                    e.setProduct(product.getId());
                    e.setShop(product.getShop().getId());
                    e.setUser(users.getId());
                    e.setViberNotify(false);
                });

                bookmarkCacheRepository.save(newBookmark);

            } catch (NumberFormatException formatException) {

                resultLevel = initialLevel.convertToLevel(initialLevel.level_MONITOR_RESP,
                        true,
                        false);

                resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));
            }
        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })));
    }
}
