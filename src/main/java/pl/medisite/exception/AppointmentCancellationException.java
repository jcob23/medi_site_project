package pl.medisite.exception;

public class AppointmentCancellationException extends RuntimeException {
    public AppointmentCancellationException(String message) {
        super(message);
    }
}
