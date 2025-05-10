package ru.skidoz.repository.telegram;

import java.util.Optional;

import ru.skidoz.model.entity.telegram.ButtonEntity;
import ru.skidoz.model.entity.telegram.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ButtonRepository extends JpaRepository<ButtonEntity, Integer> {

    ButtonEntity findByButtonRow_LevelAndCallback(LevelEntity level, String callback);

    void delete(ButtonEntity button);

    Optional<ButtonEntity> findById(Integer id);

    ButtonEntity save(ButtonEntity button);
}
