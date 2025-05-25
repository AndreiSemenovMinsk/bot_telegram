package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.telegram.CashbackWriteOff;
import org.springframework.stereotype.Service;

@Service
public interface CashbackWriteOffCacheRepository extends JpaRepositoryTest<CashbackWriteOff, Integer> {

//    CashbackWriteOff findById(Integer id);

    List<CashbackWriteOff> findAllByUser(Integer id);

    void delete(CashbackWriteOff purchase);
}
