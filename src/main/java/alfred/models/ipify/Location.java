package alfred.models.ipify;

public class Location {
    private String country;
    private String region;
    private String city;
    private String timezone;

    public Location() {


    }

    public Location(String country, String region, String city, String timezone) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.timezone = timezone;
    }

    public String getCountryCode() {
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
