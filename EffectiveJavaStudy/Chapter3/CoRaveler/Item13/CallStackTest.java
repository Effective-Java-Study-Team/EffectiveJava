package CoRaveler.Item13;

import java.util.Arrays;

public class CallStackTest {
    public static void main(String[] args) {
        StackTest st = new StackTest();
        st.push(1);
        st.push(1);
        st.push(1);
        st.push(1);

        StackTest st2 = st.clone();

    }
}
