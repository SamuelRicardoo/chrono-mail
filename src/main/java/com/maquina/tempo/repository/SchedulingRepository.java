package com.maquina.tempo.repository;

import com.maquina.tempo.entities.Scheduling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchedulingRepository extends JpaRepository<Scheduling,Long> {

    @Query("SELECT s FROM Scheduling s WHERE (s.date) = CURRENT_DATE")
    List<Scheduling> findBySchedulingTimeToday();
}