package request;

public class HTTPRequestException extends Exception{
    private final int statusCode;

    public HTTPRequestException(int statusCode, String message){
        this(statusCode,message, null);
    }

    public HTTPRequestException(int statusCode, String message, Throwable cause){
        super(message,cause);
        this.statusCode = statusCode;
    }
}
