package com.github.yhnatiuk.gpotechtask;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import com.github.yhnatiuk.gpotechtask.domain.CommandStatus;
import com.github.yhnatiuk.gpotechtask.scheduled_task.ScheduledTask;
import com.github.yhnatiuk.gpotechtask.service.dto.CommandDto;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;

public class ScheduledTaskTest extends AbstractTest {

  @Value("${spring.schedule.period}")
  private String period;

  @SpyBean
  private ScheduledTask spyScheduledTask;

  @Autowired
  private ScheduledTask realScheduledTask;

  @Test
  public void scheduledTaskRanSuccessfully() {
    await()
        .timeout(new Duration(Long.parseLong(period), TimeUnit.MILLISECONDS))
        .untilAsserted(() -> verify(spyScheduledTask, atLeast(1)).checkCommandExpiration());
  }

  @Test
  public void commandSetStatusToFailedIn30SecsSuccessfully() throws InterruptedException {
    insertCommand(commandData);
    Thread.sleep(Long.parseLong(period) + 100);
    realScheduledTask.checkCommandExpiration();
    List<CommandDto> allCommands = commandService.getAllCommands();
    assertThat(allCommands.size(), equalTo(1));
    assertThat("Command has wrong status", allCommands.get(0).getStatus(),
        equalTo(CommandStatus.FAILED));
    commandRepository.deleteAll();
  }

  private CommandDto insertCommand(String data) {
    CommandDto commandDto = new CommandDto();
    commandDto.setData(data);
    return commandService.addCommand(commandDto);
  }
}
