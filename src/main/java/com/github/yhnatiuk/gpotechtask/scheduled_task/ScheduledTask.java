package com.github.yhnatiuk.gpotechtask.scheduled_task;


import com.github.yhnatiuk.gpotechtask.repository.CommandRepository;
import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledTask {

  private CommandRepository commandRepository;

  public ScheduledTask(CommandRepository commandRepository) {
    this.commandRepository = commandRepository;
  }

  @Scheduled(fixedRate = 300000)
  private void checkCommandExpiration() {
    commandRepository.setStatusFailedIfCommandWasExpired(LocalDateTime.now());
  }
}
