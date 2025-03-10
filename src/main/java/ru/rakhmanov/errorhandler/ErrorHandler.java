package ru.rakhmanov.errorhandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rakhmanov.exception.NotFoundException;

@ControllerAdvice("ru.rakhmanov")
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex) {
        return "blog/error404";
    }

    @ExceptionHandler(Exception.class)
    public String handleIternalServerError(Exception ex) {
        return "blog/error";
    }

}
