package alfred.Classes;

public class License {

    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("\nname: %s", name);
    }
}
