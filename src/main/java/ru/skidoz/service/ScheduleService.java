package ru.skidoz.service;


import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ru.skidoz.aop.CacheAspect;
import ru.skidoz.aop.repo.ActionCacheRepository;
import ru.skidoz.aop.repo.BasketCacheRepository;
import ru.skidoz.aop.repo.BasketProductCacheRepository;
import ru.skidoz.aop.repo.BookmarkCacheRepository;
import ru.skidoz.aop.repo.BotCacheRepository;
import ru.skidoz.aop.repo.ButtonCacheRepository;
import ru.skidoz.aop.repo.ButtonRowCacheRepository;
import ru.skidoz.aop.repo.CashbackCacheRepository;
import ru.skidoz.aop.repo.CashbackWriteOffCacheRepository;
import ru.skidoz.aop.repo.CategoryCacheRepository;
import ru.skidoz.aop.repo.CategoryGroupCacheRepository;
import ru.skidoz.aop.repo.CategorySuperGroupCacheRepository;
import ru.skidoz.aop.repo.FilterOptionCacheRepository;
import ru.skidoz.aop.repo.FilterPointCacheRepository;
import ru.skidoz.aop.repo.JpaRepositoryTest;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.NameWordProductCacheRepository;
import ru.skidoz.aop.repo.PartnerCacheRepository;
import ru.skidoz.aop.repo.PartnerGroupCacheRepository;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.PurchaseCacheRepository;
import ru.skidoz.aop.repo.RecommendationCacheRepository;
import ru.skidoz.aop.repo.ShopCacheRepository;
import ru.skidoz.aop.repo.ShopGroupCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.category.CategoryGroupMapper;
import ru.skidoz.mapper.category.CategoryMapper;
import ru.skidoz.mapper.category.CategorySuperGroupMapper;
import ru.skidoz.mapper.search.FilterOptionMapper;
import ru.skidoz.mapper.search.FilterPointMapper;
import ru.skidoz.mapper.side.ActionMapper;
import ru.skidoz.mapper.side.BasketMapper;
import ru.skidoz.mapper.side.BasketProductMapper;
import ru.skidoz.mapper.side.BookmarkMapper;
import ru.skidoz.mapper.side.CashbackMapper;
import ru.skidoz.mapper.side.NameWordProductMapper;
import ru.skidoz.mapper.side.PartnerGroupMapper;
import ru.skidoz.mapper.side.PartnerMapper;
import ru.skidoz.mapper.side.ProductMapper;
import ru.skidoz.mapper.side.PurchaseMapper;
import ru.skidoz.mapper.side.ShopGroupMapper;
import ru.skidoz.mapper.side.ShopMapper;
import ru.skidoz.mapper.telegram.BotMapper;
import ru.skidoz.mapper.telegram.ButtonMapper;
import ru.skidoz.mapper.telegram.ButtonRowMapper;
import ru.skidoz.mapper.telegram.CashbackWriteOffMapper;
import ru.skidoz.mapper.telegram.LevelMapper;
import ru.skidoz.mapper.telegram.MessageMapper;
import ru.skidoz.mapper.telegram.RecommendationMapper;
import ru.skidoz.mapper.telegram.ScheduleBuyerMapper;
import ru.skidoz.mapper.telegram.UsersMapper;
import ru.skidoz.model.DTO;
import ru.skidoz.model.entity.AbstractEntity;
import ru.skidoz.model.entity.category.LanguageEnum;
import ru.skidoz.model.pojo.AbstractDTO;
import ru.skidoz.model.pojo.telegram.Level;
import ru.skidoz.model.pojo.telegram.LevelChat;
import ru.skidoz.model.pojo.telegram.LevelDTOWrapper;
import ru.skidoz.model.pojo.telegram.Message;
import ru.skidoz.model.pojo.telegram.ScheduleBuyer;
import ru.skidoz.model.pojo.telegram.User;
import ru.skidoz.repository.ActionRepository;
import ru.skidoz.repository.BasketProductRepository;
import ru.skidoz.repository.BasketRepository;
import ru.skidoz.repository.BookmarkRepository;
import ru.skidoz.repository.BotRepository;
import ru.skidoz.repository.CashbackRepository;
import ru.skidoz.repository.CashbackWriteOffRepository;
import ru.skidoz.repository.CategoryGroupRepository;
import ru.skidoz.repository.CategoryRepository;
import ru.skidoz.repository.CategorySuperGroupRepository;
import ru.skidoz.repository.FilterOptionRepository;
import ru.skidoz.repository.FilterPointRepository;
import ru.skidoz.repository.NameWordProductRepository;
import ru.skidoz.repository.PartnerGroupRepository;
import ru.skidoz.repository.PartnerRepository;
import ru.skidoz.repository.ProductRepository;
import ru.skidoz.repository.PurchaseRepository;
import ru.skidoz.repository.RecommendationRepository;
import ru.skidoz.repository.ScheduleBuyerRepository;
import ru.skidoz.repository.ShopGroupRepository;
import ru.skidoz.repository.ShopRepository;
import ru.skidoz.repository.telegram.ButtonRepository;
import ru.skidoz.repository.telegram.ButtonRowRepository;
import ru.skidoz.repository.telegram.LevelRepository;
import ru.skidoz.repository.telegram.MessageRepository;
import ru.skidoz.repository.telegram.UserRepository;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


@Qualifier("CurrencyService")
@Service
public class ScheduleService {

    public static Integer timePoint = 0;
    @Autowired
    public TelegramBot telegramBot;
    @Autowired
    private CacheAspect cacheAspect;

    @Autowired
    private FilterPointRepository filterPointRepository;
    @Autowired
    private FilterPointCacheRepository filterPointCacheRepository;
    @Autowired
    private CategorySuperGroupRepository categorySuperGroupRepository;
    @Autowired
    private CategoryGroupRepository categoryGroupRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategorySuperGroupCacheRepository categorySuperGroupCacheRepository;
    @Autowired
    private CategoryGroupCacheRepository categoryGroupCacheRepository;
    @Autowired
    private CategoryCacheRepository categoryCacheRepository;
    @Autowired
    private FilterOptionRepository filterOptionRepository;
    @Autowired
    private FilterOptionCacheRepository filterOptionCacheRepository;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private ScheduleBuyerRepository scheduleBuyerRepository;
    @Autowired
    private InitialLevel initialLevel;
    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ButtonRowCacheRepository buttonRowCacheRepository;
    @Autowired
    private ButtonRowRepository buttonRowRepository;
    @Autowired
    private ButtonCacheRepository buttonCacheRepository;
    @Autowired
    private ButtonRepository buttonRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NameWordProductRepository nameWordProductRepository;
    @Autowired
    private NameWordProductCacheRepository nameWordProductCacheRepository;

    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private ActionCacheRepository actionCacheRepository;

    @Autowired
    private RecommendationRepository recommendationRepository;
    @Autowired
    private RecommendationCacheRepository recommendationCacheRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private PartnerCacheRepository partnerCacheRepository;
    @Autowired
    private PartnerGroupRepository partnerGroupRepository;
    @Autowired
    private PartnerGroupCacheRepository partnerGroupCacheRepository;
    @Autowired
    private ShopGroupRepository shopGroupRepository;
    @Autowired
    private ShopGroupCacheRepository shopGroupCacheRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ShopCacheRepository shopCacheRepository;
    @Autowired
    private PurchaseCacheRepository purchaseCacheRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCacheRepository productCacheRepository;
    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private BasketCacheRepository basketCacheRepository;
    @Autowired
    private BasketProductRepository basketProductRepository;
    @Autowired
    private BasketProductCacheRepository basketProductCacheRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private BookmarkCacheRepository bookmarkCacheRepository;
    @Autowired
    private BotRepository botRepository;
    @Autowired
    private BotCacheRepository botCacheRepository;
    @Autowired
    private CashbackRepository cashbackRepository;
    @Autowired
    private CashbackCacheRepository cashbackCacheRepository;
    @Autowired
    private CashbackWriteOffRepository cashbackWriteOffRepository;
    @Autowired
    private CashbackWriteOffCacheRepository cashbackWriteOffCacheRepository;
//    @Autowired
//    private CashbackWriteOffResultPurchaseRepository cashbackWriteOffResultPurchaseRepository;
//    @Autowired
//    private CashbackWriteOffResultPurchaseCacheRepository cashbackWriteOffResultPurchaseCacheRepository;
    @Autowired
    private ButtonRowMapper buttonRowMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private FilterOptionMapper filterOptionMapper;
    @Autowired
    private FilterPointMapper filterPointMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryGroupMapper categoryGroupMapper;
    @Autowired
    private CategorySuperGroupMapper categorySuperGroupMapper;
    @Autowired
    private CashbackMapper cashbackMapper;
    @Autowired
    private CashbackWriteOffMapper cashbackWriteOffMapper;
    @Autowired
    private BookmarkMapper bookmarkMapper;
    @Autowired
    private BasketMapper basketMapper;
    @Autowired
    private BasketProductMapper basketProductMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PartnerMapper partnerMapper;
    @Autowired
    private PartnerGroupMapper partnerGroupMapper;
    @Autowired
    private PurchaseMapper purchaseMapper;
    @Autowired
    private RecommendationMapper recommendationMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private ShopGroupMapper shopGroupMapper;
    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private LevelMapper levelMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private ButtonMapper buttonMapper;
    @Autowired
    private BotMapper botMapper;
    @Autowired
    private NameWordProductMapper nameWordProductMapper;
    @Autowired
    private ScheduleBuyerMapper scheduleBuyerMapper;

    public <D extends AbstractDTO, E extends AbstractEntity> void store(
            JpaRepositoryTest<D, Integer> cache,
            JpaRepository<E, Integer> repository,
            EntityMapper<D, E> entityMapper) {

        String intefaceName = cache.getClass().getGenericInterfaces()[0].getTypeName();
        final String[] split = intefaceName.split("\\.");
        final String simpleName = split[split.length - 1];

        int repoIndex = cacheAspect.repoOrders.get(simpleName);
        ConcurrentHashMap<Integer, DTO> newDTOs = cacheAspect.idNewMap.get(repoIndex);

        int size = newDTOs.size();
        List<D> dtos = new ArrayList<>(size);
        List<E> entities = new ArrayList<>(size);
        List<Integer> ids = new ArrayList<>(size);

        newDTOs.forEach((k, v) -> {
            ids.add(k);
            dtos.add((D) v);
        });
        newDTOs.clear();

        for (int i = 0; i < size; i++) {
            D value = dtos.get(i);
            if (value != null) {
                E entity = entityMapper.toEntity(value);
                entity.setId(null);
                entities.add(entity);
            }
        }

        List<E> results = repository.saveAll(entities);

        for (int i = 0; i < size; i++) {

            int newId = results.get(i).getId();
            D d = dtos.get(i);
            d.setId(newId);
            cache.save(d);
        }
    }


    public void save() throws IOException, WriterException {

        long start = System.currentTimeMillis();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++ScheduleService+++++++++++++++++++++++++++++++++++");



        store(categorySuperGroupCacheRepository, categorySuperGroupRepository, categorySuperGroupMapper);

        store(categoryGroupCacheRepository, categoryGroupRepository, categoryGroupMapper);

        store(categoryCacheRepository, categoryRepository, categoryMapper);

        store(filterPointCacheRepository, filterPointRepository, filterPointMapper);

        store(filterOptionCacheRepository, filterOptionRepository, filterOptionMapper);


        store(userCacheRepository, userRepository, usersMapper);

        store(levelCacheRepository, levelRepository, levelMapper);

        store(buttonRowCacheRepository, buttonRowRepository, buttonRowMapper);

        store(buttonCacheRepository, buttonRepository, buttonMapper);

        store(messageCacheRepository, messageRepository, messageMapper);


        store(userCacheRepository, userRepository, usersMapper);

        store(shopCacheRepository, shopRepository, shopMapper);

        store(shopGroupCacheRepository, shopGroupRepository, shopGroupMapper);

        store(partnerCacheRepository, partnerRepository, partnerMapper);

        store(partnerGroupCacheRepository, partnerGroupRepository, partnerGroupMapper);

        //actionCacheRepository.getNewList().forEach(e -> System.out.println("***++++" + e));
        //actionCacheRepository.getNewList().forEach(e -> System.out.println(e.getShop() + "@@@@@" + commonReplace.containsKey(e.getShop()) + "++++" + commonReplace.get(e.getShop())));

        store(actionCacheRepository, actionRepository, actionMapper);

        store(productCacheRepository, productRepository, productMapper);

        store(basketCacheRepository, basketRepository, basketMapper);

        store(basketProductCacheRepository, basketProductRepository, basketProductMapper);

        store(bookmarkCacheRepository, bookmarkRepository, bookmarkMapper);

        store(purchaseCacheRepository, purchaseRepository, purchaseMapper);

        store(recommendationCacheRepository, recommendationRepository, recommendationMapper);


        //cashbackCacheRepository.getNewList().forEach(e -> System.out.println("cashback***++++" + e));
        //cashbackCacheRepository.getNewList().forEach(e -> System.out.println(e.getShop() + "+++cashback@@@@@" + commonReplace.containsKey(e.getShop()) + "++++" + commonReplace.get(e.getShop())));


        store(cashbackCacheRepository, cashbackRepository, cashbackMapper);

        store(cashbackWriteOffCacheRepository, cashbackWriteOffRepository, cashbackWriteOffMapper);

        //store(cashbackWriteOffResultPurchaseCacheRepository, cashbackWriteOffResultPurchaseRepository, cashbackWriteOffResultPurchaseMapper);

        store(botCacheRepository, botRepository, botMapper);

        store(nameWordProductCacheRepository, nameWordProductRepository, nameWordProductMapper);

        System.out.println();
        System.out.println("-----------------ScheduleService---------------");
        System.out.println((System.currentTimeMillis() - start) + "ms");
        System.out.println();
    }


    public void reminder() {

        ZoneId z = ZoneId.of("Europe/Minsk");
        ZonedDateTime zdt = ZonedDateTime.now(z);

        List<ScheduleBuyer> scheduleBuyer1HourList = scheduleBuyerMapper.toDto(scheduleBuyerRepository
                .findAllByDayAndMonthAndYearAndTimeStart(zdt.getDayOfMonth(), zdt.getMonthValue(), zdt.getYear(),
                        timePoint - 12));

        for (ScheduleBuyer scheduleBuyer : scheduleBuyer1HourList) {
            notificationHandler(scheduleBuyer, "Напоминание - через 1 час ");
        }

        List<ScheduleBuyer> scheduleBuyerHalfHourList = scheduleBuyerMapper.toDto(scheduleBuyerRepository
                .findAllByDayAndMonthAndYearAndTimeStart(zdt.getDayOfMonth(), zdt.getMonthValue(), zdt.getYear(),
                        timePoint - 12));
        for (ScheduleBuyer scheduleBuyer : scheduleBuyerHalfHourList) {
            notificationHandler(scheduleBuyer, "Напоминание - через полчаса ");
        }

        timePoint++;
    }

    private void notificationHandler(ScheduleBuyer scheduleBuyer, String text) {
        User user = userCacheRepository.findById(scheduleBuyer.getUser());
        Level level = new Level();
        Message message = new Message(level, Map.of(LanguageEnum.RU, text + productCacheRepository.findById(scheduleBuyer.getProduct()).getAlias()));
        level.addMessage(message);

        final LevelDTOWrapper levelWrapper = new LevelDTOWrapper();
        levelWrapper.setLevel(level);

        TelegramBot.Runner runner = telegramBot.getTelegramKey(new String(user.getRunner()));
        runner.add(new ArrayList<>(Collections.singletonList(new LevelChat(e -> {
            e.setLevel(levelWrapper);
            e.setChatId(user.getChatId());
        }))));
    }
}
