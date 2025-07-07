package ru.skidoz.aop.repo;

import java.math.BigDecimal;
import java.util.List;
import ru.skidoz.model.entity.ActionTypeEnum;
import ru.skidoz.model.pojo.side.Cashback;
import org.springframework.stereotype.Service;

@Service
public interface CashbackCacheRepository extends JpaRepositoryTest<Cashback, Integer> {

    public List<Cashback> findAllByUserId(Integer id);

//    public Cashback findById(Integer id);

    public Cashback findByIdAndUserId(Integer id, Integer  buyerId);

    public List<Cashback> findAllByPurchase_Id(Integer  purchaseId);

    List<Cashback> findAllByUser_IdAndAction_Id(Integer  buyerId, Integer actionId);

    List<Cashback> findAllByUser_IdAndShop_Id(Integer  buyerId, Integer  shopId);

    List<Cashback> findAllByShopAndBuyerAndAction_Type(Integer  buyerId, Integer  shopId, ActionTypeEnum actionTypeEnum);

    List<Cashback> findAllByUser_IdAndShop_IdAndAction_Type(Integer  buyerId, Integer  shopId, ActionTypeEnum actionTypeEnum);

    BigDecimal purchaseSumByUserAndShopAndAction_Type(Integer  buyerId, Integer  shopId, ActionTypeEnum actionTypeEnum);

    void deleteById(Integer id, Integer purchaseId, Integer actionId, ActionTypeEnum actionTypeEnum);

}
