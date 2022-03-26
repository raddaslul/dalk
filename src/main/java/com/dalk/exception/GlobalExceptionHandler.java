package com.dalk.exception;

import com.dalk.exception.ex.*;
import com.dalk.exception.ex.RuntimeException;
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

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameException(DuplicateUsernameException e) {
        return new ResponseEntity<>(new ErrorResponse("U002", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicationNicknameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateNicknameException(DuplicationNicknameException e) {
        return new ResponseEntity<>(new ErrorResponse("U003", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLoginUserNotFoundException(LoginUserNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("U004", e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("U005", e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(WarnDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleWarnDuplicateException(WarnDuplicateException e) {
        return new ResponseEntity<>(new ErrorResponse("U006", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalTokenUsernameException.class)
    public ResponseEntity<ErrorResponse> handleIllegalTokenUsernameException(IllegalTokenUsernameException e) {
        return new ResponseEntity<>(new ErrorResponse("T001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalTokenUserIdException.class)
    public ResponseEntity<ErrorResponse> handleIllegalTokenUserIdException(IllegalTokenUserIdException e) {
        return new ResponseEntity<>(new ErrorResponse("T002", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalTokenDateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalTokenDateException(IllegalTokenDateException e) {
        return new ResponseEntity<>(new ErrorResponse("T003", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBoardNotFoundException(BoardNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("B001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WarnBoardDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleWarnBoardDuplicateException(WarnBoardDuplicateException e) {
        return new ResponseEntity<>(new ErrorResponse("B002", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotFoundException(CommentNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("C001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WarnCommentDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleWarnCommentDuplicateException(WarnCommentDuplicateException e) {
        return new ResponseEntity<>(new ErrorResponse("C002", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(ItemNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("I001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(new ErrorResponse("I002", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LackPointException.class)
    public ResponseEntity<ErrorResponse> handleLackPointException(LackPointException e) {
        return new ResponseEntity<>(new ErrorResponse("P001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleChatRoomNotFoundException(ChatRoomNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("CR001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateChatRoomUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateChatRoomUserException(DuplicateChatRoomUserException e) {
        return new ResponseEntity<>(new ErrorResponse("CR002", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WarnChatRoomDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleWarnChatRoomDuplicateException(WarnChatRoomDuplicateException e) {
        return new ResponseEntity<>(new ErrorResponse("CR003", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateVoteException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateVoteException(DuplicateVoteException e) {
        return new ResponseEntity<>(new ErrorResponse("V001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CarouselNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCarouselNotFoundException(CarouselNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("CS001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoticeNotFoundException(NoticeNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse("N001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LottoCountException.class)
    public ResponseEntity<ErrorResponse> handleLottoCountException(LottoCountException e) {
        return new ResponseEntity<>(new ErrorResponse("L001", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                , HttpStatus.BAD_REQUEST);
    }
}