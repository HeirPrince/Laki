package nassaty.playmatedesign.ui.helpers;

/**
 * Created by Prince on 7/25/2017.
 */

public class UrlManager {

    public static final String host = "http://192.168.42.141/";
    public static final String signInUrl = host+"roulette/php/login.php";
    public static final String signUpUrl = host+"roulette/php/register.php";
    public static final String readUrl = host+"roulette/php/crude/recup/recuptab.php";
    public static final String writeUrl = host+"roulette/php/crude/insert/insertOperation.php";

    public static String getHost() {
        return host;
    }

    public static String getSignInUrl() {
        return signInUrl;
    }

    public static String getWriteUrl() {
        return writeUrl;
    }

    public static String getReadUrl() {
        return readUrl;
    }

    public static String getSignUpUrl() {
        return signUpUrl;
    }

}
