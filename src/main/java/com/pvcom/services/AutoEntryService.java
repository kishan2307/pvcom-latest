/**
 *
 */
package com.pvcom.services;

import com.pvcom.model.AutoEntry;

/**
 * @author Kishan
 */

public interface AutoEntryService {
    public boolean add(AutoEntry entry);

    public boolean update(AutoEntry entry);

    public boolean delete(int id);

    public AutoEntry get(int id);

    public Iterable<AutoEntry> get();

    public void executeAutoSchedule();
}
