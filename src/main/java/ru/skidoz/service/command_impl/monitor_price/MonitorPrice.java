package ru.skidoz.service.command_impl.monitor_price;

import java.util.Collections;
import java.util.Map;

import ru.skidoz.aop.repo.RemotedPriceCacheRepository;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.side.RemotedPrice;
import ru.skidoz.model.pojo.telegram.*;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.service.command.Command;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author andrey.semenov
 */
@Component
public class MonitorPrice implements Command {

    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    public RemotedPriceCacheRepository remotedPriceRepository;

    @Override
    public LevelResponse runCommand(Update update, Level level, User users) throws CloneNotSupportedException {

        System.out.println();
        System.out.println("********************RemotedPrice******************" + level.getCallName());
        System.out.println();
        System.out.println();

        //Level resultLevel = initialLevel.level_MULTI_LEVEL_RATE;
        LevelDTOWrapper resultLevel = initialLevel.convertToLevel(initialLevel.level_MONITOR_PRICE,
                false,
                false);

        Long chatId = users.getChatId();

        if (update.getMessage() != null) {

            String search = update.getMessage().getText();
            System.out.println("MultiActionLevel***" + search);

            try {
                Integer shopId = 0;
                String id4 = null;
                //Integer price = Integer.MAX_VALUE;
                String priceString = null;
                if (search.contains("lamoda")) {

                    int ind4 = search.indexOf("/p/");
                    String sear4 = search.substring(ind4 + 3, ind4 + 30);
                    int byn4 = sear4.indexOf("/");
                    id4 = sear4.substring(0, byn4).toUpperCase();
                    shopId = 2;


                    HttpResponse<String> response3 = Unirest.get("https://www.lamoda.by/api/v1/product/get?sku=" + id4 + "&is_hybrid_supported=false")
                            .header("Cookie", "lid=ZEADrWS30VwVJVoiONnWAgA=")
                            .asString();

                    int ind3 = response3.getBody().indexOf("\"price\":");
                    String sear3 = response3.getBody().substring(ind3 + 8, ind3 + 100);
                    int byn3 = sear3.indexOf(",");

                    System.out.println(sear3.substring(0, byn3));

                    //price = Integer.parseInt(sear3.substring(0, byn3));
                    priceString = sear3.substring(0, byn3);

                } else if (search.contains("wildberries")) {
                    int ind4 = search.indexOf("?card=");
                    String sear4 = search.substring(ind4 + 6, ind4 + 20);
                    int byn4 = sear4.indexOf("&");
                    id4 = sear4.substring(0, byn4).toUpperCase();
                    shopId = 3;

                    System.out.println("https://card.wb.ru/cards/detail?nm=" + id4 + "&appType=128&curr=byn&lang=ru&dest=-59208&spp=0");

                    HttpResponse<String> response = Unirest.get("https://card.wb.ru/cards/detail?nm=" + id4 + "&appType=128&curr=byn&lang=ru&dest=-59208&spp=0").asString();
                    int ind = response.getBody().indexOf("salePriceU");
                    String sear = response.getBody().substring(ind + 12, ind + 100);
                    int byn = sear.indexOf(",");

                    priceString = sear.substring(0, byn);

                    System.out.println("priceString-----------------" + priceString);

                    String mantissa = priceString.substring(priceString.length() - 2, priceString.length() - 1);
                    String num = priceString.substring(0, priceString.length() - 2);
                    priceString = num + "," + mantissa;

                    System.out.println("priceString+++++++++++++++++" + priceString);

                    //price = Integer.parseInt(sear.substring(0, byn - 2));
                } else if (search.contains("ozon")) {

                    int ind4 = search.indexOf("product/");
                    String sear4 = search.substring(ind4 + 8);

                    System.out.println(sear4);

                    int byn4 = sear4.indexOf("/?");

                    System.out.println("byn4***********************" + byn4);

                    id4 = sear4.substring(0, byn4).toUpperCase();
                    shopId = 4;

                    System.out.println("id4------------------------" + id4);

                    System.out.println("https://ozon.by/api/entrypoint-api.bx/page/json/v2?url=%2Fproduct%2F" + id4);


                    HttpResponse<String> response2 = Unirest.get("https://ozon.by/api/entrypoint-api.bx/page/json/v2?url=%2Fproduct%2F" + id4)
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

                    System.out.println("@@@@@@@@@@@" + sear2.substring(0, byn2 + 1));
                    //price = Integer.parseInt(sear2.substring(0, byn2 - 1));
                    priceString = sear2.substring(0, byn2 + 3);
                }

                System.out.println("shopId--------" + shopId);
                System.out.println("id4-----------" + id4);
                System.out.println("RemotedPrice--" + chatId.toString() + "@" + priceString);

                if (id4 != null &&  shopId > 0) {

                    RemotedPrice rp = new RemotedPrice();
                    rp.setId(chatId.toString() + "@");
                    rp.setProductId(id4);
                    rp.setShop(shopId);
                    rp.setChatId(chatId);
                    rp.setBidPrice(-1);
                    rp.setUrl(search);

                    remotedPriceRepository.save(rp);

                    resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU, "Товар успешно найден. Текущая цена " + priceString + " руб.")));
                    resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU, "Пришлите целевую цену, при достижении которой будет отправлено уведомление")));
                }

            } catch (Exception exception) {

                resultLevel = initialLevel.convertToLevel(initialLevel.level_MONITOR_PRICE,
                        true,
                        false);

                resultLevel.addMessage(new Message(null, Map.of(LanguageEnum.RU, "Необходимо вводить ссылки на товар!")));

                System.out.println("exception***********************" + exception);
            }
        }

        LevelDTOWrapper finalResultLevel = resultLevel;
        return new LevelResponse(Collections.singletonList(new LevelChat(e -> {
            e.setChatId(chatId);
            e.setUser(users);
            e.setLevel(finalResultLevel);
        })), null, null);
    }
}
