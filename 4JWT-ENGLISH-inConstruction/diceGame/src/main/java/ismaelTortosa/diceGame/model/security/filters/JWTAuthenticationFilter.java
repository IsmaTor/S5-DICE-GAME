package ismaelTortosa.diceGame.model.security.filters;

import ismaelTortosa.diceGame.model.exceptions.ErrorResponseLoginMessage;
import ismaelTortosa.diceGame.model.security.credentials.AuthCredentials;
import ismaelTortosa.diceGame.model.security.users.AdminDetailsImpl;
import ismaelTortosa.diceGame.model.security.users.UserDetailsImpl;
import ismaelTortosa.diceGame.model.security.utils.TokenUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javax.security.auth.login.AccountLockedException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = Logger.getLogger(JWTAuthenticationFilter.class.getName());
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_MILLISECONDS = 60000;
    private LocalDateTime timestamp;
    private ErrorResponseLoginMessage errorResponse;
    private String username;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String appJson = MediaType.APPLICATION_JSON_VALUE;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthCredentials authCredentials = new AuthCredentials();

        int failedAttempts, remainingAttempts;
        long remainingTime, blockedTime;
        HttpSession session;

        //converts a JSON object to an AuthCredentials object.
        try{
            authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);
        } catch (IOException e){
            LOGGER.warning("Could not read JSON file." + e);
        }
        //Control.
        username = authCredentials.getName();
        LOGGER.info("Username: " + username);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                authCredentials.getName(),
                authCredentials.getPassword(),
                Collections.emptyList());

        session = request.getSession(); //get session.
        if (session.getAttribute("failedAttempts") != null) {
            failedAttempts = (Integer) session.getAttribute("failedAttempts"); //failedAttempts value.
        } else {
            failedAttempts = 0;
        }

        if (session.getAttribute("blockedTime") != null) {
            blockedTime = (Long) session.getAttribute("blockedTime"); //Recover blocking time.
            remainingTime = blockedTime - System.currentTimeMillis(); //Calculate the remaining time.
            if (remainingTime > 0) {
                try {
                    throw new AccountLockedException("Your account has been locked due to too many failed attempts. Wait " + remainingTime / 1000 + " seconds before trying again.");
                } catch (AccountLockedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                session.removeAttribute("blockedTime"); //Remove session lock time.
                failedAttempts = 0; //Reset the counter of failed attempts in case the blocking time has passed.
                session.setAttribute("failedAttempts", failedAttempts); //Update the value of failedAttempts in the session.
            }
        }
        Authentication authentication;
        try {
            authentication = getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
            failedAttempts = 0; //Reset the counter of failed attempts on success.
            session.setAttribute("failedAttempts", failedAttempts); //Update the value of failedAttempts in the session.
        } catch (BadCredentialsException e) {
            LOGGER.info("Username: " + authCredentials.getName() + " has entered the password incorrectly.");
            failedAttempts++;
            remainingAttempts = MAX_FAILED_ATTEMPTS - failedAttempts;
            LOGGER.info("Remain " + remainingAttempts + " attempts before account is locked.");
            session.setAttribute("failedAttempts", failedAttempts); //Update the value of failedAttempts in the session.

            if (remainingAttempts <= 0) {
                blockedTime = System.currentTimeMillis() + LOCK_TIME_MILLISECONDS; //Calculate blocking time.
                session.setAttribute("blockedTime", blockedTime); //Store lock time in session.
                remainingTime = LOCK_TIME_MILLISECONDS;
                try {
                    throw new AccountLockedException("Your account has been locked due to too many failed attempts. Wait " + remainingTime / 1000 + " seconds before trying again.");
                } catch (AccountLockedException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (remainingAttempts == 1) {
                throw new BadCredentialsException("You have entered an incorrect password. Only 1 attempt left before account is locked.");
            } else {
                throw new BadCredentialsException("Your account has been locked due to too many failed attempts. Wait " + remainingAttempts + " attempts before account is locked.");
            }
        }
        LOGGER.info("Username: " + authCredentials.getName() + " is in the game.");
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException{
        String token = "";

        if (authResult.getPrincipal() instanceof AdminDetailsImpl) { //Token for administrator.
            AdminDetailsImpl adminDetails = (AdminDetailsImpl) authResult.getPrincipal();
            token = TokenUtils.createTokenAdmin(adminDetails.getIdAdmin(), adminDetails.getUsername(), adminDetails.getPassword(), adminDetails.getRol());
        } else if (authResult.getPrincipal() instanceof UserDetailsImpl) { //Token for user.
            UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
            token = TokenUtils.createToken(userDetails.getIdUser(), userDetails.getUsername(), userDetails.getPassword());
        }

        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().flush();

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(appJson);
        response.getWriter().write("User " + username + " authentication successful.");
        response.getWriter().close();

        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        String errorResponseJson;

        HttpSession session = request.getSession(false); //False so that the web container does not create a new session on each call, which can cause excessive memory usage.
        if (session != null) {
            Integer failedAttempts = (Integer) session.getAttribute("failedAttempts");
            if (failedAttempts != null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                errorResponse = new ErrorResponseLoginMessage(HttpStatus.UNAUTHORIZED.value(), username, "Password incorrect.", "Remain " + (MAX_FAILED_ATTEMPTS - failedAttempts) + " attempts before account is locked.", timestamp.now());
                objectMapper.registerModule(new JavaTimeModule());

                errorResponseJson = objectMapper.writeValueAsString(errorResponse);
                response.setContentType(appJson); //Indicates that the response is a JSON object.
                response.getWriter().write(errorResponseJson);
            } else {
                //If the number of failed attempts has not been recorded, it displays a generic error message.
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                errorResponse = new ErrorResponseLoginMessage(HttpStatus.UNAUTHORIZED.value(), username, "Authentication failed.", "It has not been possible to carry out the authentication, a possible failure could be because the login attempts could not be counted.", timestamp.now());
                objectMapper.registerModule(new JavaTimeModule());

                errorResponseJson = objectMapper.writeValueAsString(errorResponse);
                response.setContentType(appJson);
                response.getWriter().write(errorResponseJson);
            }
        }
        response.getWriter().close();
    }

}
