package ru.skidoz.service.command_impl.search_group;

import java.util.Collections;
import java.util.Map;

import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * @author andrey.semenov
 */
@Component
public class CreateGroupResp2 implements Command {

    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users)
            throws CloneNotSupportedException {

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(
                initialLevel.level_CREATE_GROUP_LIMIT,
                false,
                false);

        Long chatId = users.getChatId();
        final int currentConstructShopGroup = users.getCurrentConstructShopGroup();


        String inputText = update.getMessage().getText().replaceAll("[^0-9]", "");

        try {
            final ShopGroup shopGroup = shopGroupCacheRepository.findById(currentConstructShopGroup);

            int limit = Integer.parseInt(inputText);
            shopGroup.setLimitSum(Integer.parseInt(inputText));

            resultLevel.addMessage(new Message(
                    null,
                    Map.of(LanguageEnum.RU, "Вы установили лимит " + limit + " на группу " + shopGroup.getName() + "!")));
            shopGroupCacheRepository.save(shopGroup);

        } catch (NumberFormatException formatException) {
            resultLevel.addMessage(new Message(
                    null,
                    Map.of(LanguageEnum.RU, "Необходимо вводить только числовое значение!")));
        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new LevelResponse(
                Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(chatId);
                    e.setUser(users);
                    e.setLevel(finalResultLevel);
                })), null, null);
    }
}
