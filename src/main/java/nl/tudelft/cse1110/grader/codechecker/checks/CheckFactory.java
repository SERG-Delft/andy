package nl.tudelft.cse1110.grader.codechecker.checks;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CheckFactory {

    public Check build(String check, List<String> params) {
        try {
            Class<?> aClass = Class.forName("nl.tudelft.cse1110.grader.codechecker.checks." + check);

            /**
             * If a list of parameters is supplied, we look for the constructor
             * that receives the list; otherwise, we go for the default constructor.
             *
             * This will throw an exception if the correct constructor is not provided, or the
             * user wrongly passes or forgets to pass parameters.
             */
            if(!params.isEmpty()) {
                Constructor<?> ctor = aClass.getConstructor(List.class);
                return (Check) ctor.newInstance(params);
            }
            else {
                Constructor<?> ctor = aClass.getConstructor();
                return (Check) ctor.newInstance();
            }
        } catch (IllegalArgumentException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format("Could not instantiate rule %s with params %s [size=%d]", check, String.join(" ", params), (params!=null?params.size():-1)), e);
        }
    }
}
