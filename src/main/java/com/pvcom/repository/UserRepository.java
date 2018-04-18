/**
 *
 */
package com.pvcom.repository;

import com.pvcom.model.User;
import com.pvcom.projections.UserIdNames;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kishan
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    List<UserIdNames> getAllByRoleNot(String role);

    List<UserIdNames> getAllByRole(String role);

    List<UserIdNames> getAllByOrderById();

    User findByEmail(String email);

    @Query("SELECT t.id FROM User t where t.email = :email")
    Integer findIdByEmail(@Param("email") String email);
}
