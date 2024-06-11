package coursemaker.coursemaker.exception;

import coursemaker.coursemaker.domain.member.exception.InvalidPasswordException;
import coursemaker.coursemaker.domain.member.exception.UnauthorizedException;
import coursemaker.coursemaker.domain.member.exception.UserDuplicatedException;
import coursemaker.coursemaker.domain.member.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType("login required");
        response.setMessage("로그인 후 이용이 가능합니다.");
        response.setStatus(401);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType("Illegal argument");
        response.setMessage("인자값이 잘못됬습니다: "+e.getMessage());
        response.setStatus(400);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(RootException.class)
    public ResponseEntity<ErrorResponse> handleRootException(RootException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType(e.getErrorCode().getErrorType());
        response.setMessage(e.getMessage());
        response.setStatus(e.getErrorCode()
                .getStatus()
                .value());

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        ErrorResponse response = new ErrorResponse();

        response.setErrorType("unknown error");
        response.setMessage("예상치 못한 오류가 발생했습니다: "+e.getMessage());
        response.setStatus(400);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        ErrorResponse response = new ErrorResponse();

        response.setErrorType("unknown error");
        response.setMessage("예상치 못한 쌈@뽕한 오류가 발생했습니다: " + e.getMessage());
        response.setStatus(400);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorResponse response = new ErrorResponse();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            response.setMessage(errorMessage);
            response.setStatus(400);
            response.setErrorType("Illegal argument");
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity<String> handleUserDuplicatedException(UserDuplicatedException e) {
        return ResponseEntity
                .status(ErrorCode.DUPLICATED_MEMBER.getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity
                .status(ErrorCode.NOT_FOUND_MEMBER.getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException e) {
        return ResponseEntity
                .status(ErrorCode.UNAUTHORIZED_MEMBER.getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorType(ErrorCode.PICTURE_OVER_SIZE.getErrorType());
        response.setMessage(ErrorCode.PICTURE_OVER_SIZE.getDescription());
        response.setStatus(ErrorCode.PICTURE_OVER_SIZE.getStatus().value());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
