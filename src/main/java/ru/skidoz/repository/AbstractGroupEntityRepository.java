package ru.skidoz.repository;

import ru.skidoz.model.entity.AbstractGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AbstractGroupEntityRepository extends JpaRepository<AbstractGroupEntity, String> {

//    AbstractGroupEntity findByAlias(String name);


//    List<AbstractGroupEntity> findAllBySourceAtAction(Action action);

    /*@Query(value = "SELECT emp.* FROM employee emp WHERE MATCH (emp.name, emp.address, emp.passport_no) AGAINST (:lastName IN NATURAL LANGUAGE MODE)", nativeQuery = true)
    List<Employee> findFullTextSearchByLastName(@Param("lastName") String lastName);*/
}
