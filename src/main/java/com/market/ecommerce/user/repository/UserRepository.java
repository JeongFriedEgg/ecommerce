package com.market.ecommerce.user.repository;

import com.market.ecommerce.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u.userId, u.email, u.phoneNumber FROM User u WHERE u.userId = :userId OR u.email = :email OR u.phoneNumber = :phoneNumber")
    List<Object[]> findConflictingUserInfo(@Param("userId") String userId,
                                           @Param("email") String email,
                                           @Param("phoneNumber") String phoneNumber);
}
