package us.sep.biz.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import us.sep.util.exceptions.CustomizeException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex, request.getRequestURI());
        log.error("occur BaseException:" + errorResponse.toString());
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(errorResponse);
    }

    /**
     * 请求参数异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class )
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
       Map<String, Object> errors = new HashMap<>(2);

       /* ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);

        });*/
        errors.put("errorMesssage", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.METHOD_ARGUMENT_NOT_VALID, request.getRequestURI(), errors);
        log.error("occur MethodArgumentNotValidException:" + errorResponse.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(BindException.class )
    public ResponseEntity<ErrorResponse> handleBindException(BindException ex, HttpServletRequest request) {
        Map<String, Object> errors = new HashMap<>(2);

       /* ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);

        });*/
        errors.put("errorMesssage", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.METHOD_ARGUMENT_NOT_VALID, request.getRequestURI(), errors);
        log.error("occur BindException:" + errorResponse.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(value = UserNameAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserNameAlreadyExistException(UserNameAlreadyExistException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex, request.getRequestURI());
        log.error("occur UserNameAlreadyExistException:" + errorResponse.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = {RoleNotFoundException.class, UserNameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(BaseException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex, request.getRequestURI());
        log.error("occur ResourceNotFoundException:" + errorResponse.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(value = {RoleAlreadyExistException.class})
    public ResponseEntity<ErrorResponse> handleRoleAlreadyExistException(BaseException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex, request.getRequestURI());
        log.error("occur RoleAlreadyExistException:" + errorResponse.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = {CustomizeException.class})
    public ResponseEntity<ErrorResponse> handleCustomizeException(CustomizeException ex, HttpServletRequest request) {
        Map<String,Object> map = new HashMap(4);
        map.put("message",ex.getErrorCode());
        ErrorResponse errorResponse = new ErrorResponse(ex, request.getRequestURI(),map);
        log.error("occur CustomizeException:" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
