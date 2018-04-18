package com.pvcom.services.impl;

import com.pvcom.beans.AssignBean;
import com.pvcom.beans.DashboardResponseBean;
import com.pvcom.model.User;
import com.pvcom.model.UserWorkflows;
import com.pvcom.model.Workflow;
import com.pvcom.projections.SearchBean;
import com.pvcom.repository.EntityRepositoryCustom;
import com.pvcom.repository.UserRepository;
import com.pvcom.repository.UserWorkflowRepository;
import com.pvcom.repository.WorkflowRepository;
import com.pvcom.services.EntryService;
import com.pvcom.services.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pvcom.common.WorkflowConstants.DeaultValues.*;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private UserWorkflowRepository userWorkflowRepository;

    @Autowired
    private EntityRepositoryCustom customRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntryService entryService;

    @Transactional
    public UserWorkflows add(UserWorkflows userWorkflow, String email) {
        if (userWorkflow.getWorkflow() != null) {
            User user = userRepository.findByEmail(email);
            userWorkflow.getWorkflow().setStatus(WORKFLOW_TYPE_WORKLOW_STATUS.getIntValue());
            Workflow workflow = workflowRepository.save(userWorkflow.getWorkflow());
            if (workflow != null) {
                userWorkflow.setWorkflow(workflow);
                userWorkflow.setCreatedBy(user.getId());
                if (USER_ROLE_ADMIN.getValue().equals(user.getRole())) {
                    userWorkflow.setStatus(WORKFLOW_STATUS_DE.getIntValue());
                    userWorkflow.setUserId(userWorkflow.getDeUserId());
                    return userWorkflowRepository.save(userWorkflow);
                }
            }
        }
        return null;
    }

    @Transactional
    public UserWorkflows update(UserWorkflows userWorkflow, String email) {
        if (userWorkflow.getWorkflow() != null) {
            User user = userRepository.findByEmail(email);
            userWorkflow.setUpdatedBy(user.getId());
            userWorkflow.getWorkflow().setStatus(WORKFLOW_TYPE_WORKLOW_STATUS.getIntValue());
            Workflow workflow = workflowRepository.save(userWorkflow.getWorkflow());
            if (workflow != null) {
                userWorkflow.setWorkflow(workflow);
                switch (user.getRole()) {
                    case "de":
                        if (userWorkflow.getWorkflow().getDe() != null) {
                            userWorkflow.setUserId(userWorkflow.getQcUserId());
                            userWorkflow.getWorkflow().getDe().setDoneBy(user.getId());
                            userWorkflow.setStatus(WORKFLOW_STATUS_QC.getIntValue());
                        }
                        break;
                    case "qc":
                        if (userWorkflow.getWorkflow().getQc() != null) {
                            userWorkflow.setUserId(userWorkflow.getMrUserId());
                            userWorkflow.getWorkflow().getQc().setDoneBy(user.getId());
                            userWorkflow.setStatus(WORKFLOW_STATUS_MR.getIntValue());
                        }
                        break;
                    case "mr":
                        if (userWorkflow.getWorkflow().getMr() != null) {
                            userWorkflow.setUserId(userWorkflow.getFsUserId());
                            userWorkflow.getWorkflow().getMr().setDoneBy(user.getId());
                            userWorkflow.setStatus(WORKFLOW_STATUS_FS.getIntValue());
                        }
                        break;
                    case "fs":
                        if (userWorkflow.getWorkflow().getFs() != null) {
                            userWorkflow.setUserId(userWorkflow.getCreatedBy());
                            userWorkflow.getWorkflow().getFs().setDoneBy(user.getId());
                            userWorkflow.setStatus(WORKFLOW_STATUS_CLOSE.getIntValue());
                        }
                        break;
                    case "admin":
                        int sts = userWorkflow.getStatus();
                        if (sts == WORKFLOW_STATUS_DE.getIntValue()) {
                            userWorkflow.setUserId(userWorkflow.getDeUserId());
                        } else if (sts == WORKFLOW_STATUS_QC.getIntValue()) {
                            userWorkflow.setUserId(userWorkflow.getQcUserId());
                        } else if (sts == WORKFLOW_STATUS_MR.getIntValue()) {
                            userWorkflow.setUserId(userWorkflow.getQcUserId());
                        } else if (sts == WORKFLOW_STATUS_FS.getIntValue()) {
                            userWorkflow.setUserId(userWorkflow.getFsUserId());
                        } else {
                            userWorkflow.setUserId(user.getId());
                        }
                        break;
                }
                return userWorkflowRepository.saveAndFlush(userWorkflow);
            }
        }
        return null;
    }

    public UserWorkflows get(int id) {
        return userWorkflowRepository.findOne(id);
    }

    public List<UserWorkflows> list() {
        return userWorkflowRepository.findAll();
    }

    public List<UserWorkflows> list(String listType) {
        if ("de".equalsIgnoreCase(listType)) {
            return userWorkflowRepository.findAllByStatusOrderByCreatedOnDesc(WORKFLOW_STATUS_DE.getIntValue());
        } else if ("qc".equalsIgnoreCase(listType)) {
            return userWorkflowRepository.findAllByStatusOrderByCreatedOnDesc(WORKFLOW_STATUS_QC.getIntValue());
        } else if ("mr".equalsIgnoreCase(listType)) {
            return userWorkflowRepository.findAllByStatusOrderByCreatedOnDesc(WORKFLOW_STATUS_MR.getIntValue());
        } else if ("fs".equalsIgnoreCase(listType)) {
            return userWorkflowRepository.findAllByStatusOrderByCreatedOnDesc(WORKFLOW_STATUS_FS.getIntValue());
        } else if ("done".equalsIgnoreCase(listType)) {
            return userWorkflowRepository.findAllByStatusOrderByCreatedOnDesc(WORKFLOW_STATUS_CLOSE.getIntValue());
        } else if ("lpc".equalsIgnoreCase(listType)) {
            return userWorkflowRepository.getCasesByStatusAndLpNotNull(WORKFLOW_STATUS_CLOSE.getIntValue());
        }

        return userWorkflowRepository.findAllByStatusNotOrderByCreatedOnDesc(WORKFLOW_STATUS_CLOSE.getIntValue());
    }

    @Transactional
    public UserWorkflows update(int id, UserWorkflows userWorkflows) {
        if (userWorkflowRepository.exists(id)) {
            userWorkflows.setId(id);
            return userWorkflowRepository.save(userWorkflows);
        }
        return null;
    }

    @Transactional
    public boolean delete(int id) {
        if (userWorkflowRepository.exists(id)) {
            userWorkflowRepository.delete(id);
            return true;
        }
        return false;
    }

    @Override
    public int getLpPendingCasesCount() {
        return 0;
    }

    public DashboardResponseBean getDashBoardData() {
        DashboardResponseBean responseBean = new DashboardResponseBean();
        responseBean.setDeCount(userWorkflowRepository.countAllByStatus(WORKFLOW_STATUS_DE.getIntValue()));
        responseBean.setQcCount(userWorkflowRepository.countAllByStatus(WORKFLOW_STATUS_QC.getIntValue()));
        responseBean.setMrCount(userWorkflowRepository.countAllByStatus(WORKFLOW_STATUS_MR.getIntValue()));
        responseBean.setFsCount(userWorkflowRepository.countAllByStatus(WORKFLOW_STATUS_FS.getIntValue()));
        responseBean.setEntryCount(entryService.getEntryCount());
        responseBean.setDuplicatCount(entryService.getDupEntryCount());
        responseBean.setLocalReference(entryService.getLocalRefNumberList());
        responseBean.setInvalidCount(workflowRepository.countByValidAndStatus("NO", WORKFLOW_TYPE_ENTRY_STATUS.getIntValue()));
        responseBean.setPotentialCount(workflowRepository.countByValidAndStatus("Potential", WORKFLOW_TYPE_ENTRY_STATUS.getIntValue()));
        responseBean.setLpCasesCount(userWorkflowRepository.countByStatusAndLpNotNull(WORKFLOW_STATUS_CLOSE.getIntValue()));
        responseBean.setDoneCount(userWorkflowRepository.countAllByStatus(WORKFLOW_STATUS_CLOSE.getIntValue()));
        responseBean.setRecentUpdated(userWorkflowRepository.findTop10ByOrderByUpdatedOnDesc());
        return responseBean;
    }

    public List<UserWorkflows> getUserWorkflowList(String name) {
        if (name != null) {
            Integer id = userRepository.findIdByEmail(name);
            return userWorkflowRepository.findAllByUserIdOrderByCreatedOnDesc(id);
        }
        return null;
    }

    @Override
    public Map<String, Object> getSarchResults(String search) {
        Map<String, Object> map = new HashMap<String, Object>();
        String temp = "%" + search + "%";
        map.put("cases", userWorkflowRepository.searchByWwUidAndLRef(temp, temp));
        map.put("entries", entryService.searchByLocalRef(search));
        return map;
    }

    @Override
    public Map<String, Object> search(SearchBean searchBean) {
        if (searchBean != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cases", customRepository.search(searchBean, "case"));
            map.put("entries", customRepository.search(searchBean, "entry"));
            return map;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean assignAll(AssignBean assignBean, String email) {
        boolean flag = false;
        User user = userRepository.findByEmail(email);
        if ("entry".equalsIgnoreCase(assignBean.getType())) {
            List<Workflow> workflows = entryService.getList(assignBean.getIds());
            if (workflows != null && user != null) {
                if (!workflows.isEmpty()) {
                    System.out.println(workflows);
                    for (Workflow workflow : workflows) {
                        UserWorkflows userWorkflows = new UserWorkflows();
                        workflow.setUpdatedBy(user.getId());
                        userWorkflows.setCreatedBy(user.getId());
                        userWorkflows.setStatus(WORKFLOW_STATUS_DE.getIntValue());
                        workflow.setStatus(WORKFLOW_TYPE_WORKLOW_STATUS.getIntValue());

                        userWorkflows.setUserId(assignBean.getDe());
                        userWorkflows.setDeUserId(assignBean.getDe());
                        userWorkflows.setQcUserId(assignBean.getQc());
                        userWorkflows.setMrUserId(assignBean.getMr());
                        userWorkflows.setFsUserId(assignBean.getFs());
                        userWorkflows.setWorkflow(workflow);

                        if (workflowRepository.save(userWorkflows.getWorkflow()) != null) {
                            if (userWorkflowRepository.save(userWorkflows) != null) {
                                flag = true;
                            }
                            ;
                        }
                    }
                }
            }
        } else if ("cases".equalsIgnoreCase(assignBean.getType())) {
            List<UserWorkflows> userWorkflows = userWorkflowRepository.findAll(assignBean.getIds());
            if (userWorkflows != null && !userWorkflows.isEmpty()) {
                for (UserWorkflows userWorkflow : userWorkflows) {
                    userWorkflow.setDeUserId(assignBean.getDe());
                    userWorkflow.setQcUserId(assignBean.getQc());
                    userWorkflow.setMrUserId(assignBean.getMr());
                    userWorkflow.setFsUserId(assignBean.getFs());
                    userWorkflow.setUpdatedBy(user.getId());

                    if(WORKFLOW_STATUS_DE.getIntValue()==userWorkflow.getStatus()){
                        userWorkflow.setUserId(assignBean.getDe());
                    }else if(WORKFLOW_STATUS_QC.getIntValue()==userWorkflow.getStatus()){
                        userWorkflow.setUserId(assignBean.getQc());
                    }else if(WORKFLOW_STATUS_MR.getIntValue()==userWorkflow.getStatus()){
                        userWorkflow.setUserId(assignBean.getMr());
                    }else if(WORKFLOW_STATUS_FS.getIntValue()==userWorkflow.getStatus()){
                        userWorkflow.setUserId(assignBean.getFs());
                    }
                }
                if (userWorkflowRepository.save(userWorkflows) != null) {
                    flag = true;
                }
            }
        }
        return flag;
    }
}
