package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.telegram.Message;
import org.springframework.stereotype.Service;

@Service
public interface MessageCacheRepository extends JpaRepositoryTest<Message, Integer> {

    List<Message> findAllByLevel_Id(Integer levelId);

    //void deleteMessagesByLevel(Level level);

}
