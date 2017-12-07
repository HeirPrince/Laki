package nassaty.playmatedesign.ui.model;

/**
 * Created by Prince on 11/3/2017.
 */

public class CreditCard {

    private String holderPhone;
    private String holderName;
    private String holderNumber;
    private String cvv;
    private String expiry;

    public CreditCard() {
    }

    public CreditCard(String holderPhone, String holderName, String holderNumber, String cvv, String expiry) {
        this.holderPhone = holderPhone;
        this.holderName = holderName;
        this.holderNumber = holderNumber;
        this.cvv = cvv;
        this.expiry = expiry;
    }

    public String getHolderPhone() {
        return holderPhone;
    }

    public void setHolderPhone(String holderPhone) {
        this.holderPhone = holderPhone;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getHolderNumber() {
        return holderNumber;
    }

    public void setHolderNumber(String holderNumber) {
        this.holderNumber = holderNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}
