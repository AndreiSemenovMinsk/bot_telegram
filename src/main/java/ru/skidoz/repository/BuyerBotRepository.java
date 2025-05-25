package ru.skidoz.repository;

import ru.skidoz.model.entity.BuyerBotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerBotRepository extends JpaRepository<BuyerBotEntity, Integer> {

//    BuyerBot findByUserAndBot(Users buyer, Bot bot);

    BuyerBotEntity save(BuyerBotEntity buyerBot);
}
