package fr.sparadrah.ecf.utils.exception;

public class StockInsuffisantException extends IllegalArgumentException {
    public StockInsuffisantException(String message) {
        super(message);
    }
}
