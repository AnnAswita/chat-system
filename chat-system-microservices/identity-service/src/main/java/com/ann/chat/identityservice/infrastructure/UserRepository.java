package com.ann.chat.identityservice.infrastructure;

import com.ann.chat.identityservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> { }
