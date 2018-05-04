package com.todo.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = JwtException.class)
	public void handleJsonExceptions(HttpServletResponse res) throws IOException {
		res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
