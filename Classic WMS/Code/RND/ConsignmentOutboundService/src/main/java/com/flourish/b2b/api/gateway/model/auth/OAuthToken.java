package com.flourish.b2b.api.gateway.model.auth;

import lombok.Data;

@Data
public class OAuthToken {

	private String access_token;
	private String token_type;
	private String refresh_token;
	private String expires_in;
	private String scope;
	private String jti;
}
