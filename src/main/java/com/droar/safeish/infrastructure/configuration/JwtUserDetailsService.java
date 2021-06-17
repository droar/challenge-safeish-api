package com.droar.safeish.infrastructure.configuration;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.droar.safeish.application.port.out.CreateSafeBoxPort;
import com.droar.safeish.domain.SafeBox;

@Service
public class JwtUserDetailsService implements UserDetailsService {
  
  /** The create safe box port. */
  @Autowired
  private CreateSafeBoxPort createSafeBoxPort;

  @Override
  public UserDetails loadUserByUsername(String safeBoxUuid) throws UsernameNotFoundException {
    SafeBox safeBoxObtained = this.createSafeBoxPort.obtainSafeBoxByUuid(safeBoxUuid);
    
    if (safeBoxObtained != null) {
      return new User(safeBoxObtained.getUuid(), safeBoxObtained.getPassword(), new ArrayList<>());
    } else {
      throw new UsernameNotFoundException("User not found with username: " + safeBoxUuid);
    }
  }
}
