package br.com.appbase.view.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtil {

    public String getData (String formato){
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
        String dataFormatada = sdf.format(hora);

        return dataFormatada;
    }
}
