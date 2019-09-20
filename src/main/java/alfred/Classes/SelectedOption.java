package alfred.Classes;

public class SelectedOption {
    private String value;

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%nvalue: %s", value);
    }
}
