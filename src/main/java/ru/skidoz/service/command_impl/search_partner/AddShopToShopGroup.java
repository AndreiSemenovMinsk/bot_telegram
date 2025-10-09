package ru.skidoz.service.command_impl.search_partner;

import com.google.zxing.WriterException;
import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.ShopGroupAddVoteCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.ButtonRow;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class AddShopToShopGroup implements Command {

    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private ShopGroupAddVoteCacheRepository shopGroupAddVoteCacheRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    public TelegramBotWebhook telegramBot;

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

            final ShopGroupAddVote shopGroupAddVote = shopGroupAddVoteCacheRepository.findBySecretIdAndAddingShop_Id(
                    secretCode,
                    currentAdminShop);
            final Integer shopGroupId = shopGroupAddVote.getShopGroup();
            final ShopGroup shopGroup = shopGroupCacheRepository.findById(shopGroupId);

            if (shopGroupAddVote != null) {


                final Shop shop = shopCacheRepository.findById(currentAdminShop);

                resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU,"Вы добавляетесь в группу взаимозачета " + shopGroup.getName())));

                shopGroupAddVote.setApproved(true);
                shopGroupAddVoteCacheRepository.save(shopGroupAddVote);

                for (Integer shopPartnerId : shopGroup.getShopSet()) {

                    ShopGroupAddVote vote = new ShopGroupAddVote(e -> {
                        e.setAddingShop(shopPartnerId);
                        e.setApproved(false);
                        e.setShopGroup(shopGroupId);
                        e.setAddingInitiatorShop(shopGroupAddVote.getAddingInitiatorShop());
                    });
                    shopGroupAddVoteCacheRepository.save(vote);

                    final Shop shopPartner = shopCacheRepository.findById(shopPartnerId);
                    User userPartner = userCacheRepository.findById(shopPartner.getAdminUser());
                    
                    Level levelPartner = new Level();
                    Message message = new Message(levelPartner, Map.of(LanguageEnum.RU,
                            "Магазин " + shop.getName()  + " запрашивает добавление в груар                                                                                                                                        пу взаимозачета " + shopGroup.getName()));
                    level.addMessage(message);



                    ButtonRow row = new ButtonRow();
                    Button button0 = new Button(row, Map.of(LanguageEnum.RU, "Подтвердить добавление в группу "),
                            initialLevel.level_VOTE_ADD_SHOP_GROUP.getIdString() + vote.getSecretCode());
                    row.add(button0);
                    Button button1 = new Button(row, Map.of(LanguageEnum.RU, "Отмена"), initialLevel.level_SELLERS_REMOVE_DISMISS.getIdString());
                    row.add(button1);
                    resultLevel.addRow(row);

                    final LevelDTOWrapper levelWrapper = new LevelDTOWrapper();
                    levelWrapper.setLevel(levelPartner);

                    final String runner = shopCacheRepository.findById(userPartner.getFirstRunnerShop()).getSecretId();
                    telegramBot.addAsync(
                            new LevelResponse(
                                    new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
                                        e.setLevel(levelWrapper);
                                        e.setChatId(userPartner.getChatId());
                                    }))), null, runner));
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
