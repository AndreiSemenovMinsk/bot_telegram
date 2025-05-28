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
import ru.skidoz.aop.repo.ButtonCacheRepository;
import ru.skidoz.aop.repo.ButtonRowCacheRepository;
import ru.skidoz.aop.repo.JpaRepositoryTest;
import ru.skidoz.aop.repo.LevelCacheRepository;
import ru.skidoz.aop.repo.MessageCacheRepository;
import ru.skidoz.aop.repo.ProductCacheRepository;
import ru.skidoz.aop.repo.ScheduleBuyerCacheRepository;
import ru.skidoz.aop.repo.UserCacheRepository;
import ru.skidoz.mapper.EntityMapper;
import ru.skidoz.mapper.telegram.ButtonRowMapper;
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
import ru.skidoz.repository.telegram.ButtonRepository;
import ru.skidoz.repository.telegram.ButtonRowRepository;
import ru.skidoz.repository.telegram.LevelRepository;
import ru.skidoz.repository.telegram.MessageRepository;
import ru.skidoz.repository.telegram.UserRepository;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
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
    private LevelRepository levelRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ButtonRowRepository buttonRowRepository;
    @Autowired
    private ScheduleBuyerCacheRepository scheduleBuyerRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LevelCacheRepository levelCacheRepository;
    @Autowired
    private MessageCacheRepository messageCacheRepository;
    @Autowired
    private ButtonRowCacheRepository buttonRowCacheRepository;
    @Autowired
    private ButtonCacheRepository buttonCacheRepository;
    @Autowired
    private UserCacheRepository userCacheRepository;

    @Autowired
    private ButtonRowMapper buttonRowMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private ProductCacheRepository productCacheRepository;

    public <D extends AbstractDTO, E extends AbstractEntity> void store(
            JpaRepositoryTest<D, Integer> cache,
            JpaRepository<E, Integer> repository,
            EntityMapper<D, E> entityMapper) {

        String simpleName = cache.getClass().getSimpleName();
        System.out.println("simpleName " + simpleName);

        int repoIndex = cacheAspect.repos.get(simpleName);
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
                value.setId(null);
                E entity = entityMapper.toEntity(value);
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

    @Scheduled(cron = "${saveToRepoTimer}")
    public void save() throws IOException, WriterException {

        long start = System.currentTimeMillis();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++ScheduleService+++++++++++++++++++++++++++++++++++");

        store(buttonRowCacheRepository, buttonRowRepository, buttonRowMapper);

        System.out.println();
        System.out.println("-----------------ScheduleService---------------");
        System.out.println((System.currentTimeMillis() - start) + "ms");
        System.out.println();
    }


    @Scheduled(cron = "${remindTimer}")
    public void reminder() {

        ZoneId z = ZoneId.of("Europe/Minsk");
        ZonedDateTime zdt = ZonedDateTime.now(z);

        List<ScheduleBuyer> scheduleBuyer1HourList = scheduleBuyerRepository
                .findAllByDayAndMonthAndYearAndTimeStart(zdt.getDayOfMonth(), zdt.getMonthValue(), zdt.getYear(), timePoint - 12);

        for (ScheduleBuyer scheduleBuyer : scheduleBuyer1HourList) {

            notificationHandler(scheduleBuyer, "Напоминание - через 1 час ");
        }

        List<ScheduleBuyer> scheduleBuyerHalfHourList = scheduleBuyerRepository
                .findAllByDayAndMonthAndYearAndTimeStart(zdt.getDayOfMonth(), zdt.getMonthValue(), zdt.getYear(), timePoint - 12);
        for (ScheduleBuyer scheduleBuyer : scheduleBuyerHalfHourList) {

            notificationHandler(scheduleBuyer, "Напоминание - через полчаса ");
        }
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
