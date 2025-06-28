package ru.skidoz.service.command_impl.search_group;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
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
public class SearchGroupResp1 implements Command {

    @Autowired
    private ShopCacheRepository shopRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        //Level resultLevel = initialLevel.level_SEARCH_PARTNER_RESP;
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_GROUP_RESP,
                true,
                false);
        String inputText = update.getMessage().getText();
        List<Shop> shopList = shopRepository.findAllByNameLikeAndActiveIsTrue(inputText);
        Long chatId = users.getChatId();

        List<PartnerGroup> partnerList = partnerGroupCacheRepository.findAll(); //.findAllByCreditor_Id(shopInitiator);

                for (PartnerGroup partnerGroup : partnerList) {

                    ShopGroup shopGroup = shopGroupCacheRepository.findById(partnerGroup.getDebtor());
                    String shopNames = shopGroup.getShopSet().stream().map(e -> e.getName().substring(0, 5)).collect(Collectors.joining(", "));

                    for (Shop shop : shopList) {
                        ButtonRow row = new ButtonRow();
                        String nameButton;
                        if (shopGroup.getShopSet().contains(shop)) {
                            nameButton = "Редактировать " + shopNames;
                        } else {
                            nameButton = "Добавить " + shopNames;
                        }
                        Button button = new Button(row, Map.of(LanguageEnum.RU, nameButton), initialLevel.level_SEARCH_GROUP_RESP.getIdString() + partnerGroup.getId());
                        row.add(button);
                        resultLevel.addRow(row);
                    }
                }

        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(resultLevel);
        })), null, null);
    }
}
