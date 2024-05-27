package coursemaker.coursemaker.domain.member.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class MemberContext implements UserDetails {
    private MemberDto memberDto;
    private final List<GrantedAuthority> roles;

    public MemberContext(MemberDto memberDto, List<GrantedAuthority> roles) {
        this.memberDto = memberDto;
        this.roles = roles;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }
    @Override
    public String getPassword() {
        return memberDto.getPassword();
    }
    @Override
    public String getUsername() {
        return memberDto.getUsername();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}