package alfred.models;

import javax.persistence.*;

@Entity
public class Error {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
