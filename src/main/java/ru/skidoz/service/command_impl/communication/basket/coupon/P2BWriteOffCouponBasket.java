package ru.skidoz.service.command_impl.communication.basket.coupon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.main.Action;
import ru.skidoz.model.pojo.side.Basket;
import ru.skidoz.model.pojo.side.BasketProduct;
import ru.skidoz.model.pojo.side.Product;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.aop.repo.*;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.skidoz.util.ProductChecker.checkProductSource;


/**
 * @author andrey.semenov
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class P2BWriteOffCouponBasket implements Command {

    @Autowired
    private BasketCacheRepository basketCacheRepository;
    @Autowired
    private BasketProductCacheRepository basketProductCacheRepository;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private CashbackWriteOffCacheRepository cashbackWriteOffCacheRepository;
    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;
    @Autowired
    private RecommendationCacheRepository recommendationCacheRepository;
    @Autowired
    private InitialLevel initialLevel;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws IOException, WriterException {

        System.out.println();
        System.out.println("+++++++++++++++++++++P2BWriteOffCouponBasket++++++++++++++++++++");
        System.out.println();
        System.out.println();


        List<LevelChat> levelChatDTOList = new ArrayList<>();
        LevelDTOWrapper resultLevel = null;
        Long shopChatId = users.getChatId();

        try {
            Shop shopInitiator = shopCacheRepository.findBySellerChatId(shopChatId);
            User buyer = userCacheRepository.findByChatId(shopInitiator.getCurrentConversationShopUserChatId());

            resultLevel = initialLevel.convertToLevel(initialLevel.level_P2B_WRITEOFF_COUPON_SELECT_ACTION,
                    false,
                    false);

            List<Action> actionCouponList = actionCacheRepository.findAllByShopAndTypeAndActiveIsTrue(shopInitiator.getId(), ActionTypeEnum.COUPON);
            Action actionCoupon = actionCacheRepository.findFirstByShopAndTypeAndActiveIsTrue(shopInitiator.getId(), ActionTypeEnum.COUPON_DEFAULT);
            actionCouponList.add(actionCoupon);
            List<Basket> basketList = basketCacheRepository.findAllByUserIdAndShopIdAndTemp(buyer.getId(), shopInitiator.getId(), true);

            basketList.forEach(System.out::println);

            System.out.println("basketList.size+++" + basketList.size());

            Map<Action, List<BasketProduct>> actionMap = new HashMap();

            for (Basket basket : basketList) {

                System.out.println("basket+++++" + basket);

                List<BasketProduct> basketProducts = basketProductCacheRepository.findAllByBasketId(basket.getId());

                for (BasketProduct basketProduct : basketProducts) {

                    System.out.println("basketProduct+++" + basketProduct);
                    Product product = productCacheRepository.findById(basketProduct.getProduct());

                    for (Action action : actionCouponList) {

                        if (checkProductSource(action, product)) {

                            if (!actionMap.containsKey(action)) {
                                actionMap.put(action, new ArrayList<>(List.of(basketProduct)));
                            } else {
                                actionMap.get(action).add(basketProduct);
                            }
                        }
                    }
                }
            }

            for (Map.Entry<Action, List<BasketProduct>> entry : actionMap.entrySet()) {

                Action action = entry.getKey();
                String prodDescr = entry.getValue().stream().map(basketProduct ->
                        productCacheRepository.findById(basketProduct.getProduct()).getName(users.getLanguage()) + " " + basketProduct.getProductAmount())
                        .collect(Collectors.joining(", "));

                ButtonRow row = new ButtonRow();
                Button approveButton = new Button(row, Map.of(users.getLanguage(), action.getName() + " " + prodDescr), initialLevel.level_P2B_WRITEOFF_COUPON_REQUEST.getIdString() + action.getId());
                row.add(approveButton);
                resultLevel.addRow(row);
            }

            basketList.forEach(basket -> basketCacheRepository.delete(basket));

        } catch (Exception e) {
            e.printStackTrace();
        }


        //TODO - изменить отдачу
        LevelDTOWrapper finalResultLevel1 = resultLevel;
        levelChatDTOList.add(new LevelChat(e -> {
            e.setChatId(shopChatId);
            e.setUser(users);
            e.setLevel(finalResultLevel1);
        }));
        return new LevelResponse(levelChatDTOList, null, null);
    }
}
