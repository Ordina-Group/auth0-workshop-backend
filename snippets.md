## Add dependencies

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-oauth2-core</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-oauth2-jose</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-oauth2-resource-server</artifactId>
        </dependency>

## Add validator
    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if (token.getAudience().contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "invalid audience", null));
    }

## Add jwt decoder
    protected JwtDecoder jwtDecoder() {
        final NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuer);
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        decoder.setJwtValidator(audienceValidator);
        return decoder;
    }

## Add role converter

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        if (jwt.hasClaim(roleClaimName)) {
            List<String> roles = jwt.getClaimAsStringList(roleClaimName);
            return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    protected Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new RoleConverter(roleClaimName));
        return jwtConverter;
    }

## Add roles to access token
    if (event.authorization) {
        api.accessToken.setCustomClaim("http://localhost:8080/roles", event.authorization.roles);
    }