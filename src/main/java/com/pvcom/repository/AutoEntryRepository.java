/**
 *
 */
package com.pvcom.repository;

import com.pvcom.model.AutoEntry;
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
public interface AutoEntryRepository extends CrudRepository<AutoEntry, Integer> {

}
