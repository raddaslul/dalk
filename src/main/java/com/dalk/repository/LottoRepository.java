package com.dalk.repository;

import com.dalk.domain.Lotto;
import com.dalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LottoRepository extends JpaRepository<Lotto, Long> {
    Lotto findByUser(User user);
}
