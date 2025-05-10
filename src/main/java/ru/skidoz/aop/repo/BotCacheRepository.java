package ru.skidoz.aop.repo;

import ru.skidoz.model.pojo.telegram.Bot;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface BotCacheRepository extends JpaRepositoryTest<Bot, Integer> {

//    public Bot findById(Integer  botId);

    public Bot findByShopId(Integer  shopId);

    public void delete(Integer  botId);


}
