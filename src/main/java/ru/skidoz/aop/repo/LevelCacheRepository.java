package ru.skidoz.aop.repo;

import java.util.Set;

import ru.skidoz.model.pojo.telegram.Level;
import org.springframework.stereotype.Service;

@Service
public interface LevelCacheRepository extends JpaRepositoryTest<Level, Integer> {

//    Level findById(Integer id);

    Set<Level> findAllByParentLevel_Id(Integer parentLevelId);

    Level findFirstBySourceIsMessageIsTrueAndParentLevel_Id(Integer parentLevelId);

    Level findFirstByUser_ChatIdAndCallName(Long chatId, String callName);

    Level findByParentLevelIdAndCallName(Integer parentLevelId, String callName);

    Level getChildLevel(Integer parentLevelId, String callName);

    Level getChildLevel(Integer parentLevelId, Integer levelId);

    Set<Level> findAllByParentLevelId(Integer parentLevelId);
}
