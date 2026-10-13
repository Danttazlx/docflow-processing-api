package Project_Invento.demo.infrastructore.exception.exceptions;

public class EtlProcessingException extends RuntimeException {
    public EtlProcessingException(String message, Throwable throwable) {
        super(message);
    }
}
