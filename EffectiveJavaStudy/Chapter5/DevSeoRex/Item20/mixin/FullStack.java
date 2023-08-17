package mixin;

public abstract class FullStack {

    /*
    *   모든 조합에 대해 메서드를 가지고 있어야 한다.
    *   조합 폭발(combinatorial explosion)이 일어난다.
    * */
    abstract void makeServerApi();
    abstract void makeView();
    abstract void makeWebService();
}
