package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.telegram.ButtonRow;
import ru.skidoz.model.pojo.telegram.Level;
import org.springframework.stereotype.Service;

@Service
public interface ButtonRowCacheRepository extends JpaRepositoryTest<ButtonRow, Integer> {

    void deleteButtonRowByLevel(Level level);

    List<ButtonRow> findAllByLevel_Id(Integer  levelId);
}
