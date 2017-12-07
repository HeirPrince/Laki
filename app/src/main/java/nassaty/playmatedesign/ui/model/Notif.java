package nassaty.playmatedesign.ui.model;

/**
 * Created by Prince on 7/29/2017.
 */

public class Notif {

    private String sender;
    private String receiver;
    private String message;
    private Boolean isRead;
    private String type;
    private int sender_position;

    public Notif() {
    }

    public Notif(String sender, String receiver, String message, Boolean isRead, String type, int sender_position) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isRead = isRead;
        this.type = type;
        this.sender_position = sender_position;
    }

    public int getSender_position() {
        return sender_position;
    }

    public void setSender_position(int sender_position) {
        this.sender_position = sender_position;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}