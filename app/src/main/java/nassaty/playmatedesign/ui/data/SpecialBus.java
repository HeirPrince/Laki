package nassaty.playmatedesign.ui.data;

/**
 * Created by Prince on 11/20/2017.
 */

public class SpecialBus {
    private String cardNumber;
    private String cardExpiry;
    private String cardCvv;
    private String opponent;
    private int amt;
    private int qty;
    private int pos;

    public SpecialBus() {
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public String getCardCvv() {
        return cardCvv;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public int getAmt() {
        return amt;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
