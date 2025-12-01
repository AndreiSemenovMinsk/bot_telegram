package ru.skidoz.aop.repo;

import ru.skidoz.model.pojo.telegram.BotType;

import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface BotTypeCacheRepository extends JpaRepositoryTest<BotType, Integer> {

    BotType findByInitialLevelStringId(String  levelId);
}
