package CoRaveler.Item34;

import java.io.ObjectInputFilter.Status;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class SeePlanetWithReflection {
    public static void main(String[] args) {
        Planet mercury = Planet.MERCURY;

        System.out.println("Fields : ");
        for (Field field : mercury.getClass().getDeclaredFields()) {
            int modifiers = field.getModifiers();
            String modifierText = Modifier.toString(modifiers);

            System.out.println(modifierText + " " + field.getName());
        }
        System.out.println();

        System.out.println("Methods : ");
        for (Method method : mercury.getClass().getDeclaredMethods()) {
            int modifiers = method.getModifiers();
            String modifierText = Modifier.toString(modifiers);

            System.out.println(modifierText + " " + method.getName());
        }
    }
}
