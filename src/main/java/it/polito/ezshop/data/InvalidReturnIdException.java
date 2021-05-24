package it.polito.ezshop.data;

public class InvalidReturnIdException extends Exception {
    public InvalidReturnIdException() {
        super();
    }

    public InvalidReturnIdException(String msg) {
        super(msg);
    }
}
