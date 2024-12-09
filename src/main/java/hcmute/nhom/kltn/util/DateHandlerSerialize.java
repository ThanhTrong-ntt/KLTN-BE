package hcmute.nhom.kltn.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class DateHandlerSerialize.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public class DateHandlerSerialize extends StdSerializer<Date> {


    public DateHandlerSerialize() {
        this(null);
    }

    protected DateHandlerSerialize(Class<Date> t) {
        super(t);
    }
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        jsonGenerator.writeString(dateFormat.format(date));
    }
}
