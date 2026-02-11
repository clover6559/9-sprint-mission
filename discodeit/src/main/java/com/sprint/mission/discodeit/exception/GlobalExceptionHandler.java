//package com.sprint.mission.discodeit.exception;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(IllegalArgumentException.class)
//    public String handleIllegalArgumentException(
//            IllegalArgumentException e, HttpServletRequest request, Model model) {
//        model.addAttribute("status",400);
//        model.addAttribute("error", "잘못된 요청입니다.");
//        model.addAttribute("message", e.getMessage());
//        model.addAttribute("path", request.getRequestURI());
//        return "error/custom-error";
//    }
//
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleUserNotFount(UserNotFoundException){
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body()
//    }
//}
