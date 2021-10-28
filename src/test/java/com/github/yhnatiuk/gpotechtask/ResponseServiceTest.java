package com.github.yhnatiuk.gpotechtask;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.github.yhnatiuk.gpotechtask.domain.CommandStatus;
import com.github.yhnatiuk.gpotechtask.service.dto.CommandDto;
import com.github.yhnatiuk.gpotechtask.service.dto.ResponseDto;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ResponseServiceTest extends AbstractTest {

  @Test
  public void commandStatusSetToLinkedIfResponsePrefixAndSuffixTheSameAsInCommand() {
    CommandDto addedCommand = insertCommand(commandData);

    insertResponse(responseData);

    List<ResponseDto> allResponses = responseService.getAllResponse();
    assertThat(allResponses.size(), equalTo(1));
    assertThat(allResponses.get(0).getData(), equalTo(responseData));

    List<CommandDto> allCommands = commandService.getAllCommands();
    assertThat(allCommands.size(), equalTo(1));
    assertThat(allCommands.get(0).getId(), equalTo(addedCommand.getId()));
    assertThat("Command has wrong status", allCommands.get(0).getStatus(),
        equalTo(CommandStatus.LINKED));
    commandRepository.deleteAll();
    responseRepository.deleteAll();
  }

  @Test
  public void obtainAllResponsesSuccessfully() {
    insertResponse(responseData);
    insertResponse(responseData);
    List<ResponseDto> allResponses = responseService.getAllResponse();
    assertThat(allResponses.size(), equalTo(2));
    responseRepository.deleteAll();
  }

  private ResponseDto insertResponse(String responseData) {
    ResponseDto responseDto = new ResponseDto();
    responseDto.setData(responseData);
    return responseService.addResponse(responseDto);
  }

  private CommandDto insertCommand(String data) {
    CommandDto commandDto = new CommandDto();
    commandDto.setData(data);
    return commandService.addCommand(commandDto);
  }
}
