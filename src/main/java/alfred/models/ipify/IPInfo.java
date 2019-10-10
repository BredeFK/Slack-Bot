package alfred.models.ipify;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class IPInfo {
    @Id
    @Column(name = "ip", updatable = false, nullable = false)
    private String ip;

    @Embedded
    private Location location;

    public IPInfo() {

    }

    public IPInfo(String ip, String country, String region, String city, String timezone) {
        this.ip = ip;
        this.location = new Location(country, region, city, timezone);
    }

    public String getIp() {
        return ip;
    }

    public Location getLocation() {
        return location;
    }
}
