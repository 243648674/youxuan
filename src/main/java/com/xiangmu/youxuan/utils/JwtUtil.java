package com.xiangmu.youxuan.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static String SECRET_KEY;   //密钥
    private static Key KEY;             //
    private static long EXPIRATION_TIME;//过期时间

    //从 yml 注入密钥（HS256 要求至少 32 字节）
    @Value("${jwt.secret-key}")
    public void setSecretKey(String secretKey){
        JwtUtil.SECRET_KEY = secretKey;
        JwtUtil.KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
    //从 yml 注入过期时间（毫秒）
    @Value("${jwt.expiration}")
    public void setExpirationTime(long expirationTime){
        JwtUtil.EXPIRATION_TIME=expirationTime;
    }
    //员工层：生成token，把员工id写进去 + 过期时间 + 用密钥签名
    public static String createEmployeeJWT(Long employeeId){
        long now = System.currentTimeMillis();//得到一个很大的数字
        return Jwts.builder()
                .claim("employeeId",employeeId)
                .claim("role","employee")
                .issuedAt(new Date(now))
                .expiration(new Date(now + EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }
    //用户层：生成token，把用户id写进去 + 过期时间 + 用密钥签名
    public static String createUserJWT(Long userId){
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claim("userId",userId)
                .claim("role","user")
                .issuedAt(new Date(now))
                .expiration(new Date(now + EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }
    //解析 token：验签 + 取出里面的数据（验签失败会抛异常）
    public static Claims parseEmployeeJWT(String token){
        return Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
