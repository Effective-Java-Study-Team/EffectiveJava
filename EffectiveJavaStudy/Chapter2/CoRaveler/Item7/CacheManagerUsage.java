package CH2.Item7;

import java.util.Map;
import java.util.WeakHashMap;

public class CacheManagerUsage {
    public static void main(String[] args) {
        CacheManager cacheManager = new CacheManager();

        // cache 에 넣을 데이터 생성
        String dataKey = new String("SOMETHING IMPORTANT DATA KEY");
        String dataValue = "SOMETHING IMPORTANT DATA VALUE";

        // 데이터에 캐시 넣기
        cacheManager.put(dataKey, dataValue);

        // 캐시에서 데이터 가져오기
        String retrievedData = (String) cacheManager.get("SOMETHING IMPORTANT DATA KEY");
        System.out.println("Initial cache value : " + retrievedData);

        // dataKey 에 대한 강한 참조를 없애면 GC 에 의해 회수될 수 있음
        dataKey = null;

        // GC 호출
        System.gc();

        // 이 시점에서 "IMPORTANT DATA KEY" 키와 연관된 값은 GC 의 대상이 가능함.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        retrievedData = (String) cacheManager.get("SOMETHING IMPORTANT DATA KEY");
        System.out.println("After GC, cache value : " + retrievedData);
    }
}


class CacheManager{
    // WeakHashMap 를 사용해 객체가 GC의 대상이 되도록 한다.
    private static Map<Object, Object> cache = new WeakHashMap<>();

    public Object get(Object key) {
        return cache.get(key);
    }

    public void put(Object key, Object value){
        cache.put(key, value);
    }
}