package com.example.ticketstream.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ticketstream.model.Screening;

@Repository
public interface ScreeningRepository extends CrudRepository<Screening, UUID> {
	
	Optional<Screening> findByStartYearMonthAndStartTimeAndEndTimeAndId(
			int yearMonth, 
			LocalDateTime startTime, 
			LocalDateTime endTime,
			UUID screeningId
			);
	
	@Query("select * from screening where start_year_month = :startYearMonth "
			+ "AND (start_time, end_time) >= (:startTime, :startTime) "
			+ "AND (start_time, end_time) <= (:endTime, :endTime)")
	List<Screening> findByStartTimeBetweenAndEndTimeBetween(
			@Param("startYearMonth") int startYearMonth, 
			@Param("startTime") LocalDateTime startTime, 
			@Param("endTime") LocalDateTime endTime);

	@Query("select * from screening where start_year_month = :startYearMonth "
			+ "AND (start_time, end_time) >= (:startTime, :startTime) "
			+ "AND (start_time, end_time) <= (:endTime, :endTime) "
			+ "AND movie_id = :movieId "
			)
	List<Screening> findByMovieIdAndByStartTimeBetweenAndEndTimeBetween(
			@Param("startYearMonth") int startYearMonth, 
			@Param("startTime") LocalDateTime startTime, 
			@Param("endTime") LocalDateTime endTime,
			@Param("movieId") UUID movieId
			);

	@Query("select * from screening where start_year_month = :startYearMonth "
			+ "AND (start_time, end_time) >= (:startTime, :startTime) "
			+ "AND (start_time, end_time) <= (:endTime, :endTime) "
			+ "AND theater_id = :theaterId "
			)
	List<Screening> findByTheaterIdAndByStartTimeBetweenAndEndTimeBetween(
			@Param("startYearMonth") int startYearMonth, 
			@Param("startTime") LocalDateTime startTime, 
			@Param("endTime") LocalDateTime endTime,
			@Param("theaterId") UUID theaterId
			);

}
