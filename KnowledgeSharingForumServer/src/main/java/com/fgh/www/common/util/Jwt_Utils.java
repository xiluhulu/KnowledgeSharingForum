
package com.fgh.www.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

/**
 * @author fgh
 */
public class Jwt_Utils {
    // 有效期为 60 * 1000 一分钟（10小时）
    public static final Long TIME = 10 * 60 * 60*1000L;
    // 有效期为 60 * 1000 一分钟（1分钟）
//    public static final Long TIME =  60*1000L;
    // 有效期为 60 * 1000 一分钟（20秒）
//    public static final Long TIME =  20*1000L;
    // 加密算法
    public static final SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS256;
    //私钥
    public static final String SECRET = "xiluhuluxiluhuluxiluhuluxiluhulu";
    //密钥实例
    public static final SecretKey KEY =  Keys.hmacShaKeyFor( SECRET.getBytes());
    // 创建 token
    public static String createToken(Object id, String username, String role) {
        JwtBuilder builder = Jwts.builder();
        return builder
                // header: 头部
                .header().add("head", "jwt").and()
                // 载体
                .claim("id", id)
                .claim("username",username)
                .claim("role",role)
                // 主题
                .subject("xxx")
                // 过期时间
                .expiration(new Date(System.currentTimeMillis() + TIME))
                // jwt 的 id（属于载体中的）
                .id(UUID.randomUUID().toString())
                // 使用新的密钥进行签名
                .signWith(KEY, ALGORITHM)
                .compact();
    }
    // 解析 token
    public static Claims parseToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
             IllegalArgumentException {
        return Jwts.parser().
                verifyWith(KEY)
                .build().parseSignedClaims(token).getPayload();
    }
    //获取id
    public static Integer getId(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
             IllegalArgumentException {
        return Integer.parseInt(parseToken(token).get("id").toString());
    }
    //获取用户名
    public static String getUserName(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
            IllegalArgumentException {
        return parseToken(token).get("username").toString();
    }
    //获取角色
    public static String getRole(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
            IllegalArgumentException {
        return parseToken(token).get("role").toString();
    }
}

