package com.github.yhnatiuk.gpotechtask.service.dto;

import org.hibernate.validator.constraints.Length;

public class ResponseDto {

  private Long id;

  @Length(min = 80, max = 80)
  private String data;

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
}
