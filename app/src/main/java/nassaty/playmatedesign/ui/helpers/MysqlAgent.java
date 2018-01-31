package nassaty.playmatedesign.ui.helpers;

/**
 * Created by Prince on 1/2/2018.
 */

public class MysqlAgent {

    public interface Reserve{
        void isReservedListener(Boolean isReserved);
    }

    public void Reserve(Reserve reserve){
        reserve.isReservedListener(true);//volley codes missing
    }

}
