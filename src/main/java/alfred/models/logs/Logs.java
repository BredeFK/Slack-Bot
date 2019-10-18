package alfred.models.logs;

import alfred.models.ipify.IPInfo;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "USER_IP")
    private String userIp;

    @Column(name = "ERROR_TYPE")
    private int errorType;

    @Column(name = "URL_SUFFIX")
    private String urlSuffix;

    @Column(name = "USER_AGENT")
    private String userAgent;

    @Column(name = "DATE")
    private String date;

    public Logs() {

    }

    public Logs(String userIp, int errorType, String urlSuffix, String userAgent, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss Z");
        this.userIp = userIp;
        this.errorType = errorType;
        this.urlSuffix = urlSuffix;
        this.userAgent = userAgent;
        this.date = dateFormat.format(date);
    }

    public long getId() {
        return id;
    }

    public String getUserIp() {
        return userIp;
    }

    public int getErrorType() {
        return errorType;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("id: %d%nuserIp: %s%nerrorType: %s%nurlSuffix: %s%ndate: %s"
                , id, userIp, errorType, urlSuffix, date);
    }
}
