package co.novalist.web;

/**
 * Created by user on 6/13/2017.
 */
public class FlashMessage {

    private String message;
    private Status status;

    public FlashMessage(String message, Status status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return message;
    }

    public enum Status {
        SUCCESS, FAILURE
    }
}
