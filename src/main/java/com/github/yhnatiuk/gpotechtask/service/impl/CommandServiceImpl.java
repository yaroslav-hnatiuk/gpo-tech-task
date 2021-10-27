package com.github.yhnatiuk.gpotechtask.service.impl;

import com.github.yhnatiuk.gpotechtask.domain.Command;
import com.github.yhnatiuk.gpotechtask.domain.CommandStatus;
import com.github.yhnatiuk.gpotechtask.repository.CommandRepository;
import com.github.yhnatiuk.gpotechtask.service.CommandService;
import com.github.yhnatiuk.gpotechtask.service.dto.CommandDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CommandServiceImpl implements CommandService {

  private final CommandRepository commandRepository;

  public CommandServiceImpl(
      CommandRepository commandRepository) {
    this.commandRepository = commandRepository;
  }

  @Override
  public CommandDto addCommand(CommandDto commandDto) {
    commandDto.setExpirationDateTime(LocalDateTime.now().plusSeconds(30));
    commandDto.setStatus(CommandStatus.WAITING);
    Command savedCommand = commandRepository.save(toDomain(commandDto));
    return toDto(savedCommand);
  }

  @Override
  public List<CommandDto> getAllCommands() {
    List<Command> allCommands = new ArrayList<>();
        commandRepository.findAll().forEach(allCommands::add);
    return allCommands.stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  private CommandDto toDto(Command command) {
    CommandDto commandDto = new CommandDto();
    commandDto.setId(command.getId());
    commandDto.setData(command.getData());
    commandDto.setExpirationDateTime(command.getExpirationDateTime());
    commandDto.setStatus(command.getStatus());
    return commandDto;
  }

  private Command toDomain(CommandDto commandDto) {
    Command command = new Command();
    command.setId(commandDto.getId());
    command.setData(commandDto.getData());
    command.setExpirationDateTime(commandDto.getExpirationDateTime());
    command.setStatus(commandDto.getStatus());
    return command;
  }
}
