package com.boscarine.finddup;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class BeanPrinter {
    /**
     * Uses reflection to print contents of bean to String for debugging purposes.
     *
     * @param o instance of object to be printed.
     */
    public static String printMe(Object o) {
        return printMe(o, 0);
    }

    public static String printMe(Collection<?> c) {
        return printMe(c, 0);
    }



    private static String printMe(Object o, int tabPosition) {
        if (o == null)
            return "(null)";
        if (o instanceof String)
            return o.toString();
        String prefix = "";
        for (int i = 0; i < tabPosition; i++) {
            prefix += "\t";
        }

        final StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getSimpleName() + "{");
        for (Method m : o.getClass().getMethods()) {
            final String methodName = m.getName();
            if ((methodName.startsWith("get") || methodName.startsWith("is")) && m.getParameterTypes().length == 0
                    && !methodName.equals("getClass")) {
                try {
                    Object value = m.invoke(o, (Object[]) null);
                    if (value == null) {
                        continue;
                    }
                    final Package pkg = m.getReturnType().getPackage();
                    if (m.getReturnType() == Date.class) {
                        value = value.toString();
                    } else if (pkg != null && pkg.getName().startsWith("java.util")) {
                        if (m.getReturnType() == List.class) { // use special method for printing lists.
                            value = printMe((List<?>) value, tabPosition + 1);
                        } else { // maps
                            value = printMe(value, tabPosition + 1);
                        }
                    } else if (pkg != null && pkg.getName().startsWith("org.chip")) {
                        value = printMe(value, tabPosition + 1);
                    }
                    // if (value != null) {
                    sb.append("\n" + prefix + "\t" + methodName + " = " + value);
                    // }
                } catch (Exception e) {
                    System.out.println(m);
                    System.out.println(m.getReturnType());
                    throw new RuntimeException(e);
                }
            }
        }
        sb.append("\n" + prefix + "}");
        return sb.toString();
    }

    private static String printMe(Collection<?> c, int tabPosition) {
        if (c == null) {
            return "(null)";
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : c) {
            sb.append(printMe(o, tabPosition + 1));
        }
        return sb.toString();
	}
}