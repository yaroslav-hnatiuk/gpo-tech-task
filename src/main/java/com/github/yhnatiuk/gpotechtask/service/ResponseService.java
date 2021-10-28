package com.github.yhnatiuk.gpotechtask.service;

import com.github.yhnatiuk.gpotechtask.service.dto.ResponseDto;
import java.util.List;

public interface ResponseService {

  ResponseDto addResponse(ResponseDto responseDto);

  List<ResponseDto> getAllResponse();

}
