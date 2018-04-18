/**
 *
 */
package com.pvcom.repository;

import com.pvcom.model.UserWorkflows;
import com.pvcom.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kishan
 */
@Repository
public interface UserWorkflowRepository extends JpaRepository<UserWorkflows, Integer> {

    int countAllByStatus(int Status);

    List<UserWorkflows> findTop10ByOrderByUpdatedOnDesc();

    List<UserWorkflows> findAllByStatusOrderByCreatedOnDesc(int status);
    List<UserWorkflows> findAllByStatusNotOrderByCreatedOnDesc(int status);

    List<UserWorkflows> findAllByUserIdOrderByCreatedOnDesc(int userid);
    
    List<UserWorkflows> findByworldWideUniqIdIgnoreCaseContaining(String searchString);

    @Query("SELECT t.id FROM User t where t.email = :email")
    Integer findIdByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserWorkflows u join u.workflow w where u.worldWideUniqId LIKE :wwuid OR w.local_uniq_id LIKE :luid")
    List<UserWorkflows> searchByWwUidAndLRef(@Param("wwuid") String wwuid,@Param("luid") String luid);

    @Query("SELECT count(u) FROM UserWorkflows u join u.workflow w where w.lp_name is not null and u.status<>:status")
    int countByStatusAndLpNotNull(@Param("status") int status);

    @Query("SELECT u FROM UserWorkflows u join u.workflow w where w.lp_name is not null and u.status<>:status")
    List<UserWorkflows> getCasesByStatusAndLpNotNull(@Param("status") int status);

    List<UserWorkflows> findByIdIn(List<Integer> cases);
}
