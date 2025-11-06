package fr.sparadrap.ecf.utils.exception;

public class StockInsuffisantException extends IllegalArgumentException {
    public StockInsuffisantException(String message) {
        super(message);
    }
}
