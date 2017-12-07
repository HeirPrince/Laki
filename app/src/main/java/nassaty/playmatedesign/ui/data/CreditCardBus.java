package nassaty.playmatedesign.ui.data;

/**
 * Created by Prince on 11/12/2017.
 */

public class CreditCardBus {

    private String cardNumber;
    private String cardExpiry;
    private String cardCvv;

    public CreditCardBus(String cardNumber, String cardExpiry, String cardCvv) {
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardCvv = cardCvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public String getCardCvv() {
        return cardCvv;
    }
}
