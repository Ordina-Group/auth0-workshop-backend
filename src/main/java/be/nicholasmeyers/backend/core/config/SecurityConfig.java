package be.nicholasmeyers.backend.core.config;

import be.nicholasmeyers.backend.core.converter.RoleConverter;
import be.nicholasmeyers.backend.core.handler.AuthenticationErrorHandler;
import be.nicholasmeyers.backend.core.validator.AudienceValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${role.claim.name}")
    private String roleClaimName;

    private final AuthenticationErrorHandler authenticationErrorHandler;

    public SecurityConfig(AuthenticationErrorHandler authenticationErrorHandler) {
        this.authenticationErrorHandler = authenticationErrorHandler;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/movie/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .cors()
                .and()
                .oauth2ResourceServer()
                .authenticationEntryPoint(authenticationErrorHandler)
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .jwt()
                .decoder(jwtDecoder());
    }

    protected JwtDecoder jwtDecoder() {
        final NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuer);
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        decoder.setJwtValidator(audienceValidator);
        return decoder;
    }

    protected Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new RoleConverter(roleClaimName));
        return jwtConverter;
    }
}
