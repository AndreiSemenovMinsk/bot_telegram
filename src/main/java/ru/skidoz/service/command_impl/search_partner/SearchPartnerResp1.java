package ru.skidoz.service.command_impl.search_partner;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.PartnerCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
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
public class SearchPartnerResp1 implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private PartnerCacheRepository partnerRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {
        // перешли с кнопки, а потом нажимаем на кнопку выбора
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_PARTNER_RESP,
                true,
                false);
        String inputText = update.getMessage().getText();
        List<Shop> shopList = shopCacheRepository.findAllByNameLikeAndActiveIsTrue(inputText);
        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

        System.out.println();
        System.out.println("*****************SearchPartnerResp1****************");
        System.out.println();
        System.out.println("inputText*" + inputText);
        System.out.println("shopList**********" + shopList.size());
        System.out.println();
        System.out.println();

        List<Partner> partnerList = partnerRepository.findAllByCreditor_Id(shopInitiator.getId());

        for (Shop shop : shopList){
            ButtonRow row = new ButtonRow();
            String nameButton;

            System.out.println("+++++" + shop.getId());

            if (partnerList.stream().map(Partner::getDebtor).anyMatch(o -> shop.getId().equals(o))){
                nameButton = "Редактировать " + shop.getName();
            } else {
                nameButton = "Добавить " + shop.getName();
            }
            Button button = new Button(row, Map.of(LanguageEnum.RU, nameButton), initialLevel.level_SEARCH_PARTNER_RATE.getIdString() + shop.getId());
            row.add(button);
            resultLevel.addRow(row);
        }

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
