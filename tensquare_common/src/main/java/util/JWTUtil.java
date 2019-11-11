package util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@ConfigurationProperties("jwt.config")
public class JWTUtil {

    //密钥
    private String key;
    //过期时间
    private long ttl;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * 创建token令牌
     * @param id 用户ID
     * @param username 用户名
     * @param roles 用户身份
     * @return token令牌
     */
    public String createJwt(String id, String username, String roles){
        long nowMillis = System.currentTimeMillis();
        JwtBuilder builder =Jwts.builder().setId(id)
                                .setSubject(username)
                                .setIssuedAt(new Date())
                                .claim("roles",roles)
                                .signWith(SignatureAlgorithm.HS256,key);
        if (ttl > 0){
            builder.setExpiration(new Date(nowMillis+ttl));
        }
        return builder.compact();
    }

    /**
     * 解析令牌信息
     * @param jwtSt 令牌
     * @return 解析结果
     */
    public Claims parseJwt(String jwtSt){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwtSt)
                .getBody();
    }
}
