package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("유저를 찾을 수 없습니다."),
    DUPLICATE_USER("이미 존재하는 유저입니다."),

    DUPLICATE_CHANNEL("이미 존재하는 채널입니다."),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다."),
    EMPTY_PARTICIPANT_LIST("최소 1명 이상의 참여자가 필요합니다"),

    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),

    DUPLICATE_USERSTATUS("이미 존재하는 유저 상태입니다."),
    USERSTATUS_NOT_FOUND("유저 상태를 찾을 수 없습니다."),

    DUPLICATE_READSTATUS("이미 존재하는 읽음 상태입니다."),
    READSTATUS_NOT_FOUND("읽음 상태를 찾을 수 없습니다."),

    BINARYCONTENT_NOT_FOUND("파일을 찾을 수 없습니다."),

    UNAUTHORIZED("권한이 없습니다."),
    INVALID_CREDENTIALS("로그인에 실패했습니다."),
    FILE_UPLOAD_ERROR("파일 업로드 중 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR("알 수 없는 서버 오류가 발생했습니다.");
    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
