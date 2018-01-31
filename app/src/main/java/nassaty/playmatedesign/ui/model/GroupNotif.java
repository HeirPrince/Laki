package nassaty.playmatedesign.ui.model;

import java.util.List;

/**
 * Created by Prince on 1/30/2018.
 */

public class GroupNotif {

    private String name;
    private List<String> participants;
    private String image;

    public GroupNotif() {
    }

    public GroupNotif(String name, List<String> participants, String image) {
        this.name = name;
        this.participants = participants;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
