package ru.skidoz.aop.repo;

import java.util.List;
import java.util.Set;

import ru.skidoz.model.pojo.side.NameWord;
import ru.skidoz.model.pojo.side.NameWordProduct;
import org.springframework.stereotype.Service;


/**
 * @author andrey.semenov
 */
@Service
public interface NameWordProductCacheRepository extends JpaRepositoryTest<NameWordProduct, Integer> {

    List<NameWordProduct> findByNameWord(Integer nameWordId);
}
