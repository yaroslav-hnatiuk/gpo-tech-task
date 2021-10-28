package com.github.yhnatiuk.gpotechtask.scheduled_task;


import com.github.yhnatiuk.gpotechtask.repository.CommandRepository;
import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTask {

  private final CommandRepository commandRepository;

  public ScheduledTask(CommandRepository commandRepository) {
    this.commandRepository = commandRepository;
  }

  @Scheduled(fixedRateString = "${spring.schedule.period}")
  public void checkCommandExpiration() {
    commandRepository.setStatusFailedIfCommandWasExpired(LocalDateTime.now());
  }
}
