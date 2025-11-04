package ru.skidoz.aop.repo;

import ru.skidoz.model.entity.LinkStarterKeeperEntity;
import ru.skidoz.model.pojo.telegram.LinkStarterKeeper;
import ru.skidoz.model.pojo.telegram.PartnerGroup;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface LinkStarterKeeperCacheRepository extends JpaRepositoryTest<LinkStarterKeeper, Integer> {

    LinkStarterKeeper findBySecretCode(Integer secretCode);

}
