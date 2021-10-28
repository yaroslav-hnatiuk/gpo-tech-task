package com.github.yhnatiuk.gpotechtask;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.github.yhnatiuk.gpotechtask.domain.CommandStatus;
import com.github.yhnatiuk.gpotechtask.service.dto.CommandDto;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CommandServiceTest extends AbstractTest {

  @Test
  public void commandHasStatusWaitingAfterCreationSuccess() {
    CommandDto addedCommand = insertCommand(commandData);
    List<CommandDto> allCommands = commandService.getAllCommands();
    assertThat(allCommands.size(), equalTo(1));
    assertThat(allCommands.get(0).getId(), equalTo(addedCommand.getId()));
    assertThat("Command has wrong status", allCommands.get(0).getStatus(),
        equalTo(CommandStatus.WAITING));
    commandRepository.deleteAll();
  }

  @Test
  public void obtainAllCommandsSuccessfully() {
    insertCommand(commandData);
    insertCommand(commandData);
    List<CommandDto> allCommands = commandService.getAllCommands();
    assertThat(allCommands.size(), equalTo(2));
    commandRepository.deleteAll();
  }

  private CommandDto insertCommand(String data) {
    CommandDto commandDto = new CommandDto();
    commandDto.setData(data);
    return commandService.addCommand(commandDto);
  }
}
