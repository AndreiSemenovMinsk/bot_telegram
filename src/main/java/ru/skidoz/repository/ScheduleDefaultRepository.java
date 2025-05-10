package ru.skidoz.repository;

import java.util.List;

import ru.skidoz.model.entity.ScheduleDefaultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleDefaultRepository extends JpaRepository<ScheduleDefaultEntity, Integer> {

    List<ScheduleDefaultEntity> findAllByDayAndMonthAndYearAndProduct_Id(Integer day, Integer month, Integer year, Integer productId);

    @Query(value = "SELECT COUNT(schedule_default.id) FROM schedule_default " +
            " INNER JOIN abstract_group_entity ON (abstract_group_entity.id=schedule_default.product_id) WHERE " +
            " schedule_default.product_id=:productId AND day=:day AND month=:month AND year=:year AND ((time_start < :timeStart AND time_end > :timeStart) " +
            " OR (time_start < abstract_group_entity.duration + :timeStart AND time_end > abstract_group_entity.duration + :timeStart) " +
            " OR (time_start > :timeStart AND time_end < abstract_group_entity.duration + :timeStart))", nativeQuery = true)
    Integer setTimeSchedule(@Param("day") Integer day,
                            @Param("month") Integer month,
                            @Param("year") Integer year,
                            @Param("productId") Integer productId,
                            @Param("timeStart") Integer timeStart);


    @Query(value = "SELECT COUNT(schedule_default.id) FROM schedule_default WHERE " +
            " product_id=:productId AND day=:day AND month=:month AND year=:year AND " +
            " ((time_start < :timeStart AND time_end > :timeStart) OR (time_start < :timeEnd AND time_end > :timeEnd) " +
            "OR (time_start > :timeStart AND time_end < :timeEnd))", nativeQuery = true)
    Integer setDefaultSchedule(@Param("day") Integer day,
                               @Param("month") Integer month,
                               @Param("year") Integer year,
                               @Param("productId") Integer productId,
                               @Param("timeStart") Integer timeStart,
                               @Param("timeEnd") Integer timeEnd);


    @Modifying
    @Query(value = "INSERT INTO schedule_default SET " +
            " day=:day, month=:month, year=:year, time_start=:timeStart, time_end=:timeEnd, " +
            " product_id=:productId", nativeQuery = true)
    void insertSetDefaultSchedule(@Param("day") Integer day,
                                  @Param("month") Integer month,
                                  @Param("year") Integer year,
                                  @Param("productId") Integer productId,
                                  @Param("timeStart") Integer timeStart,
                                  @Param("timeEnd") Integer timeEnd);

    @Modifying
    void deleteAllByDayAndMonthAndYearAndProduct_Id(Integer day, Integer month, Integer year, Integer productId);

    ScheduleDefaultEntity save(ScheduleDefaultEntity scheduleDefault);
}
