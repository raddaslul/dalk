package com.dalk.repository.wl;

import com.dalk.domain.wl.WarnUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarnUserRepository extends JpaRepository<WarnUser,Long> {

    Optional<WarnUser> findByUserIdAndWarnUserName(Long user_id, String warnUserName);
}
