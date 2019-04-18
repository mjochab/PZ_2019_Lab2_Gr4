
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Veth
 */
public class Utils {
    //data gotowa do wstawienia przez nauczyciela
    public static Date returnDate(String data) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
        String dateString = data;
        Date date = format.parse(dateString);
        return date;
    
    }
}
