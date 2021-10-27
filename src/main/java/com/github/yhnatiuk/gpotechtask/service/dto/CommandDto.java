package com.github.yhnatiuk.gpotechtask.service.dto;


import com.github.yhnatiuk.gpotechtask.domain.CommandStatus;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Length;

public class CommandDto {

  private Long id;

  @Length(min = 80, max = 80)
  private String data;

  private LocalDateTime expirationDateTime;

  private CommandStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public LocalDateTime getExpirationDateTime() {
    return expirationDateTime;
  }

  public void setExpirationDateTime(LocalDateTime expirationDateTime) {
    this.expirationDateTime = expirationDateTime;
  }

  public CommandStatus getStatus() {
    return status;
  }

  public void setStatus(CommandStatus status) {
    this.status = status;
  }
}
