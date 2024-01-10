package com.books.bkb.utils;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private static final String jwtCookie = "JWT_AUTH";

    private static final long time = 3600000;

    //private static final String jwtEncode = "================WC====spring=weeb=security====NM================";

    private static final String privateKeyPath = "/usr/local/pem/private.pem";

    private static final String publicKeyPath = "/usr/local/pem/public.pem";

    private static RSAPrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        //byte[] key = Files.readAllBytes(Paths.get(privateKeyPath));
        String oriKey = Files.readString(Paths.get(privateKeyPath), Charset.defaultCharset());
        byte[] encoded = Base64.decodeBase64(oriKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    private static RSAPublicKey loadPublicKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        //byte[] keyBytes = Files.readAllBytes(Paths.get(publicKeyPath));
        String oriKey = Files.readString(Paths.get(publicKeyPath), Charset.defaultCharset());
        byte[] encoded = Base64.decodeBase64((oriKey));
        // 创建 X509EncodedKeySpec 对象
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);
        // 使用 RSA 算法构建密钥工厂
        KeyFactory kf = KeyFactory.getInstance("RSA");
        // 生成 RSA 公钥对象
        return (RSAPublicKey) kf.generatePublic(spec);
    }

    public static String getJwtFromCookies(HttpServletRequest httpServletRequest)
    {
        Cookie cookie = WebUtils.getCookie(httpServletRequest, jwtCookie);
        return cookie != null ? cookie.getValue() : null;
    }

    public static ResponseCookie generateJwtCookie(String Username, String id, String role) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Claims claims = Jwts.claims();
        claims.put("username", Username);
        claims.put("id", id);
        claims.put("role", role);
        String jwt = Jwts.builder().setClaims(claims).setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + time))
                .signWith(loadPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
        return ResponseCookie.from(jwtCookie, jwt).path("*").build();
    }

    public static Claims getUsernameFromJwt(String jwt) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        return Jwts.parserBuilder().setSigningKey(loadPublicKey()).build()
                .parseClaimsJws(jwt).getBody();
    }

    public static boolean validateJwt(String jwt)
    {
        try {
            Jwts.parserBuilder().setSigningKey(loadPublicKey()).build().parse(jwt);
            return true;
        } catch (MalformedJwtException e)
        {
            logger.error("Invalid token: {}", e.getMessage());
        } catch (ExpiredJwtException e)
        {
            logger.error("Expired token: {}", e.getMessage());
        } catch (UnsupportedJwtException e)
        {
            logger.error("Unsupported token: {}", e.getMessage());
        } catch (IllegalArgumentException e)
        {
            logger.error("Illegal token: {}", e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
