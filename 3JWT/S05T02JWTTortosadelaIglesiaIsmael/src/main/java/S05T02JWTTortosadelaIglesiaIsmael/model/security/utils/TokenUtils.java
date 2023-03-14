package S05T02JWTTortosadelaIglesiaIsmael.model.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private final static String ACCESS_TOKEN_SECRET = "asdglasfEOdga45698%Jflelslalgigiels%lkfgAAD158Poo";

    //variable de la clave del token
    private static final byte[] SIGNING_KEY = ACCESS_TOKEN_SECRET.getBytes();

    //instanciamos de JwtParser para parsear y extraer información del token
    private static final JwtParser PARSER = Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build();
    private final static long ACCES_TOKEN_VALIDITY_SECONDS = 2592000L;
    //establecemos una fecha de expiración para el token
    private static final Date EXPIRATION_DATE = new Date(System.currentTimeMillis() + ACCES_TOKEN_VALIDITY_SECONDS * 1000);

    //variable que se usará en la creación del token donde irá la expiración y la clave
    private static final JwtBuilder BUILDER = Jwts.builder().setSubject("").setExpiration(EXPIRATION_DATE).signWith(Keys.hmacShaKeyFor(SIGNING_KEY));

    //método crear Token
    public static String createToken(int id, String nombre, String password){

        Map<String, Object> extra = new HashMap<>();
        extra.put("nombre", nombre);
        extra.put("id", id);

        return BUILDER.setSubject(nombre)
                .addClaims(extra)
                .compact();
    }

    //método para autenticar el token
    public static UsernamePasswordAuthenticationToken getAuthentication(String token) {

        try {
            Claims claims = PARSER
                    .parseClaimsJws(token)
                    .getBody();

            String nombre = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(nombre, null, Collections.emptyList());
        } catch (JwtException e) {
            System.out.println("JwtException: " + e);
            return null;
        }
    }

    //extraer el id del usuario del token
    public static int getUserIdFromToken(String token) {
        Claims claims = PARSER
                .parseClaimsJws(token)
                .getBody();

        //System.out.println("Valor de claims: " + claims.toString());

        Object userIdObject = claims.get("id"); //coge el ID del cuerpo del token
        if (userIdObject == null) {
            throw new IllegalArgumentException("El 'id' no se encuentra en el token");
        }
        int userId = (int) userIdObject;
        return userId; //retorna solo el ID
    }

}
