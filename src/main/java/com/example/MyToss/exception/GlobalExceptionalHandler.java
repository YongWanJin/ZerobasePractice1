package com.example.MyToss.exception;

import com.example.MyToss.Type.ErrorCode;
import com.example.MyToss.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.MyToss.Type.ErrorCode.INVALID_ID_REQUEST;
import static com.example.MyToss.Type.ErrorCode.UNKNOWN_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionalHandler {

    // AccountException 발생시
    // 에러코드 및 에러메시지를 로그와 응답Response으로 리턴
    @ExceptionHandler(AccountException.class)
    public ErrorResponse hadleAccountException(AccountException e){
        log.error("{} is ocurred.", e.getErrorCode());
        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }

    // DB에 데이터를 저장하려는데 pk키가 중복될때
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ErrorResponse dataIntegrityViolationException(DataIntegrityViolationException e){
//        log.error("DataIntegrityViolationException is occurred");
//        return new ErrorResponse(e.getE);
//    }

    // 계좌 추가 시, 최초 잔액을 1000원 미만으로 설정하고 요청했을때
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse MethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException is occurred");
        return new ErrorResponse(INVALID_ID_REQUEST, INVALID_ID_REQUEST.getDescription());
    }



    // 그밖에 모든 예상치 못한 예외 발생시
    // 에러코드 및 에러메시지를 로그와 응답Response으로 리턴
    @ExceptionHandler(Exception.class)
    public ErrorResponse hadleException(Exception e){
        log.error("{} is ocurred.", e);
        // 에러코드와 에러메시지를 응답Response으로 리턴
        return new ErrorResponse(UNKNOWN_ERROR, UNKNOWN_ERROR.getDescription());
    }
}
