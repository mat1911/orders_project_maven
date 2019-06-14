package converters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.AppException;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class JsonConverter<T> {

    private final String jsonFilename;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public JsonConverter(String jsonFilename) {
        this.jsonFilename = jsonFilename;
    }

    // conversion from object to json
    public void toJson(final T element) {
        try (FileWriter fileWriter = new FileWriter(jsonFilename)) {
            gson.toJson(element, fileWriter);
        } catch (Exception e) {
            throw new AppException("TO JSON - JSON FILENAME EXCEPTION");
        }
    }

    // conversion from json to object
    public Optional<T> fromJson() {
        try (FileReader fileReader = new FileReader(jsonFilename)) {
            return Optional.of(gson.fromJson(fileReader, type));
        } catch (Exception e) {
            throw new AppException("FROM JSON - JSON FILENAME EXCEPTION");
        }
    }
}
