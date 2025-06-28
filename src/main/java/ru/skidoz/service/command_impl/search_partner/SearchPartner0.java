package ru.skidoz.service.command_impl.search_partner;

import java.util.ArrayList;
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
public class SearchPartner0 implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private PartnerCacheRepository partnerCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        // перешли с кнопки, а потом нажимаем на кнопку выбора
        //Level resultLevel = initialLevel.level_SEARCH_PARTNER;
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_PARTNER,
                false,
                false);
        List<Shop> shopSet = shopCacheRepository.findAllByActiveIsTrue();
        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

        System.out.println();
        System.out.println("*****************SearchPartner0****************");
        System.out.println();
        System.out.println();
        System.out.println();

        List<Partner> partnerList = partnerCacheRepository.findAllByCreditor_Id(shopInitiator.getId());
        List<ButtonRow> buttonRowList = new ArrayList<>();

        System.out.println(shopSet);

        for (Shop shop : shopSet) {

            System.out.println("shop+++" + shop);

            if (!shopInitiator.getId().equals(shop.getId()) && !InitialLevel.SHOP.getId().equals(shop.getId())) {

                ButtonRow row = new ButtonRow();
                String nameButton;
                if (partnerList.stream().map(Partner::getDebtor).noneMatch(o -> shop.getId().equals(o))) {
                    nameButton = "Добавить " + shop.getName();
                    Button button = new Button(row, Map.of(LanguageEnum.RU, nameButton), initialLevel.level_SEARCH_PARTNER_RESP_BUTTON.getIdString() + shop.getId());
                    row.add(button);

                    buttonRowList.add(row);
                }
            }
        }

        System.out.println("buttonRowList.size()---" + buttonRowList.size());

        for (ButtonRow row : buttonRowList) {
            resultLevel.addRow(row);
        }

        List<LevelChat> levelChatDTOList = new ArrayList<>();
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        }));
        return new LevelResponse(levelChatDTOList, null, null);
    }
}
