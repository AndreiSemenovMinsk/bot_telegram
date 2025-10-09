package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.telegram.PartnerGroup;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface PartnerGroupCacheRepository extends JpaRepositoryTest<PartnerGroup, Integer> {

    PartnerGroup findFirstByShop_IdAndShopGroup_Id(Integer /*Shop*/ creditor, Integer/*ShopGroup*/ debtor);

    List<PartnerGroup> findAllByShop_Id(Integer /*Shop*/ creditor);

    List<PartnerGroup> findAllByShopAndShopGroupInGroup(Integer  creditorId, Integer debtorId);

    List<PartnerGroup> findAll();

//     PartnerGroup findById(Integer id);
}
