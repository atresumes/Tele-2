package retrofit2.converter.gson;

import com.google.gson.TypeAdapter;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    public T convert(ResponseBody value) throws IOException {
        try {
            T fromJson = this.adapter.fromJson(value.charStream());
            return fromJson;
        } finally {
            value.close();
        }
    }
}
