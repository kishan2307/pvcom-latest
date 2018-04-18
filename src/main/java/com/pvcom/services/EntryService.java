/**
 *
 */
package com.pvcom.services;

import com.pvcom.model.Workflow;

import java.util.List;

/**
 * @author Kishan
 */

public interface EntryService {
    public Workflow add(Workflow workflow, String email);

    public Workflow add(Workflow workflow);

    public Iterable<Workflow> list(String name);

    public List<Workflow> getUniqByLocalRef();

    public List<Workflow> getDuplicateByLocalRef();

    public Workflow get(int id);

    public boolean update(int id, Workflow workflow, String email);

    public int getEntryCount();

    public int getDupEntryCount();

    public List<String> getLocalRefNumberList();

    public List<Workflow> searchByLocalRef(String search);

    public boolean delete(int id);

    public List<Workflow> getList(List<Integer> list);

    List<Workflow> getListByType(String type);
}
