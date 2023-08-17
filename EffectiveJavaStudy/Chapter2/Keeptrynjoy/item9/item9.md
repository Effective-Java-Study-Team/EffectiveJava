# 아이템 9. try-finally 보다는 try-with-resources를 사용하라

## try-finally는 디버깅을 어렵게 한다.

- 전통적으로 사용된 try-finally는 자원이 둘 이상이면 코드가 지저분해지고, 내부 자원에서 발생한 예외를 외부 자원에서 집어삼켜버려 스택추적내역에 내부 자원에서 발생된 예외가 남지 않게 된다. 외부자원 대신 내부자원의 예외를 기록하도록 로직을 구성할 수 있지만, 코드가 너무 지저분해지기 때문에 실제로 그렇게까지 하는 경우는 거의 없다.
- 자바 7에서 추가된 try-with-resoutrces로 위 문제가 해결 가능하다.
    - AutoCloseable 인터페이스 구현을 통해 해당 구조를 사용할 수 있다.

    ```java
    private static final byte BUFFER_SIZE = 31;
    
        static String firstLineOfFile(String path) throws IOException{
    
            try(BufferedReader br = new BufferedReader(
                    new FileReader(path))){
                return br.readLine(); // close에서 발생한 예외는 숨겨지고 leadLine에서 발생한 예외가 기록된다.
            }
        }
    
        static void copy(String src, String dst) throws IOException{
            try (InputStream in = new FileInputStream(src);
                 OutputStream out = new FileOutputStream(dst)){
    
                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0)
                    out.write(buf, 0, n);
            }
        }
    ```

    - 프로그래머에거 보여줄 예외 하나만 보존되거 여러 개의 다른 예외가 숨겨질 수 있다. 숨겨진 예외들은 버려지지 않고, 스택 추적 내역에 ‘숨겨졌다(suppressed)’는 꼬리표를 달고 출력된다.
    - 자바 7에서 Throwable에 추가된 getSuppressed 메서드를 이용하면 프로그램 코드를 가져올 수도 있다.

## 정리

- 꼭 회수해야 하는 자원을 다룰 때는 try-finally 말고, try-with-resoureces를 사용하자. 코드는 더 짧고 분명해지며, 만들어지는 예외 정보는 훨씬 유용하다.