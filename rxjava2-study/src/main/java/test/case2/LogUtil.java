package test.case2;

import java.util.Collection;
import java.util.function.Consumer;

public class LogUtil {
    private LogUtil() {
    }

    private static void print(String prefix, Object o) {
        System.out.println(prefix + "|" + o.toString());

    }

    public static void print(Object o) {
        String methodName = getMethodName();
        print(methodName, o);
    }

    public static void printCollection(Collection collection) {
        String methodName = getMethodName();

        collection.forEach(new Consumer() {
            @Override
            public void accept(Object o) {
                print(methodName, o);
            }
        });


    }

    private static String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
