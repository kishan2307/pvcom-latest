/**
 *
 */
package com.pvcom.repository;

import com.pvcom.model.AutoEntry;
import com.pvcom.model.AutoEntryLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kishan
 */
@Repository
public interface AutoEntryLogsRepository extends CrudRepository<AutoEntryLog, Integer> {

}
