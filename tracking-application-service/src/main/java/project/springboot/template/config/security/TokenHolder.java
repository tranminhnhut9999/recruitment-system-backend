package project.springboot.template.config.security;

public class TokenHolder {
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public static void setToken(String token) {
        tokenHolder.set(token);
    }

    public static String getToken() {
        return tokenHolder.get();
    }

    public static void clear() {
        tokenHolder.remove();
    }
}
