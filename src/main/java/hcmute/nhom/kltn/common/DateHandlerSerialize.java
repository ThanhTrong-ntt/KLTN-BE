package hcmute.nhom.kltn.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class DateHandlerSerialize.
 *
 * @author: ThanhTrong
 **/
public class DateHandlerSerialize extends JsonSerializer<Date> {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formattedDate = formatter.format(date);
        gen.writeString(formattedDate);
    }
}
