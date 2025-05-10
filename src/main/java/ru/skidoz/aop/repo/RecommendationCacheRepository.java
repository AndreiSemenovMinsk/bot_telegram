package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.telegram.Recommendation;
import org.springframework.stereotype.Service;


/**
 * @author andrey.semenov
 */
@Service
public interface RecommendationCacheRepository extends JpaRepositoryTest<Recommendation, Integer> {

    List<Recommendation> findAllByBuyer_Id(Integer /*Users*/ buyer);

    List<Recommendation> findAllByFriend_Id(Integer /*Users*/ friend);
}
