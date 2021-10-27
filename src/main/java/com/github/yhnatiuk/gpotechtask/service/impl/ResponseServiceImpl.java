package com.github.yhnatiuk.gpotechtask.service.impl;

import static java.util.Objects.nonNull;

import com.github.yhnatiuk.gpotechtask.domain.Command;
import com.github.yhnatiuk.gpotechtask.domain.CommandStatus;
import com.github.yhnatiuk.gpotechtask.domain.Response;
import com.github.yhnatiuk.gpotechtask.repository.CommandRepository;
import com.github.yhnatiuk.gpotechtask.repository.ResponseRepository;
import com.github.yhnatiuk.gpotechtask.service.ResponseService;
import com.github.yhnatiuk.gpotechtask.service.dto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponseService {

  private ResponseRepository responseRepository;

  private CommandRepository commandRepository;

  public ResponseServiceImpl(ResponseRepository responseRepository, CommandRepository commandRepository) {
    this.responseRepository = responseRepository;
    this.commandRepository = commandRepository;
  }


  @Override
  public ResponseDto addResponse(ResponseDto responseDto) {
    Response response = responseRepository.save(toDomain(responseDto));
    String prefix = responseDto.getData().substring(0, 8);
    String suffix = responseDto.getData().substring(responseDto.getData().length() - 10);
    Command command = commandRepository.findAppropriateCommand(prefix, suffix);
    if (nonNull(command)){
      command.setStatus(CommandStatus.LINKED);
      commandRepository.save(command);
    }
    return toDto(response);
  }

  private Response toDomain(ResponseDto responseDto){
    Response response = new Response();
    response.setId(responseDto.getId());
    response.setData(responseDto.getData());
    return response;
  }

  private ResponseDto toDto(Response response){
    ResponseDto responseDto = new ResponseDto();
    responseDto.setId(response.getId());
    responseDto.setData(response.getData());
    return responseDto;
  }
}
