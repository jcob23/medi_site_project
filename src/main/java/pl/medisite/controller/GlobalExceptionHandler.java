package pl.medisite.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        String message = String.format("Wystąpił błąd: [%s]", ex.getMessage());
        log.error(message, ex);
        ModelAndView modelView = new ModelAndView("error");
        modelView.addObject("errorMessage", message);
        return modelView;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBindException(BindException ex) {
        String message;
        if( "password".equals(ex.getFieldError().getField()) ) {
            message = "Za krótkie hasło!";
        } else if("appointmentDate".equals(ex.getFieldError().getField())) {
            message = String.format("Data [%s] już się odbyła, nie można dodać spotkania",
                    Optional.ofNullable(ex.getFieldError()).map(FieldError::getRejectedValue).orElse(null));
        } else if( "appointmentTimeEnd".equals(ex.getFieldError().getField()) ) {
            message = "Czas zakończenia spotkania nie może być wcześniejszy niż czas rozpoczęcia.";
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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleConstraintViolationException(AccessDeniedException ex) {
        String message = "Użytkownik o takim adresie email już istnieje!";
        log.error(message, ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleForbiddenError(AccessDeniedException ex) {
        String message = "Odmowa dostępu, brak odpowiednich uprawnień";
        log.error(message, ex);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }





}
