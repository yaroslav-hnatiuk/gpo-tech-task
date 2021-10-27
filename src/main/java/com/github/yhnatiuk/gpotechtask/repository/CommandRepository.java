package com.github.yhnatiuk.gpotechtask.repository;

import com.github.yhnatiuk.gpotechtask.domain.Command;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends CrudRepository<Command, Long> {

  String querySetStatusFailedIfCommandWasExpired = "UPDATE command.command "
      + "SET status = 'FAILED' "
      + "WHERE expiration_time <= :current_date AND status = 'WAITING'";

  String queryFindAppropriateCommand = "SELECT * FROM command.command "
      + "WHERE data LIKE concat(:prefix, '%', :suffix) AND status = 'WAITING'";

  @Modifying
  @Transactional
  @Query(value = querySetStatusFailedIfCommandWasExpired, nativeQuery = true)
  void setStatusFailedIfCommandWasExpired(@Param("current_date") LocalDateTime currentLocalDateTime);

  @Query(value = queryFindAppropriateCommand, nativeQuery = true)
  Command findAppropriateCommand(@Param("prefix") String prefix, @Param("suffix") String suffix);
}
