package ru.skidoz.service.command_impl.search_group;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
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
public class SearchGroup0 implements Command {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_SEARCH_GROUP,
                false,
                false);
        List<PartnerGroup> shopList = partnerGroupRepository.findAll();
        Long chatId = users.getChatId();
        Shop shopInitiator = shopCacheRepository.findByChatId(chatId);

        List<PartnerGroup> partnerList = partnerGroupRepository.findAllByCreditor_Id(shopInitiator.getId());

        for (PartnerGroup partnerGroup : shopList){
            ButtonRow row = new ButtonRow();
            String nameButton;
            if (partnerList.contains(partnerGroup)){
                nameButton = "Редактировать " + partnerGroup.getName();
            } else {
                nameButton = "Добавить " + partnerGroup.getName();
            }
            Button button = new Button(row, Map.of(LanguageEnum.RU, nameButton), initialLevel.level_SEARCH_GROUP_RESP.getIdString() + partnerGroup.getId());
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
