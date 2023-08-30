package CoRaveler.Item33;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DatabaseRow {
    static class Column<T> {
        @SuppressWarnings("unchecked")
        public T cast(Object obj) {
            return (T) obj;
        }
    }

    private static Map<Column<?>, Object> rows = new HashMap<>();

    public <T> void putColumn(Column<T> type, T instance) {
        rows.put(Objects.requireNonNull(type), instance);
    }

    public <T> T getColumn(Column<T> type) {
        return type.cast(rows.get(type));
    }

    public static void main(String[] args) {
        DatabaseRow dbRow = new DatabaseRow();  // dbRow 는 타입 매개변수를 안 받는다!

        Column<String> strCol = new Column<>();
        Column<Integer> intCol = new Column<>();

        dbRow.putColumn(strCol, "abc");
        dbRow.putColumn(intCol, 42);

        System.out.println(dbRow.getColumn(strCol));
        System.out.println(dbRow.getColumn(intCol));
    }
}
