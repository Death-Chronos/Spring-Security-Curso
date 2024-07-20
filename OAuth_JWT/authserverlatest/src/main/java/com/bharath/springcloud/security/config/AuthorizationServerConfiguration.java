package com.bharath.springcloud.security.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.grammars.hql.HqlParser.SecondContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import com.bharath.springcloud.security.UserDetailsServiceImpl;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.PasswordLookup;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class AuthorizationServerConfiguration {

    private static final String ROLES_CLAIM = "roles";

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${keyFile}")
    private String keyFile;

    @Value("${password}")
    private String password;

    @Value("${alias}")
    private String alias;

    @Value("${providerUrl}")
    private String providerUrl;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServersSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.userDetailsService(userDetailsService);
        http.formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource()
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        JWKSet jwkSet = builfJWKSet(); // Constroi o JWK
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private JWKSet builfJWKSet() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore keyStore = KeyStore.getInstance("pkcs12"); // Para guardar as chaves
        try (InputStream fis = this.getClass().getClassLoader().getResourceAsStream(keyFile);) { // Pega a chave
            keyStore.load(fis, alias.toCharArray()); // Poe ela lá

            return JWKSet.load(keyStore, new PasswordLookup() {

                @Override
                public char[] lookupPassword(String name) {
                    return password.toCharArray(); // passando a senha
                }

            }); // Construindo e mandando o JWKSet(basicamente um objeto com um array de chaves para o JWT)
        } 
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings (){
        return AuthorizationServerSettings.builder().issuer(providerUrl).build(); 
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository (){
        RegisteredClient registredClient = RegisteredClient.withId("couponservice")
                                                        .clientId("couponclientapp")
                                                        .clientSecret(passwordEncoder.encode("9999"))
                                                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                                                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                                                        .redirectUri("https://oidcdebugger.com/debug")
                                                        .scope("read").scope("write")
                                                        .tokenSettings(tokenSettings())
                                                        .build(); //Adicionando o cliente 
        return new InMemoryRegisteredClientRepository(registredClient);
        
    }

    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(30)).build();
 
    } 

    @Bean //Esse método é para customizar o JWT, adicionando as Roles ao JWT, pois atualmente nessa versão e neste método, ele não vem junto.
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(){
        return context ->{
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                Authentication principal = context.getPrincipal();
                Set<String> authorities = principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
                context.getClaims().claim(ROLES_CLAIM, authorities);

            }
        };
            
    };
}
