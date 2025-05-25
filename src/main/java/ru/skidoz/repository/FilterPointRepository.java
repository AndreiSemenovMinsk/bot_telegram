package ru.skidoz.repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.FilterPointEntity;
import ru.skidoz.model.entity.category.LanguageEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterPointRepository extends JpaRepository<FilterPointEntity, Integer> {

    FilterPointEntity findByCategory_IdAndNameRU(Integer categoryId, String name);

    FilterPointEntity findByCategory_IdAndNameEN(Integer categoryId, String name);

    FilterPointEntity findByCategory_IdAndNameDE(Integer categoryId, String name);

    List<FilterPointEntity> findAllByCategory_IdAndNameRU(Integer categoryId, String name);

    List<FilterPointEntity> findAllByCategory_IdAndNameEN(Integer categoryId, String name);

    List<FilterPointEntity> findAllByCategory_IdAndNameDE(Integer categoryId, String name);

    FilterPointEntity findByCategory_IdAndUnitNameRU(Integer categoryId, String unitName);

    FilterPointEntity findByCategory_IdAndUnitNameEN(Integer categoryId, String unitName);

    FilterPointEntity findByCategory_IdAndUnitNameDE(Integer categoryId, String unitName);

    List<FilterPointEntity> findAllByCategory_Id(Integer categoryId);

    FilterPointEntity save(FilterPointEntity filterPoint);

    default FilterPointEntity findByCategoryAndName_LocaleAndName_Text(Integer categoryId, LanguageEnum language, String name) {
        if (LanguageEnum.EN.equals(language)) {
            return findByCategory_IdAndNameEN(categoryId, name);
        } else if (LanguageEnum.RU.equals(language)) {
            return findByCategory_IdAndNameRU(categoryId, name);
        } else if (LanguageEnum.DE.equals(language)) {
            return findByCategory_IdAndNameDE(categoryId, name);
        } else {
            return null;
        }
    }

    default List<FilterPointEntity> findAllByCategoryAndName_LocaleAndName_Text(Integer categoryId, LanguageEnum language, String name) {
        if (LanguageEnum.EN.equals(language)) {
            return findAllByCategory_IdAndNameEN(categoryId, name);
        } else if (LanguageEnum.RU.equals(language)) {
            return findAllByCategory_IdAndNameRU(categoryId, name);
        } else if (LanguageEnum.DE.equals(language)) {
            return findAllByCategory_IdAndNameDE(categoryId, name);
        } else {
            return new ArrayList<>();
        }
    }

    default FilterPointEntity findByCategoryAndUnitName_LocaleAndUnitName_Text(Integer categoryId, LanguageEnum language, String unitName) {
        if (LanguageEnum.EN.equals(language)) {
            return findByCategory_IdAndUnitNameEN(categoryId, unitName);
        } else if (LanguageEnum.RU.equals(language)) {
            return findByCategory_IdAndUnitNameRU(categoryId, unitName);
        } else if (LanguageEnum.DE.equals(language)) {
            return findByCategory_IdAndUnitNameDE(categoryId, unitName);
        } else {
            return null;
        }
    }
}
