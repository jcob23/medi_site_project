package pl.medisite.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;
import java.util.Optional;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //Zrobić że jeżeli mail jest poprawny, a hasło błędne to pojawi sie odpowiedni komunikat
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBindException(BindException ex){
        String message;
        if("password".equals(ex.getFieldError().getField())){
            message = "Za krótkie hasło!";
        }else{

            message = String.format("Błędne żądanie dla pola: [%s], nieprawidłowa wartość: [%s].",
                    Optional.ofNullable(ex.getFieldError()).map(FieldError::getField).orElse(null),
                    Optional.ofNullable(ex.getFieldError()).map(FieldError::getRejectedValue).orElse(null)
            );
        }
        log.error(message,ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }

    // obsługa błędu 403
//    https://mkyong.com/spring-security/customize-http-403-access-denied-page-in-spring-security/
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleForbiddenError(AccessDeniedException ex){
        String message = "Odmowa dostępu, brak odpowiednich uprawnień";
        log.error(message,ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBadRequestException(BadRequestException ex){
        String message = "nie można odwołać wizyty z przeszłości";
        log.error(message,ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }








}
