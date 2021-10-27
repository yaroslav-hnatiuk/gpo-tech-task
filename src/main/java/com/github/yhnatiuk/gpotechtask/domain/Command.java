package com.github.yhnatiuk.gpotechtask.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity(name = "command")
@Table(name = "command", schema = "command")
public class Command {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "data", length = 80)
  private String data;

  @Column(name = "expiration_time")
  private LocalDateTime expirationDateTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private CommandStatus status;

  public Command() {
  }

  public Command(Long id, String data, LocalDateTime expirationDateTime) {
    this.id = id;
    this.data = data;
    this.expirationDateTime = expirationDateTime;
  }

  public CommandStatus getStatus() {
    return status;
  }

  public void setStatus(CommandStatus status) {
    this.status = status;
  }

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
}
