package com.xiangmu.youxuan.context;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> roleThreadLocal = new ThreadLocal<>();
    //存
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static void setCurrentRole(String role){
        roleThreadLocal.set(role);
    }
    //取
    public static Long getCurrentId(){
        return threadLocal.get();
    }
    public static String getCurrentRole() {
        return roleThreadLocal.get();
    }
    //删
    public static void remove(){
        threadLocal.remove();
        roleThreadLocal.remove();
    }
}
