package be.nicholasmeyers.backend.core.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final String roleClaimName;

    public RoleConverter(String roleClaimName) {
        this.roleClaimName = roleClaimName;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        if (jwt.hasClaim(roleClaimName)) {
            List<String> roles = jwt.getClaimAsStringList(roleClaimName);
            return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
