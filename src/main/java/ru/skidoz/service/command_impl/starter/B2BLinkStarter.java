package ru.skidoz.service.command_impl.starter;

import static ru.skidoz.model.entity.category.LanguageEnum.RU;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.skidoz.aop.repo.LinkStarterKeeperCacheRepository;
import ru.skidoz.aop.repo.ShopGroupAddVoteCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.service.initializers.InitialLevel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author andrey.semenov
 */
@Component
public class B2BLinkStarter extends LinkStarter {

    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private ShopGroupAddVoteCacheRepository shopGroupAddVoteCacheRepository;
    @Autowired
    private LinkStarterKeeperCacheRepository linkStarterKeeperCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private InitialLevel initialLevel;


    @Override
    public List<LevelChat> getLevel(User user, String callback) {

        final int secretId = Integer.parseInt(callback);
        final int addingShopId = user.getCurrentAdminShop();

        LinkStarterKeeper linkStarterKeeper = linkStarterKeeperCacheRepository.findBySecretCode(secretId);

        final Integer shopGroupId = linkStarterKeeper.getParameter1();
        final Integer initiatorShopId = linkStarterKeeper.getParameter2();

        ShopGroupAddVote vote = new ShopGroupAddVote(e -> {
            e.setAddingShop(addingShopId);
            e.setApproved(false);
            e.setShopGroup(shopGroupId);
            e.setAddingInitiatorShop(initiatorShopId);
        });
        shopGroupAddVoteCacheRepository.save(vote);

        final ShopGroup shopGroup = shopGroupCacheRepository.findById(shopGroupId);

        Shop initiatorShop = shopCacheRepository.findById(initiatorShopId);
        final User userInitiator = userCacheRepository.findById(linkStarterKeeper.getParameter3());

        List<LevelChat> levelChatDTOList = new ArrayList<>(storeRecommendation(user, userInitiator, null));

        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_ADMIN_SHOPS,
                false,
                false);
        Shop addingShop = shopCacheRepository.findById(addingShopId);


        Message message0_1 = new Message(initialLevel.level_INITIALIZE,
                Map.of(RU, initiatorShop.getName() + " предлагает Вам добавить " + addingShop.getName()
                        + " в группу взаимозачетов " + shopGroup.getName() + "."));
        resultLevel.addMessage(message0_1);

        ButtonRow row = new ButtonRow();
        Button button0 = new Button(row, Map.of(LanguageEnum.RU, "Подтвердить добавление в группу "),
                initialLevel.level_ADD_SHOP_TO_SHOP_GROUP.getIdString() + vote.getSecretCode());
        row.add(button0);
        Button button1 = new Button(row, Map.of(LanguageEnum.RU, "Отмена"), initialLevel.level_SELLERS_REMOVE_DISMISS.getIdString());
        row.add(button1);
        resultLevel.addRow(row);

        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(user.getChatId());
            e.setLevel(resultLevel);
        }));
        return levelChatDTOList;
    }
}
