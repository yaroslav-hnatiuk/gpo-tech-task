package com.github.yhnatiuk.gpotechtask.service;

import com.github.yhnatiuk.gpotechtask.service.dto.CommandDto;
import java.util.List;

public interface CommandService {

  CommandDto addCommand(CommandDto command);

  List<CommandDto> getAllCommands();

}
