package org.robolectric.bytecode;

public interface ClassHandler {
    void classInitializing(Class clazz);

    Object initializing(Object instance);

    Object methodInvoked(Class clazz, String methodName, Object instance, String[] paramTypes, Object[] params) throws Throwable;

    Plan methodInvoked(String signature, boolean isStatic, Class<?> theClass);

    Object intercept(String signature, Object instance, Object[] paramTypes, Class theClass) throws Throwable;

    // todo: definitely shouldn't live here
    void setStrictI18n(boolean strictI18n);

    public interface Plan {
        Object run(Object instance, Object[] params) throws Throwable;
    }
}
