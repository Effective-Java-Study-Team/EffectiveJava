package ch2.item7;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;

public class Callee {
    private String msg;
    private WeakReference<Consumer<Callee>> callback;

    public Callee(){
        this.msg = "";
        this.callback = null;
    }

    public String getMsg(){
        return msg;
    }

    public void setCallback(Consumer<Callee> callback) {
        this.callback = new WeakReference<>(callback);
    }

    public void onInputMessage(){
        Scanner scanner = new Scanner(System.in);
        this.msg = "";
        System.out.print("메세지를 입력하세요 : ");
        this.msg = scanner.nextLine();

        Objects.requireNonNull(this.callback.get()).accept(Callee.this);

    }
}
