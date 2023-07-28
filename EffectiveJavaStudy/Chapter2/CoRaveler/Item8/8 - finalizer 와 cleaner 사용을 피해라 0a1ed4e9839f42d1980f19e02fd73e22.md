# 8 - finalizer 와 cleaner 사용을 피해라

상태: Done

자바에는 2가지의 객체 소멸자가 존재한다

- `finalizer()`
- `cleaner()`

<aside>
💡 객체 소멸자란?

객체가 메모리에서 제거될 때 호출되는 함수나 메소드

</aside>

본래의 뜻과 같은 의도의 함수이지만 
객체 소멸자는 기본적으로 `쓰지 말아야` 한다.

왜냐하면 finalizer(), cleaner() 가 언제 실행된다는 보장이
전적으로 GC 의 알고리즘에 달려 있어 프로그래머가 사용한다고 해도 보장을 받지 못하기 때문이다.

그래서 finalizer(), cleaner() 는 기본적으로 쓰지 말라고 하는 데,
더더욱 쓰지 말아야 하는 순간과 상황들이 존재하게 되는 데, 이는 다음 경우들이다.

## 상태를 영구적으로 수정하는 작업

## 성능 문제

## 보안 문제

## 그렇다면 finalizer 와 cleaner 의 대안은 무엇일까?

바로 클래스가 `AutoClosable` 인터페이스를 구현해주고,
사용자가 코드에서 `close()` 를 명시적으로 사용해주면 된다.

<aside>
💡 물론 아는 사람들은 알겠지만 `try-with-resources` 을 사용한다면
close() 를 직접 호출할 필요가 없다!

</aside>

close() 을 구현함에 있어서 물론 이렇게 생각할 수도 있다.

> 그냥 객체가 종료될 때
필요할만한 작업, 즉 코드를 넣으면 되는 것 아니야?
> 

그렇다면 `필요한 작업` 은 무엇일까?
라는 고민을 해봐야 한다.

우리가 finalizer, cleaner 를 사용하지 말라던 이유를 다시 보자.
이는 제대로 자원의 비할당이 제대로 이루어지지 않는 다는 것이 문제였고,
이에 대한 해결책으로 실행됨이 보장되는 close() 를 통해 자원의 비할당을 이루자는 것이었다.

즉, 우리는 AutoCloseable 을 구현하는 데 있어 2 가지 부분을 고려해야 한다.

1. `자원이 아직 할당되어 있을시에만 작업 하기`
2. `만약 자원이 없을 경우, Error 던져주기`

### 2 가지를 고려한 코드 예시

```java
public class ManagedFile implements AutoCloseable {
	private final File file;

	public ManagedFile(String filePath) {
		this.file = new File(filePath);
		// 파일 열기 관련 로직...
	}

	public void doSomethingWithFile() {
		// 파일 관련 작업 코드...
	}

	@Override
	public void close() {
		isClosed = true;
	}
}
```

문제가 없어보이는 평범한 코드같아 보인다.

하지만 위 코드에는 ManagedFile 이 
아직 메모리를 할당받은 상태인가에 대한 고려가 안되어 있기 때문에
메모리에 없는 객체를 참조하는 일이 일어날 수 있다.

따라서 위에서 말한 2 가지의 경우를 고려한 코드로 수정한다면

```java
public class ManagedFile implements AutoCloseable {
	private final File file;
	private boolean isClosed = false;

	public ManagedFile(String filePath) {
		this.file = new File(filePath);
		// 파일 열기 관련 로직...
	}

	public void doSomethingWithFile() {
		if(isClosed) {
			throw new IllegalStateException("File is already closed");
		}

		// 파일 관련 작업 코드...
	}

	@Override
	public void close() {
		if(isClosed) {
			throw new IllegalStateException("File is already closed");
		}

		isClosed = true;
	}
}
```

이와 같을 것이다.

## 그렇다면 대체 finalizer 와 cleaner 는 왜 있을까?

어떻게 보면 없는 게 당연해 보일만큼
단점도 한 가득에다가 이미 close 라는 훌륭한 대안도 있다.

하지만쓸모없어 보이는 finalizer 와 cleaner 에도 
쓰임새가 2 가지 정도 있다. (책에서는 아마도 라는 표현을 사용함)

1. `안정망 역할`
    
    close() 메서드가 finalizer, cleaner 의 훌륭한 대체제임에는 틀림없다.
    하지만 위에서 이렇게 설명이 나와 있은 것을 볼 수 있다.
    
    > 바로 클래스가 `AutoClosable` 인터페이스를 구현해주고,
    사용자가 코드에서 `close()` 를 명시적으로 사용해주면 된다.
    > 
    
    즉, 사용자가 `직접 close() 를 호출` 해야 한다는 것이다.
    
    사람이 일일히 수작업으로 달아야 한다는 것은
    동시에 사람이기에 실수한다면 영영 자원회수가 안된다는 것이다.
    
    이런 경우 우리는 사람의 실수를 염두에 둔
    클래스를 close, cleaner 2 가지 방식을 전부 다 구현하여
    close 도 가능하고, cleaner 도 가능한 클래스로 만드는 것이다.
    
    ### 코드를 통한 예시
    
    파일을 관리/수정/사용 하는 FileManager 클래스를 만든다고 해보자.
    
    ### FileManager 를 예시로 드는 이유
    
    파일을 열 때 운영 체제는 
    내부적으로 파일에 대한 메타데이터(이름, 생성 날짜, 파일 타입 등등…etc)를 추적하며, 
    이는 메모리를 사용하게 됩니다. 
    
    따라서 불필요하게 열려 있는 파일이 많으면 
    `시스템 리소스가 낭비`되며, 운영 체제가 `더 많은 파일을 열지 못하게 될` 수 있습니다.
    
    또한, `열려 있는 파일은 디스크 공간을 차지`합니다. 
    파일이 삭제되었지만 여전히 열려 있으면, 
    그 `파일이 차지하고 있는 디스크 공간은 해제되지 않습니다.` 
    
    이러한 파일을 `유령 파일`이라고 부르며, 
    이러한 파일이 많아지면 디스크 공간이 부족해질 수 있습니다.
    따라서 FileManager 같은 클래스는 더 이상 필요하지 않는 경우 닫아야하는 게 중요하다.
    
    1. `close 만 구현하는 경우`
        
        ```java
        import java.io.File;
        
        public class FileManagerOnlyClose implements AutoCloseable{
            private File file;
            
            public FileManagerOnlyClose(String filePath) {
                this.file = new File(filePath);
            }
        
            @Override
            public void close() throws Exception {
                if(file != null) {
                    file.delete();  // 존재
                    System.out.println("File Deleted");
                    file = null;    // 자원 비할당
                }
            }
        }
        ```
        
        이렇게 구현을 해놓은 경우, 가져다 쓸 때는 아래처럼 사용하면 된다.
        
        ```java
        public class Main {
            public static void main(String[] args) {
                FileManager fileManager = new FileManager("path_to_your_file")
                // 파일을 사용하는 코드...
        				fileManager.close(); // 자원 비할당
            }
        }
        ```
        
    2. `close, cleaner 둘 다 구현하는 경우`
        
        ```java
        import java.io.*;
        import java.lang.ref.Cleaner;
        
        public class FileManager implements AutoCloseable {
            private static final Cleaner cleaner = Cleaner.create();
        
            private static class State implements Runnable {
                private File file;
        
                State(File file) {
                    this.file = file;
                }
        
                @Override
                public void run() {
                    file.delete();
                    System.out.println("File deleted");
                    file = null;
                }
            }
        
            private final State state;
            private final Cleaner.Cleanable cleanable;
        
            public FileManager(String filePath) {
                state = new State(new File(filePath));
                cleanable = cleaner.register(this, state);
            }
        
            @Override
            public void close() {
                cleanable.clean();
            }
        }
        ```
        
        이 클래스에서는 내부 클래스인 State를 정의하고 사용하고 있습니다. 
        
        ### 왜 내부 클래스인 State 가 필요한가?
        
        ![스크린샷 2023-07-28 오후 7.37.14.png](8%20-%20finalizer%20%E1%84%8B%E1%85%AA%20cleaner%20%E1%84%89%E1%85%A1%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%8B%E1%85%B3%E1%86%AF%20%E1%84%91%E1%85%B5%E1%84%92%E1%85%A2%E1%84%85%E1%85%A1%200a1ed4e9839f42d1980f19e02fd73e22/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2023-07-28_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_7.37.14.png)
        
        Cleaner를 사용하려면 Cleaner에 등록될 때 실행할 "청소 작업"을 정의해야 합니다. 
        
        이 작업은 Runnable 인터페이스를 구현한 어떤 객체에서도 정의될 수 있습니다. 
        이 코드에서는 이 작업이 State라는 내부 클래스에서 정의되었습니다.
        
        State 클래스가 없다면, 대신 다른 Runnable 인터페이스를 구현한 클래스를 사용해야 합니다. Cleaner에 등록될 "청소 작업"을 정의하는 클래스는 꼭 State일 필요는 없습니다. 
        하지만, 이 `청소 작업`을 정의하는 클래스는 반드시 있어야 합니다.
        
        따라서, 반드시 State 클래스라는 특정 클래스가 필요한 것은 아니지만, 
        Cleaner를 사용하려면 Cleaner에 등록될 때 실행할 
        `청소 작업을 정의하는 어떤 클래스`는 반드시 필요합니다. 
        
        이 클래스는 Runnable 인터페이스를 구현해야 하며, 
        `run 메서드에서 "청소 작업"을 정의`해야 합니다.
        
        즉, State 클래스가 없다면, 해당 "청소 작업"을 수행할 다른 클래스를 정의해야 합니다. 
        이 클래스는 FileManager 클래스의 내부 클래스일 수도 있고, 외부 클래스일 수도 있습니다. 
        
        하지만 내부 클래스를 사용하면 FileManager 클래스의 멤버에 쉽게 접근할 수 있으므로 편리합니다.
        
        <aside>
        💡 정리하자면, Cleaner.register의 두 번째 파라미터에는 
        Runnable 객체를 넣도록 되어 있기에, Runnable 의 구현체가 필요하다.
        
        이를 위한 방법으로 Runnable 인터페이스를 구현한 내부 정적 클래스 State 를 선언
        
        </aside>
        
        State 클래스는 Runnable 인터페이스를 구현합니다. 
        Runnable 인터페이스는 run 메서드를 오버라이드(재정의)하여 
        실행할 코드를 포함하고 있습니다. 
        
        여기서는 파일을 삭제하는 코드가 들어 있습니다.
        
        State 클래스는 FileManager 클래스에서 `파일을 추적하고 삭제하기 위한 도구로 사용`됩니다. Cleaner.register 메서드에 전달되며, FileManager 객체가 
        GC 대상이 될 때 State의 run 메서드가 호출됩니다.
        
        ```java
        public class Main {
            public static void main(String[] args) {
                FileManager fileManager = new FileManager("path_to_your_file");
                // 파일을 사용하는 코드...
                // close 메서드를 호출하지 않았습니다.
            }
        }
        ```
        
        이 방법은 어디까지나 사용자가 
        close 를 직접 호출하지 않을 수 있기에 마련한 안정망임을 명심하자!
        
2. `네이티브 피어(native peer) 와 연결된 객체`
    
    <aside>
    💡 네이티브 피어란?
    "네이티브 피어"는 자바가 활용하는 개념이며, 
    `운영 체제나 플랫폼에 특화된 코드를 실행하는 방법`입니다. 
    
    네이티브 피어를 사용하는 것은 `Java Native Interface (JNI)`를 통해 이루어지며, 
    이는 자바가 C, C++ 등과 같은 기타 언어로 작성된 코드를 호출할 수 있게 합니다.
    
    </aside>
    
    자바의 메모리 관리는 JVM 이 GC 를 통해 한다고 알려져 있고
    일반적으로 이 경우는 맞는 말이다.
    
    하지만 JNI 를 통해 네이티브 메소드를 호출할 경우, 이는 JVM 의 메모리 밖이기 때문에
    사용자가 직접 메모리를 관리해야 한다.
    
    - JVM 의 메모리 밖?
        
        JVM은 프로세스 메모리의 일부를 자바 힙으로 사용하고, 이 공간은 자바 객체들이 할당되는 곳입니다. JVM의 가비지 컬렉터는 이 힙 영역의 메모리를 관리하고, 더 이상 사용되지 않는 객체들을 GC 를 통해회수합니다.
        
        한편, JNI를 통해 호출된 네이티브 메소드는 JVM의 힙 영역이 아닌, 
        나머지 프로세스 메모리 공간에 직접 메모리를 할당합니다. 
        
        이 메모리 영역은 `네이티브 힙`이라고도 불리며, JVM의 가비지 컬렉터의 관리 범위 밖에 있습니다. 
        그렇기 때문에 네이티브 메소드에서 할당된 메모리는 개발자가 직접 관리해야 합니다. 
        즉, `더 이상 필요하지 않은 메모리는 개발자가 직접 해제`해야 합니다.
        
    
    이러한 상황에서 Cleaner 를 사용해서 네이티브 자원을 안전하게 정리할 수 있다.
    
    ### 코드 예시
    
    ```java
    import java.lang.ref.Cleaner;
    
    public class NativeResource {
        // 네이티브 자원에 대한 참조변수
        private final long nativeHandle;
    
        // Cleanable 을 유지하기 위한 참조
        private final Cleaner.Cleanable cleanable;
    
        public NativeResource() {
            // 네이티브 자원 할당
            this.nativeHandle = allocate();
    
            // Cleaner 설정
            this.cleanable = Cleaner.create().register(this, new State(this.nativeHandle));
        }
    
        // JNI 를 통해 네이티브 코드를 호출하여 자원을 할당, 해제합니다.
        // 실제로는 allocate, free 에 해당하는 네이트브 코드를 작성하고
        // 이를 JVM 실행시 (일반적으로) 정적 초기화 블록을 통해 등록해야 합니다.
        private static native long allocate();
        private static native void free(long nativeHandle);
    
        private static class State implements Runnable {
            private final long nativeHandle;
    
            private State(long nativeHandle) {
                this.nativeHandle = nativeHandle;
            }
    
            @Override
            public void run() {
                free(nativeHandle);
            }
        }
    
        public void close() {
            cleanable.clean();
        }
    }
    ```
    
    위 코드는 네이티브 자원을 할당받은 예시 클래스이다.
    
    위 클래스를 사용하게 된다면
    네이티브 자원들은 프로그램이 종료될 때까지 자원 해제가 되지 않을 것이다.
    그렇기 때문에 사용하게 된다면, 아래와 같이 
    
    ```java
    public class Application {
        public static void main(String[] args) {
            // NativeResource 인스턴스 생성
            NativeResource resource = new NativeResource();
            
            try {
                // resource를 이용한 작업 수행...
            } finally {
                // 작업 완료 후 자원 정리
                resource.close();
            }
        }
    }
    ```
    
    finally 블록 안에서 close 를 명시적으로 호출해주거나
    
    ```java
    public class Application {
        public static void main(String[] args) {
            // NativeResource 인스턴스 생성
            NativeResource resource = new NativeResource();
    				// 네이티브 자원을 이용한 작업...
        }
    }
    ```
    
    이렇게 사용하게 되면 아까 등록한
    
    ```java
    public NativeResource() {
            // 네이티브 자원 할당
            this.nativeHandle = allocate();
    
            // Cleaner 설정
            this.cleanable = Cleaner.create().register(this, new State(this.nativeHandle));
        }
    ```
    
    Cleaner 가 종료후 자원 회수하기를 기대할 수 있다.