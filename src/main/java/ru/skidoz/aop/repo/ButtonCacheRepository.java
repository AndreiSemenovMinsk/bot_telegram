package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.telegram.Button;
import ru.skidoz.model.pojo.telegram.Level;
import org.springframework.stereotype.Service;

@Service
public interface ButtonCacheRepository extends JpaRepositoryTest<Button, Integer> {

//    Button findById(Integer id);

    Button findByButtonRow_LevelAndCallback(Level level, String callback);

    List<Button> findByButtonRowId(Integer id);

    void deleteButtonByButtonRowId(Integer buttonRowId);

    void deleteButtonByLevelId(Integer  levelId);

    void delete(Button button);
}
