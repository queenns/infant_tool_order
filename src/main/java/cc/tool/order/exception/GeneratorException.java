package cc.tool.order.exception;

/**
 * Created by lxj on 18-5-3
 */
public class GeneratorException extends RuntimeException {

    public GeneratorException(String message) {

        super(message);

    }

    public GeneratorException(String message, Exception e) {

        super(message);

        initCause(e.getCause());

        setStackTrace(e.getStackTrace());

    }

}
