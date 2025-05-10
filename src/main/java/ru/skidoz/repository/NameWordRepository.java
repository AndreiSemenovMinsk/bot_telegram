package ru.skidoz.repository;

import ru.skidoz.model.entity.NameWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameWordRepository extends JpaRepository<NameWordEntity, Integer> {

/*
    @Modifying
    @Query(value = "INSERT INTO name_word (text) (:text) ON DUPLICATE KEY UPDATE text = :text",
            nativeQuery = true)
    void saveUniq(@Param("text") String text);

    @Query(value = "select byr.id from user byr where byr.session_id=:session_id", nativeQuery = true)
    @Query(value = "SELECT COUNT(id) FROM name_word_product, nativeQuery = true)
    */

    NameWordEntity findByText(String text);

/*
    @Query(value = "SELECT nmwrd FROM NameWord nmwrd JOIN nmwrd.user usr WHERE usr.chatId = :chatId AND shp.id=usr.currentAdminShop")
    Set<NameWord> findAllByProduct(Prd product);*/

    NameWordEntity save(NameWordEntity nameWord);
}
