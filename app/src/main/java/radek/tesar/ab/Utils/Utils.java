package radek.tesar.ab.Utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by tesar on 28.04.2017.
 */

public class Utils {

    /**
     * Method make from BigDecimal string with currency
     * @param value
     * @return
     */
    public static String convertWithoutRounding(BigDecimal value) {
        Locale locale;
        locale = new Locale("cs","CZ");
        NumberFormat f = NumberFormat.getCurrencyInstance(locale);
        return f.format(value);
    }
}
