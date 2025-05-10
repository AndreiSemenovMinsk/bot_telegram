package ru.skidoz.aop.repo;

import org.springframework.stereotype.Service;
import ru.skidoz.model.pojo.side.NameWord;
import ru.skidoz.model.pojo.side.NameWordProduct;


/**
 * @author andrey.semenov
 */
@Service
public interface NameWordCacheRepository extends JpaRepositoryTest<NameWord, Integer> {

    NameWord findByText(String name);
}
