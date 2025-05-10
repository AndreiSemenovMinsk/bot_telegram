package ru.skidoz.repository.telegram;

import java.util.List;

import ru.skidoz.model.entity.telegram.ButtonRowEntity;
import ru.skidoz.model.entity.telegram.LevelEntity;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
@Configurable
public interface ButtonRowRepository extends JpaRepository<ButtonRowEntity, Integer> {

    void deleteButtonRowByLevel(LevelEntity level);

    List<ButtonRowEntity> findAllByLevel_Id(Integer levelId);

    @Override
    ButtonRowEntity save(ButtonRowEntity buttonRow);
}
