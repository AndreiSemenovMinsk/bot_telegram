package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.telegram.Level;
import org.springframework.stereotype.Service;

@Service
public interface LevelCacheRepository extends JpaRepositoryTest<Level, Integer> {

//    Level findById(Integer id);

    List<Level> findAllByParentLevel_Id(Integer parentLevelId);

    Level findBySourceIsMessageAndParentLevelId(boolean isMessage, Integer parentLevelId);

    Level findFirstByUser_ChatIdAndCallName(Long chatId, String callName);

    Level findByParentLevelIdAndCallName(Integer parentLevelId, String callName);

    Level getChildLevel(Integer parentLevelId, String callName);

    Level getChildLevel(Integer parentLevelId, Integer levelId);

    List<Level> findAllByParentLevelId(Integer parentLevelId);
}
