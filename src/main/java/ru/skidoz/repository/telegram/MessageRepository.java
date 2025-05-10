package ru.skidoz.repository.telegram;

import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.telegram.LevelEntity;
import ru.skidoz.model.entity.telegram.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    MessageEntity getMessageByLevelAndLevelID(LevelEntity level, Integer id);

    Optional<MessageEntity> findById(Integer id);

    List<MessageEntity> findAllByLevel_Id(Integer levelId);

    void deleteMessagesByLevel(LevelEntity level);

    @Override
    MessageEntity save(MessageEntity message);
}
