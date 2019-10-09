package alfred.models.ipify;

public class Location {
    private String ip;
    private String country;
    private String region;
    private String city;
    private String timezone;

    public Location(String ip) {
        this.ip = ip;

    }

    public Location(String ip, String country, String region, String city, String timezone) {
        this.ip = ip;
        this.country = country;
        this.region = region;
        this.city = city;
        this.timezone = timezone;
    }

    public String getIp() {
        return ip;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getTimezone() {
        return timezone;
    }
}
