package ismaelTortosa.diceGame.model.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private final static String ACCESS_TOKEN_SECRET = "asdglasfEOdga45698%Jflelslalgigiels%lkfgAAD158Poo";
    //Token key variable.
    private static final byte[] SIGNING_KEY = ACCESS_TOKEN_SECRET.getBytes();
    //JwtParser is instantiated to parse and extract information from the token.
    private static final JwtParser PARSER = Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build();
    private final static long ACCESS_TOKEN_VALIDITY_SECONDS = 2592000L;
    //An expiration date is set for the token.
    private static final Date EXPIRATION_DATE = new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000);
    //Variable that is used in the creation of the token where the expiration and the key are.
    private static final JwtBuilder BUILDER = Jwts.builder().setSubject("").setExpiration(EXPIRATION_DATE).signWith(Keys.hmacShaKeyFor(SIGNING_KEY));

    //Token creation.
    public static String createToken(int id, String name, String password){
        Map<String, Object> extra = new HashMap<>();
        extra.put("name", name);
        extra.put("id", id);

        return BUILDER.setSubject(name)
                .addClaims(extra)
                .compact();
    }

    //Token authentication.
    public static UsernamePasswordAuthenticationToken getAuthentication(String token){
        try{
            Claims claims = PARSER
                    .parseClaimsJws(token)
                    .getBody();

            String name = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(name, null, Collections.emptyList());
        } catch (JwtException e){
            return null;
        }
    }

    //The token id is used.
    public static int getUserIdFromToken(String token){
        Claims claims = PARSER
                .parseClaimsJws(token)
                .getBody();

        Object userIdObject = claims.get("id"); //get the id of the token body.
        if(userIdObject == null){
            throw new IllegalArgumentException("ID not found in token");
        }

        int userId = (int) userIdObject;
        return userId; //Returns only the id.
    }

}
