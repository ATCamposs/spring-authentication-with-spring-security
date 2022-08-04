package com.url.shortener.repositories;

import com.url.shortener.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(Role.Type name);

    @Query(value = """
            SELECT r.id, r.name FROM roles r WHERE r.id IN (SELECT ur.role_id FROM user_roles ur WHERE ur.user_id = (SELECT u.id FROM users u WHERE u.email = ?1))
            """, nativeQuery = true)
    Set<Role> findByUserEmail(String email);
}