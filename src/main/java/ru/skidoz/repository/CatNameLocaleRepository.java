package ru.skidoz.repository;


import ru.skidoz.model.entity.category.NameLocaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatNameLocaleRepository extends JpaRepository<NameLocaleEntity, Integer> {

    NameLocaleEntity save(NameLocaleEntity nameLocale);
}
