package nassaty.playmatedesign.ui.model;

/**
 * Created by Prince on 11/30/2017.
 */

public class Player {

    private String phone;
    private int position;
    private String cardNumber;
    private String cardCVV;
    private String cardEXP;
    private int amount;

    public Player() {
    }

    public Player(String phone, int position, String cardNumber, String cardCVV, String cardEXP, int amount) {
        this.phone = phone;
        this.position = position;
        this.cardNumber = cardNumber;
        this.cardCVV = cardCVV;
        this.cardEXP = cardEXP;
        this.amount = amount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    public String getCardEXP() {
        return cardEXP;
    }

    public void setCardEXP(String cardEXP) {
        this.cardEXP = cardEXP;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
