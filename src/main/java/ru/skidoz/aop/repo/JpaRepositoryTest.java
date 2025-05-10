package ru.skidoz.aop.repo;

import java.util.List;

import ru.skidoz.model.pojo.main.Purchase;

public interface JpaRepositoryTest<T,ID> {

    T findById(ID id);

    T save(T dto);

    T cache(T dto);

    List<T> findAll();

    void delete(T dto);
}
