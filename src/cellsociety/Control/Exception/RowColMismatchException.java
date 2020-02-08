package cellsociety.Control.Exception;

public class RowColMismatchException extends Exception {

    public RowColMismatchException(String errorMessage){
        super(errorMessage);
    }

    public RowColMismatchException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
    }
}
