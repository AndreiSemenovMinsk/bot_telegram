package ru.skidoz.aop.repo;

import org.springframework.stereotype.Repository;
import ru.skidoz.model.pojo.side.RemotedPrice;

@Repository
public interface RemotedPriceCacheRepository extends JpaRepositoryTest<RemotedPrice, String> {

}
