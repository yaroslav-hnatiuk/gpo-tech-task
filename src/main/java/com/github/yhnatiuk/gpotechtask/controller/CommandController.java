package com.github.yhnatiuk.gpotechtask.controller;

import com.github.yhnatiuk.gpotechtask.service.CommandService;
import com.github.yhnatiuk.gpotechtask.service.ResponseService;
import com.github.yhnatiuk.gpotechtask.service.dto.CommandDto;
import com.github.yhnatiuk.gpotechtask.service.dto.ResponseDto;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class CommandController {

  private final CommandService commandService;

  private final ResponseService responseService;

  public CommandController(CommandService commandService, ResponseService responseService) {
    this.commandService = commandService;
    this.responseService = responseService;
  }

  @RolesAllowed({"ROLE_ADMIN"})
  @PostMapping(path = "/command")
  public ResponseEntity<CommandDto> postCommand(@Valid @RequestBody CommandDto commandDto) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(commandService.addCommand(commandDto));
  }

  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping(path = "/command")
  public ResponseEntity<List<CommandDto>> getAllCommand() {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(commandService.getAllCommands());
  }

  @RolesAllowed({"ROLE_ADMIN"})
  @PostMapping(path = "/response")
  public ResponseEntity<ResponseDto> postResponse(@Valid @RequestBody ResponseDto responseDto) {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseService.addResponse(responseDto));
  }

  @RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
  @GetMapping(path = "/response")
  public ResponseEntity<List<ResponseDto>> getAllResponses() {
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseService.getAllResponse());
  }
}
