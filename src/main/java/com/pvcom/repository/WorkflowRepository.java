/**
 *
 */
package com.pvcom.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pvcom.model.Workflow;

/**
 * @author Kishan
 */
@Repository
@Transactional
public interface WorkflowRepository extends CrudRepository<Workflow, Integer> {

    List<Workflow> findByStatusAndCreatedBy(int status, int createdBy);

    Workflow findByIdAndStatus(int id, int status);

    List<Workflow> findByStatus(int status);

    List<Workflow> findByIdIn(List<Integer> ids);

    List<Workflow> findByValidAndStatus(String valid,int status);

    int countByValidAndStatus(String valid,int status);

    @Query("SELECT t FROM Workflow t WHERE t.id NOT IN (SELECT MIN(v.id) FROM Workflow v GROUP BY v.local_uniq_id)")
    List<Workflow> getDuplicateByLocalReferenceNum();

    @Query("SELECT t FROM Workflow t where t.status = :sts GROUP BY t.local_uniq_id")
    List<Workflow> getUniqByLocalReferenceNum(@Param("sts") int sts);

    @Query("SELECT COUNT(DISTINCT t.local_uniq_id) FROM Workflow t where t.status = :sts")
    int countByStatusa(@Param("sts") int sts);

    @Query("SELECT count(t.id) FROM Workflow t WHERE t.id NOT IN (SELECT MIN(v.id) FROM Workflow v GROUP BY v.local_uniq_id)")
    int countDuplicateEntry();

    @Query("SELECT t.local_uniq_id FROM Workflow t")
    List<String> getLocalRefList();
    
    @Query("SELECT t FROM Workflow t where t.local_uniq_id Like :search and  t.status = :sts")
    List<Workflow> findBylocal_uniq_idIgnoreCaseContaining(@Param("search")String searchString,@Param("sts") int status);
}
