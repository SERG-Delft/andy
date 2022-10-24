package delft;

import java.lang.annotation.*;

/**
 * This annotation can be used to exclude classes and methods
 * from code coverage calculation.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface GeneratedExcludeFromJacoco {
}
