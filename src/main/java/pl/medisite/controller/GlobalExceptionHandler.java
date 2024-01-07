package pl.medisite.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;
import java.util.Optional;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


//    @ExceptionHandler(Exception.class)
//    public ModelAndView handleException(Exception ex) {
//        String message = String.format("Wystąpił błąd: [%s]", ex.getMessage());
//        log.error(message, ex);
//        ModelAndView modelView = new ModelAndView("error");
//        modelView.addObject("errorMessage", message);
//        return modelView;
//    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBindException(BindException ex) {
        String message;
        if( "password".equals(ex.getFieldError().getField()) ) {
            message = "Za krótkie hasło!";
        } else {
            message = String.format("Błędne żądanie dla pola: [%s], nieprawidłowa wartość: [%s].",
                    Optional.ofNullable(ex.getFieldError()).map(FieldError::getField).orElse(null),
                    Optional.ofNullable(ex.getFieldError()).map(FieldError::getRejectedValue).orElse(null)
            );
        }
        log.error(message, ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }

    // obsługa błędu 403
//    https://mkyong.com/spring-security/customize-http-403-access-denied-page-in-spring-security/

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBadRequestException(BadRequestException ex) {
        String message = "nie można odwołać wizyty z przeszłości";
        log.error(message, ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }


}
