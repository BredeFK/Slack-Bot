package alfred.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Error {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private int code;

    private String message;

    public Error() {
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", code, message);
    }
}
