/**
 *
 */
package com.pvcom.services;

import com.pvcom.beans.AssignBean;
import com.pvcom.beans.DashboardResponseBean;
import com.pvcom.model.UserWorkflows;
import com.pvcom.projections.SearchBean;

import java.util.List;
import java.util.Map;

/**
 * @author Kishan
 */

public interface WorkflowService {

    public UserWorkflows add(UserWorkflows userWorkflow, String email);

    public UserWorkflows update(UserWorkflows userWorkflow, String email);

    public UserWorkflows get(int id);

    public List<UserWorkflows> list();

    public List<UserWorkflows> list(String listType);

    public UserWorkflows update(int id, UserWorkflows userWorkflows);

    public boolean delete(int id);

    public int getLpPendingCasesCount();

    public DashboardResponseBean getDashBoardData();

    public List<UserWorkflows> getUserWorkflowList(String name);

    public Map<String, Object> getSarchResults(String search);

    public Map<String, Object> search(SearchBean searchBean);

    public boolean assignAll(AssignBean assignBean,String email);
}
