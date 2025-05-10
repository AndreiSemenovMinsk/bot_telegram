package ru.skidoz.repository.telegram;

import java.util.Optional;
import java.util.Set;

import ru.skidoz.model.entity.telegram.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Integer> {

    Optional<LevelEntity> findById(Integer id);

    Set<LevelEntity> findAllByParentLevelId(Integer parentLevelId);

    LevelEntity findFirstBySourceIsMessageIsTrueAndParentLevelId(Integer parentLevelId);

    LevelEntity findFirstByUsers_ChatIdAndCallName(long chatId, String callName);

//    Level findByUser_IdAndCallName(Integer userId, String callName);

    @Query(value = "SELECT level FROM LevelEntity level \n" +
            "WHERE level.parentLevelId = :parentLevelId \n" +
            "AND level.callName = :callName ")
    LevelEntity getChildLevel(@Param("parentLevelId") Integer parentLevelId,
                              @Param("callName") String callName);

    @Query(value = "SELECT level FROM LevelEntity level \n" +
            "WHERE level.parentLevelId = :parentLevelId \n" +
            "AND level.id = :levelId ")
    LevelEntity getChildLevel(@Param("parentLevelId") long parentLevelId,
                              @Param("levelId") long levelId);

    LevelEntity findFirstByCallName(String callName);

    LevelEntity save(LevelEntity level);

}
