package alfred.models;

public class User {
    private String id;
    private String username;
    private String name;
    private String team_id;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getTeam_id() {
        return team_id;
    }

    @Override
    public String toString() {
        return String.format("%nid: %s%nusername: %s%nname: %s%neam_id: %s%n",
                id, username, name, team_id);
    }
}
