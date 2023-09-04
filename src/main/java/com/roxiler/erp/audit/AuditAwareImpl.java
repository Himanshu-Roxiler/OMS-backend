package com.roxiler.erp.audit;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal().getClass() == String.class) {
            return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        UserDto userDto = (UserDto) auth.getPrincipal();
        Optional<Users> user = usersRepository.findById(userDto.getId());
        return user.map(users -> users.getFirstName() + " " + users.getLastName());

    }
}
