package com.tekclover.core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@SuppressWarnings("deprecation")
@Configuration
public class OAuthConfig extends AuthorizationServerConfigurerAdapter {

	private String clientId = "pixeltrice";
	private String clientSecret = "pixeltrice-secret-key";
	private String privateKey = "-----BEGIN RSA PRIVATE KEY-----\r\n"
			+ "MIIEowIBAAKCAQEAnRKgYtSjnHI9mpMSZS/LsyVwX2ixqxEjbSTzRkhpY3HwWBt4\r\n"
			+ "JygB2Z0W6ZsPEwPvcrH9NHTYUJ77ukzjQnPSCTcfCzvlV92uAI0+7MbAV8QyPrQX\r\n"
			+ "QpjZjPUCgjuboXks2FIO/uZH9lhHJDG7xclZ3phB8QiHAwBg5m4lnYCTNdnvZ1l4\r\n"
			+ "b1N9J2r9NX3/u1+pVqukxKCbxQ3XnfCpl6nZBZ9owcUOkuAgaiv1w1tprI6syPuJ\r\n"
			+ "6GOyO/qIoUkyETZY14jT2Wceu0G58ILoFygTZl/n8ajC2y+7CE8MaxJp+IrC5lzz\r\n"
			+ "DZnUsxcq34Pn0VyTouk9yX2IqfKdTPGD2QN69wIDAQABAoIBAGf554/kwPWULjF/\r\n"
			+ "wAKZlBdf5jZSQKJEMxZSCnRxBMqepwI8FNqewjLCqWM0kNL+nOho6HtsCbG8vFhE\r\n"
			+ "A4B+2P/erD8shBIZHdEhrxDgNH0k1DnrzyX3trN3kRSSzuAiLcx47ZJQ8Dwjjdxo\r\n"
			+ "y6sPMd2HHsem5HDZNEv1iBLuBQT02MhC4Amll2Y4w9wKeNxO4qhFD2iBaC76Uhsa\r\n"
			+ "Jb53ex+wFqTOrAPrQWIOM/DZRfjgPxwXK3nHFkTyIYhrKt8d/h3qd0vxUUeZQhdJ\r\n"
			+ "NB/9Ex5/pRPhwnYRyWALusKK5BHp6v7LyLH6uFPaK9HLoP2NPcmPpoLUSaql5Lqp\r\n"
			+ "MyMf6rkCgYEAzswlUvq/UEtD2CuH4D5QG+wuhEzk2ouopRItB6IZf8cK7Wyp3jN/\r\n"
			+ "+BZb6J+3csLJOgxzpgMnve62IreFbZgDCgr/Jde6wjCWkPfXELgFY5gC6eH4Cp/e\r\n"
			+ "6GmpSzqkFDXOkvJdaIy0p99d5A9ZygSmssj/UrlXhefLM+qxnbARfhUCgYEAwnHL\r\n"
			+ "AfhTPDigGfhMSHXAQYRuUUDVD08VilHc3zCGTT8yxzOIsYva77ZIsJ/IcG+e6ksN\r\n"
			+ "5yKnVU6Xr9e3Ldg60v9+qtXX7T6n0mtTFsId0uC6dC7Xn1k22aGJzxoam+f7WfUm\r\n"
			+ "cug2Th9jbozZl/WpWsP9PMg/MWlafOUXYOpA49sCgYBdhUkj264FW+RUsGsEaGnG\r\n"
			+ "Fut6fWPJyfr8m9mtaAtsE9HHSasQh+3JfiMWcpR928Uw8UyjuS2R4ZCQ5rn6tBI7\r\n"
			+ "ynlU3djh1SNicBLcxrv5LFe0+bI468/c/1vGX8CwOx0uq1i+3VMd0BAgASk0CSJy\r\n"
			+ "HCALgzInpono8qxcibF/eQKBgCNCVzmoOg+1hPKwn8ST9aTrysVZKEl6YcGPTqn6\r\n"
			+ "NqBTUXXl1me7oJvPRichEkuxMfPyYDcFhapmcgMk7saVazVPtzxrQph2wB/b7lk2\r\n"
			+ "IgpXTfg7dkYi+1tDe6XeaezTiDSry6kSiqZn8rPdPqfbmDYpTAZ1mV/wZsQ3FqIw\r\n"
			+ "WEv5AoGBAMpPStFGKymk6asKvD+C3xLshoBwLQW0dS1rLG7HlBZlkDqGfjlcwrJa\r\n"
			+ "vBwRe+WFr3C8BsrG9gPWS1vvkVwzuLflXTtgmW2GnQ4yQbL+b4y3E+Fc6THdLe9O\r\n"
			+ "2L/03C0OC8T0EKHhZE4LjO4wAj3rQmbRJqKTYM2/lcT+CtewV38X\r\n"
			+ "-----END RSA PRIVATE KEY-----";
	private String publicKey = "-----BEGIN PUBLIC KEY-----\r\n"
			+ "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnRKgYtSjnHI9mpMSZS/L\r\n"
			+ "syVwX2ixqxEjbSTzRkhpY3HwWBt4JygB2Z0W6ZsPEwPvcrH9NHTYUJ77ukzjQnPS\r\n"
			+ "CTcfCzvlV92uAI0+7MbAV8QyPrQXQpjZjPUCgjuboXks2FIO/uZH9lhHJDG7xclZ\r\n"
			+ "3phB8QiHAwBg5m4lnYCTNdnvZ1l4b1N9J2r9NX3/u1+pVqukxKCbxQ3XnfCpl6nZ\r\n"
			+ "BZ9owcUOkuAgaiv1w1tprI6syPuJ6GOyO/qIoUkyETZY14jT2Wceu0G58ILoFygT\r\n"
			+ "Zl/n8ajC2y+7CE8MaxJp+IrC5lzzDZnUsxcq34Pn0VyTouk9yX2IqfKdTPGD2QN6\r\n"
			+ "9wIDAQAB\r\n"
			+ "-----END PUBLIC KEY-----";
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Bean
	public JwtAccessTokenConverter tokenEnhancer () {
		JwtAccessTokenConverter jwtConverter = new JwtAccessTokenConverter();
//		jwtConverter.setSigningKey(privateKey);
//		jwtConverter.setVerifierKey(publicKey);
		return jwtConverter;
	}
	
	@Bean
	public JwtTokenStore tokenStore () {
		return new JwtTokenStore(tokenEnhancer());
	}
	
	@Override
	public void configure (AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.authenticationManager(authenticationManager)
			.tokenStore(tokenStore())
			.accessTokenConverter(tokenEnhancer());
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()");
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
			.inMemory()
			.withClient(clientId)
			.secret(passwordEncoder.encode(clientSecret))
			.scopes("read", "write")
			.authorizedGrantTypes("password", "refresh_token")
			.accessTokenValiditySeconds(20000)
			.refreshTokenValiditySeconds(20000);
	}
}
