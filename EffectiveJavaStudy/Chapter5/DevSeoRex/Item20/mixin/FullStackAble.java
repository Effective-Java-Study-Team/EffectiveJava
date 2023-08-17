package mixin;

public interface FullStackAble extends FrontendAble, BackendAble {
    /*
    *   백엔드와 프론트엔드 개발이 전부 가능한 풀스택 개발자도 있을 수 있다.
    *   백엔드와 프론트를 한번에 개발해 웹 서비스를 만드는 메서드를 추가 정의했다.
    * */
    void makeWebService();
}
