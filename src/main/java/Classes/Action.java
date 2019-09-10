package Classes;

public class Action {
    private SelectedOption selected_option;

    public SelectedOption getSelected_option() {
        return selected_option;
    }

    @Override
    public String toString() {
        return "\nselected:option: {\n" + selected_option.toString() + "\n}";
    }
}
