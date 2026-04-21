package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_USER("이미 존재하는 유저입니다.", HttpStatus.CONFLICT),

    DUPLICATE_CHANNEL("이미 존재하는 채널입니다.", HttpStatus.CONFLICT),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다.", HttpStatus.BAD_REQUEST),
    EMPTY_PARTICIPANT_LIST("최소 1명 이상의 참여자가 필요합니다", HttpStatus.BAD_REQUEST),

    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    DUPLICATE_USERSTATUS("이미 존재하는 유저 상태입니다.", HttpStatus.CONFLICT),
    USERSTATUS_NOT_FOUND("유저 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    DUPLICATE_READSTATUS("이미 존재하는 읽음 상태입니다.", HttpStatus.CONFLICT),
    READSTATUS_NOT_FOUND("읽음 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    BINARYCONTENT_NOT_FOUND("파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    UNAUTHORIZED("권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS("로그인에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    FILE_UPLOAD_ERROR("파일 업로드 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR("알 수 없는 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
