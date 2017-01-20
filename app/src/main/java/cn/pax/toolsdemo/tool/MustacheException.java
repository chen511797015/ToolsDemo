package cn.pax.toolsdemo.tool;


public class MustacheException extends RuntimeException
{
    public MustacheException(String message) {
        super(message);
    }

    public MustacheException(Throwable cause) {
        super(cause);
    }

    public MustacheException(String message, Throwable cause) {
        super(message, cause);
    }
}