package hcmute.nhom.kltn.common;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class DateHandler.
 *
 * @author: ThanhTrong
 **/
public class DateHandler extends JsonDeserializer<Date> {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText();
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date: " + date, e);
        }
    }
}
