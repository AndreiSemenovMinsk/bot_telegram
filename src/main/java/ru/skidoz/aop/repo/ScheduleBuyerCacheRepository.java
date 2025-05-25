package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.telegram.ScheduleBuyer;
import org.springframework.stereotype.Service;

/**
 * @author andrey.semenov
 */
@Service
public interface ScheduleBuyerCacheRepository extends JpaRepositoryTest<ScheduleBuyer, Integer> {

    List<ScheduleBuyer> findAllByDayAndMonthAndYearAndTimeStart(Integer day, Integer month, Integer year, Integer timeStart);

    public void addByDayAndMonthAndYearAndTimeStart(Integer day, Integer month, Integer year, Integer timeStart, ScheduleBuyer scheduleBuyer);

    public List<ScheduleBuyer> findAllByUser(Integer/*Users*/ buyer);
}
