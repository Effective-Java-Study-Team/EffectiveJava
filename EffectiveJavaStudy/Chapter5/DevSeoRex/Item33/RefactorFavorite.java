import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RefactorFavorite {

    private Map<Type, Object> favorites = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void putFavorite(TypeReference<?> typeReference, T instance) {
        favorites.put(Objects.requireNonNull(typeReference.type()), instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(TypeReference<T> typeReference){
        Type type = typeReference.type();

        if(type instanceof Class<?>) {
            return ((Class<T>) type).cast(favorites.get(typeReference));
        }

        return ((Class<T>)((ParameterizedType)type).getRawType())
                .cast(favorites.get(type));
    }
}
