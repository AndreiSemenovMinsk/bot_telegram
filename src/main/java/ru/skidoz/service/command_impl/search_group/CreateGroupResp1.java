package ru.skidoz.service.command_impl.search_group;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class CreateGroupResp1 implements Command {

    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users)
            throws IOException, WriterException {

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(
                initialLevel.level_CREATE_GROUP_RESP,
                false,
                false);

        String inputText = update.getMessage().getText();

        System.out.println();
        System.out.println();
        System.out.println("*****************************ConstructAdd**********" + inputText);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("ConstructAdd**********" + inputText);
        System.out.println();

        if (shopGroupCacheRepository.findByName(inputText) == null) {

            ShopGroup shopGroup = new ShopGroup();
            shopGroup.setName(inputText);
            shopGroupCacheRepository.save(shopGroup);

            PartnerGroup partnerGroup = new PartnerGroup();
            partnerGroup.setShopGroup(shopGroup.getId());
            partnerGroup.setName(inputText);
            partnerGroupCacheRepository.save(partnerGroup);

            users.setCurrentConstructShopGroup(shopGroup.getId());
            userCacheRepository.save(users);

            resultLevel.addMessage(new Message(
                    null,
                    Map.of(LanguageEnum.RU, "Зарегистрирована новая группа " + inputText +
                            "! Введите лимит долга по группе текстом в сообщении:")));
        } else {
            resultLevel.addMessage(new Message(
                    null,
                    Map.of(LanguageEnum.RU, "Такая группа уже существует!")));
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
