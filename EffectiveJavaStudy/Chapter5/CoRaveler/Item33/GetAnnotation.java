package CoRaveler.Item33;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class GetAnnotation {
    static Annotation getAnnotation(AnnotatedElement element,
                                    String annotationTypeName) {
        Class<?> annotationType = null; // 비한정적 타입 토큰
        try {
            annotationType = Class.forName(annotationTypeName);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        return element.getAnnotation(annotationType.asSubclass(Annotation.class));
    }

}
