package com.github.yhnatiuk.gpotechtask.repository;

import com.github.yhnatiuk.gpotechtask.domain.Command;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends CrudRepository<Command, Long> {

  String querySetStatusFailedIfCommandWasExpired = "UPDATE command "
      + "SET status = 'FAILED' "
      + "WHERE expiration_time <= :expiration_date AND status = 'NEW'";

  String queryFindAppropriateCommand = "SELECT * FROM command "
      + "WHERE data LIKE concat(:prefix, '%', :suffix) AND status = 'NEW'";

  @Modifying
  @Transactional
  @Query(value = querySetStatusFailedIfCommandWasExpired, nativeQuery = true)
  void setStatusFailedIfCommandWasExpired(
      @Param("expiration_date") LocalDateTime currentLocalDateTime);

  @Query(value = queryFindAppropriateCommand, nativeQuery = true)
  List<Command> findAppropriateCommand(@Param("prefix") String prefix, @Param("suffix") String suffix);
}
