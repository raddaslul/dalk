package com.dalk.exception;

import com.dalk.exception.ex.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("U001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordContainsUsernameException.class)
    public ResponseEntity<ErrorResponse> handlePasswordContainsUsernameException(PasswordContainsUsernameException e) {
        return new ResponseEntity<>(new ErrorResponse("U002", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameException(DuplicateUsernameException e) {
        return new ResponseEntity<>(new ErrorResponse("U003", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicationNicknameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateNicknameException(DuplicationNicknameException e) {
        return new ResponseEntity<>(new ErrorResponse("U004", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordNotEqualException.class)
    public ResponseEntity<ErrorResponse> handlePasswordNotEqualException(PasswordNotEqualException e) {
        return new ResponseEntity<>(new ErrorResponse("U005", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLoginUserNotFoundException(LoginUserNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("U006", e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalTokenUsernameException.class)
    public ResponseEntity<ErrorResponse> handleIllegalTokenUsernameException(IllegalTokenUsernameException e) {
        return new ResponseEntity<>(new ErrorResponse("T001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalTokenUsernameDateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalTokenDateException(IllegalTokenUsernameDateException e) {
        return new ResponseEntity<>(new ErrorResponse("T002", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalTokenUserIdException.class)
    public ResponseEntity<ErrorResponse> handleIllegalTokenUserIdException(IllegalTokenUserIdException e) {
        return new ResponseEntity<>(new ErrorResponse("T003", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalTokenUserIdDateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalTokenUserIdDateException(IllegalTokenUserIdDateException e) {
        return new ResponseEntity<>(new ErrorResponse("T004", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBoardNotFoundException(BoardNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("P001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalBoardUpdateUserException.class)
    public ResponseEntity<ErrorResponse> handleIllegalBoardUpdateUserException(IllegalBoardUpdateUserException e) {
        return new ResponseEntity<>(new ErrorResponse("P002", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalPostDeleteUserException.class)
    public ResponseEntity<ErrorResponse> handleIllegalPostDeleteUserException(IllegalPostDeleteUserException e) {
        return new ResponseEntity<>(new ErrorResponse("P003", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotFoundException(CommentNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("C001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalCommentUpdateUserException.class)
    public ResponseEntity<ErrorResponse> handleIllegalCommentUpdateUserException(IllegalCommentUpdateUserException e) {
        return new ResponseEntity<>(new ErrorResponse("C002", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalCommentDeleteUserException.class)
    public ResponseEntity<ErrorResponse> handleIllegalCommentDeleteUserException(IllegalCommentDeleteUserException e) {
        return new ResponseEntity<>(new ErrorResponse("C003", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(ItemNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("I001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleChatRoomNotFoundException(ChatRoomNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("CR001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                , HttpStatus.BAD_REQUEST);
    }


}