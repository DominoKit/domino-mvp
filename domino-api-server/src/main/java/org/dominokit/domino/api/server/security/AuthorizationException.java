package org.dominokit.domino.api.server.security;

@SuppressWarnings("serial")
public class AuthorizationException extends RuntimeException {

	public AuthorizationException(String message) {
		super(message);
	}

}
