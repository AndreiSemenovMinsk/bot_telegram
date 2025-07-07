package ru.skidoz.service.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


import ru.skidoz.aop.repo.RemotedPriceCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;

import ru.skidoz.model.entity.telegram.UserEntity;
import ru.skidoz.model.pojo.side.RemotedPrice;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.repository.telegram.UserRepository;
import ru.skidoz.service.TelegramBot;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import ru.skidoz.service.TelegramBotWebhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Users on 30.05.2020.
 */
@Component
public class MonitorPriceTasklet implements Tasklet {

    @Autowired
    public TelegramBotWebhook telegramBot;
    @Autowired
    public RemotedPriceCacheRepository remotedPriceRepository;
    @Autowired
    public UserCacheRepository userRepository;
    @Autowired
    public ShopCacheRepository shopRepository;

    @Override
    public void execute() {

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++MonitorPriceTasklet+++++++++++++++++++++++++++++++++++");

        List<RemotedPrice> bookmarks = new ArrayList<>();

        Unirest.setTimeouts(0, 0);

        remotedPriceRepository.findAll().forEach(bookmark -> {
            String prd = bookmark.getProductId();
            try {
                int price = Integer.MAX_VALUE;
                boolean monitoring = false;

                if (bookmark.getShop().equals(2L)) {

                    System.out.println("https://www.lamoda.by/api/v1/product/get?sku=" + prd + "&is_hybrid_supported=false");

                    HttpResponse<String> response3 = Unirest.get("https://www.lamoda.by/api/v1/product/get?sku=" + prd + "&is_hybrid_supported=false")
                            .header("Cookie", "lid=ZEADrWS30VwVJVoiONnWAgA=")
                            .asString();

                    int ind3 = response3.getBody().indexOf("\"price\":");
                    String sear3 = response3.getBody().substring(ind3 + 8, ind3 + 100);
                    int byn3 = sear3.indexOf(",");

                    System.out.println(sear3.substring(0, byn3 - 1));

                    price = Integer.parseInt(sear3.substring(0, byn3));

                    monitoring = true;
                } else if (bookmark.getShop().equals(3L)) {

                    System.out.println("https://card.wb.ru/cards/detail?nm=" + prd + "&appType=128&curr=byn&lang=ru&dest=-59208&spp=0");

                    HttpResponse<String> response = Unirest.get("https://card.wb.ru/cards/detail?nm=" + prd + "&appType=128&curr=byn&lang=ru&dest=-59208&spp=0").asString();
                    int ind = response.getBody().indexOf("salePriceU");
                    String sear = response.getBody().substring(ind + 12, ind + 100);
                    int byn = sear.indexOf(",");

                    System.out.println(sear.substring(0, byn - 1));

                    price = Integer.parseInt(sear.substring(0, byn - 2));

                    monitoring = true;
                } else if (bookmark.getShop().equals(4L)) {

                    System.out.println("https://ozon.by/api/entrypoint-api.bx/page/json/v2?url=%2Fproduct%2F" + prd);

                    HttpResponse<String> response2 = Unirest.get("https://ozon.by/api/entrypoint-api.bx/page/json/v2?url=%2Fproduct%2F" + prd)
                            .header("Cookie", "__Secure-ab-group=14; __Secure-access-token=4.0.nUDoJUKoQAq6O87bS9YipQ.14.Ab9ezpAsIjwf" +
                                    "qkhrP357cISH0XAeCiIJG10Oz0xHtOUVDdaicpTrXX5aOpEnB_iqMQ..20240207120229.4HmEe6Em03Z368KUyLhczA9RQHFan6w_ndzVqH" +
                                    "xF72o; __Secure-refresh-token=4.0.nUDoJUKoQAq6O87bS9YipQ.14.Ab9ezpAsIjwfqkhrP357cISH0XAeCiIJG10Oz0xHtOUVDdaic" +
                                    "pTrXX5aOpEnB_iqMQ..20240207120229.Y7nqGqZK5MA6Agh8K66s_kvYFQOOeJs-rbrCwvnH_6E; __Secure-user-id=0; abt_data=c" +
                                    "b8b9af9ee846aac642b5a2e47d1496b:149ed87378771d7a8c16fab4a46082ce99b2a9d3d32ffd7585c87547d64da9f13a42e5fc75a24" +
                                    "dc36452be8345149337da7b2a0e2edce5de80bef89f6bcb8af825521d06ea4a61aba0183de8f504c8acac163cae91c8efa72b6f8b08cb" +
                                    "b7d955e2123d5da9f0d5105e5360eedda02927d34e452f41526f4ac0630fe3d3e2af564a988012bed6d467d2e1c7d86eb9ffefc8bf585" +
                                    "bacefa6f3016a3292dd459424")
                            .asString();

                    int ind2 = response2.getBody().indexOf("\\\"price\\\":\\\"");
                    String sear2 = response2.getBody().substring(ind2 + 12, ind2 + 50);
                    int byn2 = sear2.indexOf(",");

                    System.out.println("@@@@@@@@@@@" + sear2.substring(0, byn2 - 1));

                    System.out.println(sear2.substring(0, byn2 - 1));

                    price = Integer.parseInt(sear2.substring(0, byn2 - 1));

                    monitoring = true;
                }

                System.out.println(price + " -price- " + monitoring + " bookmark--- " + bookmark);

                if (monitoring && price < bookmark.getBidPrice()) {

                    Level level = new Level ();
                    Message message = new Message (level, Map.of(LanguageEnum.RU, "Цена по закладке " + price + " достигла цели " + bookmark.getBidPrice()));
                    level.addMessage(message);
                    final LevelDTOWrapper levelDTOWrapper = new LevelDTOWrapper();
                    levelDTOWrapper.setLevel(level);

                    //System.out.println("127-------" + userRepository.findByChatId(bookmark.getChatId()));

                    final User byChatId = userRepository.findByChatId(bookmark.getChatId());


                    final String runner = shopRepository.findById(byChatId.getFirstRunnerShop()).getSecretId();
                    telegramBot.addAsync(
                            new LevelResponse(new ArrayList<>(Collections.singletonList(new LevelChat (e -> {
                        e.setLevel(levelDTOWrapper); //убрать так же в оригинальном методе
                        e.setChatId(bookmark.getChatId());
                    }))), null, runner));

                    remotedPriceRepository.delete(bookmark);
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        });

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++MonitorPriceTasklet finish+++++++++++++++++++++++++++++++++++");
    }
}
