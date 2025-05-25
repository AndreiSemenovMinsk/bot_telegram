package ru.skidoz.repository;

import java.util.List;
import java.util.Optional;

import ru.skidoz.model.entity.BotEntity;
import ru.skidoz.model.entity.BuyerBotMessageEntity;
import ru.skidoz.model.entity.telegram.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerBotMessageRepository extends JpaRepository<BuyerBotMessageEntity, Integer> {

    Optional<BuyerBotMessageEntity> findById(Integer id);

    List<BuyerBotMessageEntity> findByBuyerBot_BotAndBuyerBot_User(BotEntity buyerBot_bot, UserEntity buyerBot_buyer);

    BuyerBotMessageEntity save(BuyerBotMessageEntity buyerBotMessage);
}
