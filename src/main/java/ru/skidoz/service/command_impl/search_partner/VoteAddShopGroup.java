package ru.skidoz.service.command_impl.search_partner;

import com.google.zxing.WriterException;
import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.ShopGroupAddVoteCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.ShopGroup;
import ru.skidoz.model.pojo.telegram.ShopGroupAddVote;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.TelegramBotWebhook;
import ru.skidoz.service.command.Command;
import ru.skidoz.service.initializers.InitialLevel;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class VoteAddShopGroup implements Command {


    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private ShopGroupAddVoteCacheRepository shopGroupAddVoteCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users)
            throws IOException, WriterException {

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(
                initialLevel.level_ADD_SHOP_TO_SHOP_GROUP,
                false,
                false);

        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");

        System.out.println();
        System.out.println();
        System.out.println("*****************************AddShopGroup**********" + inputText);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("AddShopGroup**********" + inputText);
        System.out.println();

        try {
            final int secretCode = Integer.parseInt(inputText);

            final Integer currentAdminShop = users.getCurrentAdminShop();

            final ShopGroupAddVote shopGroupAddVote = shopGroupAddVoteCacheRepository.findBySecretCodeAndAddingShop_Id(
                    secretCode,
                    currentAdminShop);
            final Integer shopGroupId = shopGroupAddVote.getShopGroup();
            final ShopGroup shopGroup = shopGroupCacheRepository.findById(shopGroupId);

            if (shopGroupAddVote != null) {

                final Shop shop = shopCacheRepository.findById(currentAdminShop);
                final Shop shopAdding = shopCacheRepository.findById(shopGroupAddVote.getAddingShop());

                resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Вы добавили в группу взаимозачета " + shopGroup.getName()
                + " магазин " + shopAdding.getName())));

                shopGroupAddVote.setApproved(true);
                shopGroupAddVoteCacheRepository.save(shopGroupAddVote);


                final List<ShopGroupAddVote> allByShopGroupAndAddingShop = shopGroupAddVoteCacheRepository.findAllByShopGroupAndAddingShop(
                        shopGroup.getId(),
                        shopGroupAddVote.getAddingShop());
                final long count = allByShopGroupAndAddingShop.stream().filter(e -> !e.isApproved()).count();
                if (count == 0) {

                    final List<Integer> shopSet = shopGroup.getShopSet();
                    shopSet.add(shopAdding.getId());
                    shopGroup.setShopSet(shopSet);
                    shopGroupCacheRepository.save(shopGroup);

                    Level levelPartner = new Level();
                    Message message = new Message(levelPartner, Map.of(LanguageEnum.RU,
                            "Магазин " + shop.getName()  + " добавлен в группу взаимозачета " + shopGroup.getName()));
                    level.addMessage(message);
                }
            } else {
                resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Группа для добавления не найдена!")));
            }

        } catch (NumberFormatException formatException) {

            resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Необходимо вводить только числовое значение!")));
        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new LevelResponse(
                Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(users.getChatId());
                    e.setUser(users);
                    e.setLevel(finalResultLevel);
                })), null, null);
    }
}
