package alfred.models.slack;

public class Action {
    private SelectedOption selected_option;

    public SelectedOption getSelected_option() {
        return selected_option;
    }

    @Override
    public String toString() {
        return String.format("%nselected_option: {%n %s %n}", selected_option.toString());
    }
}
