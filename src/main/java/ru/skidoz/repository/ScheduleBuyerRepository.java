package ru.skidoz.repository;

import java.util.List;

import ru.skidoz.model.entity.ScheduleBuyerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleBuyerRepository extends JpaRepository<ScheduleBuyerEntity, Integer> {

    List<ScheduleBuyerEntity> findAllByUser_Id(Integer/*Users*/ buyer);

    List<ScheduleBuyerEntity> findAllByDayAndMonthAndYearAndProduct_Id(Integer day, Integer month, Integer year, Integer productId);

    List<ScheduleBuyerEntity> findAllByDayAndMonthAndYearAndTimeStart(Integer day, Integer month, Integer year, Integer timeStart);

    @Query(value = "SELECT COUNT(schedule_buyer.id) FROM schedule_buyer " +
            " INNER JOIN abstract_group_entity ON (abstract_group_entity.id=schedule_buyer.product_id) " +
            " WHERE schedule_buyer.product_id=:productId AND day=:day AND month=:month AND year=:year " +
            " AND  ((time_start < :timeStart AND time_end > :timeStart) " +
            " OR (time_start < abstract_group_entity.duration + :timeStart AND time_end > abstract_group_entity.duration + :timeStart) " +
            " OR (time_start > :timeStart AND time_end < abstract_group_entity.duration + :timeStart))", nativeQuery = true)
    Integer setTimeSchedule(@Param("day") Integer day,
                            @Param("month") Integer month,
                            @Param("year") Integer year,
                            @Param("productId") Integer productId,
                            @Param("timeStart") Integer timeStart);

    @Modifying
    @Query(value = "INSERT INTO schedule_buyer SET users_id=:usersId,"+
        " day=:day, month=:month, year=:year, time_start=:timeStart, " +
        " time_end=(:timeStart + (SELECT duration FROM abstract_group_entity WHERE abstract_group_entity.id=:productId LIMIT 1)), "+
        " product_id=:productId", nativeQuery = true)
    void insertSetTimeSchedule(@Param("day") Integer day,
                               @Param("month") Integer month,
                               @Param("year") Integer year,
                               @Param("productId") Integer productId,
                               @Param("userId") Integer userId,
                               @Param("timeStart") Integer timeStart);

    ScheduleBuyerEntity save(ScheduleBuyerEntity scheduleBuyer);
}
