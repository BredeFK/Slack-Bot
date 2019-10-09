package alfred.models.ipify;

public class IPinfo {
    private String ip;
    private Location location;

    public IPinfo() {

    }

    public IPinfo(String ip, String country, String region, String city, String timezone) {
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
