package ru.skidoz.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ru.skidoz.aop.repo.CashbackCacheRepository;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.ScheduleBuyerCacheRepository;
import ru.skidoz.aop.repo.CashbackWriteOffCacheRepository;
import ru.skidoz.aop.repo.BasketCacheRepository;
import ru.skidoz.aop.repo.BookmarkCacheRepository;
import ru.skidoz.aop.repo.PurchaseCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.aop.repo.RecommendationCacheRepository;
import ru.skidoz.model.pojo.side.Basket;
import ru.skidoz.model.pojo.side.Bookmark;
import ru.skidoz.model.pojo.side.Cashback;
import ru.skidoz.model.pojo.side.Shop;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.LevelResponse;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.service.command.Command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.skidoz.service.command.CommandProvider;
import ru.skidoz.service.command_impl.starter.BasketLinkStarter;
import ru.skidoz.service.command_impl.starter.BookmarkLinkStarter;
import ru.skidoz.service.command_impl.starter.CashbackLinkStarter;
import ru.skidoz.service.command_impl.starter.P2PExistBuyerLinkStarter;
import ru.skidoz.service.command_impl.starter.P2PNewBuyerLinkStarter;
import ru.skidoz.service.command_impl.starter.P2BLinkStarter;
import ru.skidoz.service.command_impl.starter.B2BLinkStarter;
import ru.skidoz.service.initializers.InitialLevel;
import ru.skidoz.util.XMLGettingService;

import static ru.skidoz.util.Structures.parseInt;

/**
 * @author andrey.semenov
 */
@Component
public class TelegramProcessor {

    @Autowired
    private CommandProvider commandProvider;

    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;

    @Autowired
    private BasketCacheRepository basketCacheRepository;
    @Autowired
    private BookmarkCacheRepository bookmarkCacheRepository;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private CashbackWriteOffCacheRepository cashbackWriteOffCacheRepository;
    @Autowired
    private RecommendationCacheRepository recommendationCacheRepository;
    @Autowired
    private ScheduleBuyerCacheRepository scheduleBuyerCacheRepository;
    @Autowired
    private XMLGettingService xmlGettingService;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private P2PExistBuyerLinkStarter p2PExistBuyerLinkStarter;
    @Autowired
    private P2PNewBuyerLinkStarter p2PNewBuyerLinkStarter;
    @Autowired
    private P2BLinkStarter p2BLinkStarter;
    @Autowired
    private B2BLinkStarter b2BLinkStarter;
    @Autowired
    private CashbackLinkStarter cashbackLinkStarter;
    @Autowired
    private BookmarkLinkStarter bookmarkLinkStarter;
    @Autowired
    private BasketLinkStarter basketLinkStarter;


    public ConcurrentMap<String, String> mergeUsers = new ConcurrentHashMap<>();


    private List<LevelChat> startProcessor(User user, long chatId, Update update, boolean newUser) throws CloneNotSupportedException {

        String inputText = update.getMessage().getText();
        String bearingCommand = inputText.substring("/start=".length(), "/start=".length() + 2);
        String bearingId = inputText.substring("/start=".length() + 2);

        System.out.println("bearingCommand***" + bearingCommand);
        System.out.println("bearingId***" + bearingId);

        //тот, от кого пришло сообщение - значит, тот, кто прочитал qr или перешел по ссылке
        List<LevelChat> levelChatDTOList = new ArrayList<>();

        if (bearingCommand.equals("PP")) {

            User yourSelf = userCacheRepository.findBySessionId(bearingId);

            System.out.println();
            System.out.println("bearingId++++++++++" + bearingId);
            System.out.println(yourSelf);
            System.out.println();
            System.out.println();

            mergeUser(user, yourSelf);
        } else
            //List<Shop> shopsBuyer = shopRepository.getByAdminUserAndActiveIsTrue(users);
            // если отсканировали распечатанный qr-код на листовке или стенде магазина - туда пишем shopId
            if (bearingCommand.equals("BI")) {
                Shop shopPartner = shopCacheRepository.findById(Integer.valueOf(bearingId));

                //shopsBuyer.isEmpty()
                if (!user.isShopOwner()) {
                    // если у меня нет магазинов
                    levelChatDTOList.addAll(p2BLinkStarter.getLevel(null, user, null, shopPartner));
                } else {
                    // если есть магазины
                    levelChatDTOList.addAll(b2BLinkStarter.getLevel(chatId, user, null, shopPartner));
                }
            } else {

                User friend = null;
                List<LevelChat> levelChatDTOList2 = new ArrayList<>();

                System.out.println("bearingCommand++++++++++++++++++++++++++" + bearingCommand);
                //тот, кто отправил ссылку или чей qr
                //shopsBuyer.isEmpty()
                if (!user.isShopOwner()) {
                    // если я не магазин, а человек, и получил сообщение или прочел qr человека

                    if (bearingCommand.equals("BK")) {//человек шлет человеку из корзины
                        Basket basket = basketCacheRepository.findById(Integer.valueOf(bearingId));
                        if (basket != null) {
                            friend = userCacheRepository.findById(basket.getUser());
                            levelChatDTOList2 = basketLinkStarter.getLevel(chatId, bearingId, user, friend);
                        }
                    } else if (bearingCommand.equals("BM")) {//человек шлет человеку из закладок
                        Bookmark bookmark = bookmarkCacheRepository.findById(Integer.valueOf(bearingId));
                        if (bookmark != null) {
                            friend = userCacheRepository.findById(bookmark.getUser());
                            levelChatDTOList2 = bookmarkLinkStarter.getLevel(chatId, bearingId, user, friend);
                        }
                    } else if (bearingCommand.equals("CB")) {//человек шлет человеку из кэшбеков
                        Cashback cashback = cashbackCacheRepository.findById(Integer.valueOf(bearingId));
                        if (cashback != null) {
                            friend = userCacheRepository.findById(cashback.getUser());
                            levelChatDTOList2 = cashbackLinkStarter.getLevel(chatId, bearingId, user, friend);
                        }
                    } else if (bearingCommand.equals("PI")) {
                        System.out.println("bearingCommand +++++++++++++++++++++++++++++++++++++++++ PI***" + Long.valueOf(bearingId));
                        friend = userCacheRepository.findBySessionId(bearingId);

                        if (friend != null
                                && !newUser) {
                            levelChatDTOList2 = p2PExistBuyerLinkStarter.getLevel(chatId, user, friend);
                        }
                    }
                    System.out.println("friend+++++++++++++++++" + friend);
                } else {
                    friend = userCacheRepository.findById(Integer.valueOf(bearingId));
                    //List<Shop> shopsFriend = shopRepository.getByAdminUserAndActiveIsTrue(friend);
                    // если я - магазин
                    //shopsFriend.isEmpty()
                    if (!friend.isShopOwner()) {
                        levelChatDTOList2 = p2BLinkStarter.getLevel(null, user, friend, null);
                    } else {
                        List<Shop> shopsFriend = shopCacheRepository.findAllBySellerIdAndActiveIsTrue(friend.getId());
                        levelChatDTOList2 = b2BLinkStarter.getLevel(chatId, user, null, shopsFriend.get(0));
                    }
                }

                if (newUser) {
                    levelChatDTOList.addAll(p2PNewBuyerLinkStarter.getLevel(chatId, user, friend));
                }

                levelChatDTOList.addAll(levelChatDTOList2);
            }
        System.out.println("levelChatDTOList+++++++++++++++" + levelChatDTOList);

        return levelChatDTOList;
    }

    public void mergeUser(User targetUsersDTO, User duplicateUsersDTO) {


//        Users targetUsers = new Users(targetUsersDTO.getId());

//        authorizeUser(String.valueOf(targetUsersDTO.getSessionId()), String.valueOf(targetUsersDTO.getChatId()));

        System.out.println("mergeUser++++++++++++++++++");
        System.out.println("targetUsers***" + targetUsersDTO);
        System.out.println("duplicateUsers***" + duplicateUsersDTO);

        recommendationCacheRepository.findAllByBuyer_Id(duplicateUsersDTO.getId()).forEach(e -> {
            e.setBuyer(targetUsersDTO.getId());
            recommendationCacheRepository.save(e);
        });
        recommendationCacheRepository.findAllByFriend_Id(duplicateUsersDTO.getId()).forEach(e -> {
            e.setFriend(targetUsersDTO.getId());
            recommendationCacheRepository.save(e);
        });
        purchaseCacheRepository.findAllByBuyer_Id(duplicateUsersDTO.getId()).forEach(e -> {
            e.setUser(targetUsersDTO.getId());
            purchaseCacheRepository.save(e);
        });
        scheduleBuyerCacheRepository.findAllByUser(duplicateUsersDTO.getId()).forEach(e -> {
            e.setUser(targetUsersDTO.getId());
            scheduleBuyerCacheRepository.save(e);
        });
        basketCacheRepository.findAllByUserId(duplicateUsersDTO.getId()).forEach(e -> {
            e.setUser(targetUsersDTO.getId());
            basketCacheRepository.save(e);
        });
        bookmarkCacheRepository.findAllByUserId(duplicateUsersDTO.getId()).forEach(e -> {
            e.setUser(targetUsersDTO.getId());
            bookmarkCacheRepository.save(e);
        });
        cashbackCacheRepository.findAllByUserId(duplicateUsersDTO.getId()).forEach(e -> {
            e.setUser(targetUsersDTO.getId());
            System.out.println(216);
            cashbackCacheRepository.save(e);
        });
        cashbackWriteOffCacheRepository.findAllByUser(duplicateUsersDTO.getId()).forEach(e -> {
            e.setUser(targetUsersDTO.getId());
            cashbackWriteOffCacheRepository.save(e);
        });
        userCacheRepository.save(targetUsersDTO);

        mergeUsers.put(duplicateUsersDTO.getSessionId(), targetUsersDTO.getSessionId());
    }


    LevelResponse plainLevelChoicer(Integer currentLevelId, Update update, User users, long chatId, boolean newUser, String key) throws Exception {

        Level newLevel = null;
        final List<LevelChat> levelChats;
        if (update.hasMessage()) {

            String inputText = update.getMessage().getText();
            Location location = update.getMessage().getLocation();

            if (inputText != null && inputText.startsWith("/start ")) {
                levelChats = startProcessor(users, chatId, update, newUser);
                return new LevelResponse(levelChats, null, key);
            }
            //TODO - make refactoring, may not be null, but empty...
            if (inputText != null || update.getMessage().hasPhoto()) {

                if (currentLevelId == 0) {
                    return null;
                }

                System.out.println("inputText*" + inputText);

                newLevel = levelCacheRepository.findFirstBySourceIsMessageIsTrueAndParentLevel_Id(currentLevelId);

                System.out.println("newLevel   findFirstBySourceIsMessageIsTrueAndParentLevelId***" + newLevel);

                System.out.println("all child levels " + levelCacheRepository.findAllByParentLevelId(currentLevelId));

                if (newLevel == null) {
                    final Level currentLevel = levelCacheRepository.findById(currentLevelId);

                    System.out.println("currentLevel++++++++++++++++" + currentLevel);

                    String levelCallName = currentLevel.getCallName();
                    Command command = commandProvider.getCommand(levelCallName);

                    return command.runCommand(update, newLevel, users);
                }

                String levelCallName = newLevel.getCallName();

                System.out.println("newLevel.getIsBotLevel()***" + newLevel.isBotLevel());

                if (commandProvider.commandExists(levelCallName)) {
                    Command command = commandProvider.getCommand(levelCallName);
                    return command.runCommand(update, newLevel, users);
                }

                throw new RuntimeException("Не смогли обработать сообщение");
            }
        }

        if (update.hasCallbackQuery()) {
            String callback = update.getCallbackQuery().getData();

            System.out.println("callback*****" + callback);

            if (callback.startsWith("@")) {
                newLevel = levelCacheRepository.findById(currentLevelId);
            } else {
                newLevel = levelCacheRepository.findById(parseInt(callback.substring(0, 19)));
            }

            if (newLevel != null) {
                System.out.println("*****newLevel.getCallName() " + newLevel.getCallName());
            }

            System.out.println(newLevel != null);
            System.out.println("commandExists+++" + commandProvider.commandExists(newLevel.getCallName()));
            // работает на новый level

            String levelCallName = newLevel.getCallName();

            System.out.println("newLevel.getIsBotLevel()+++" + newLevel.isBotLevel());

            if (commandProvider.commandExists(levelCallName)) {
                Command command = commandProvider.getCommand(levelCallName);
                return command.runCommand(update, newLevel, users);
            } else {

                LevelDTOWrapper levelDTOWrapper = initialLevel.convertToLevel(newLevel,
                        true,
                        true);

                levelChats =  new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
                    e.setChatId(chatId);
                    e.setUser(users);
                    e.setLevel(levelDTOWrapper);
                })));
                return new LevelResponse(levelChats, null, key);
            }
        }

// если ничего не совпало - возвратим текущий
        if (newLevel == null) {
            return null;
        }

        LevelDTOWrapper levelDTOWrapper = initialLevel.convertToLevel(newLevel,
                true,
                true);

        levelChats = new ArrayList<>(Collections.singletonList(new LevelChat(
                e -> {
                    e.setChatId(chatId);
                    e.setUser(users);
                    e.setLevel(levelDTOWrapper);
                })));
        return new LevelResponse(levelChats, null, key);
    }

}
