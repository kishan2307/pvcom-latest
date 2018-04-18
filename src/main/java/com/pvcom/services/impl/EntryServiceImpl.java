package com.pvcom.services.impl;

import com.pvcom.model.User;
import com.pvcom.model.Workflow;
import com.pvcom.repository.WorkflowRepository;
import com.pvcom.services.EntryService;
import com.pvcom.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pvcom.common.WorkflowConstants.DeaultValues.WORKFLOW_TYPE_ENTRY_STATUS;

@Service
public class EntryServiceImpl implements EntryService {

    @Autowired
    private WorkflowRepository repository;

    @Autowired
    private EmailServiceHandler emailService;

    @Autowired
    private UserServices userServices;

    public Workflow add(Workflow workflow, String email) {
        int id = userServices.getIdByEmail(email);
        workflow.setCreatedBy(id);
        workflow.setStatus(WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
        return repository.save(workflow);
    }

    public Workflow add(Workflow workflow) {
        workflow.setCreatedBy(0);
        workflow.setStatus(WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
        return repository.save(workflow);
    }

    public Iterable<Workflow> list(String name) {
        User user = userServices.get(name);
        if (user != null) {
            if (user.getRole().equals("admin")) {
                return repository.findByStatus(WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
            } else {
                return repository.findByStatusAndCreatedBy(WORKFLOW_TYPE_ENTRY_STATUS.getIntValue(), user.getId());
            }
        }
        return null;
    }

    @Override
    public List<Workflow> getUniqByLocalRef() {
        return repository.getUniqByLocalReferenceNum(WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
    }

    @Override
    public List<Workflow> getDuplicateByLocalRef() {
        return repository.getDuplicateByLocalReferenceNum();
    }

    public Workflow get(int id) {
        return repository.findByIdAndStatus(id, WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
    }

    public boolean delete(int id) {
        if (repository.exists(id)) {
            repository.delete(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Workflow> getList(List<Integer> list) {
        return repository.findByIdIn(list);
    }

    @Override
    public List<Workflow> getListByType(String type) {
        if("invalid".equalsIgnoreCase(type)){
           return repository.findByValidAndStatus("NO",WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
        }else if ("potential".equalsIgnoreCase(type)){
            return repository.findByValidAndStatus("Potential",WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
        }else if("unique".equalsIgnoreCase(type)){
            return repository.getUniqByLocalReferenceNum(WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
        }else if("duplicate".equalsIgnoreCase(type)){
            return repository.getDuplicateByLocalReferenceNum();
        }
        return null;
    }

    public boolean update(int id, Workflow workflow, String email) {
        if (repository.exists(id)) {
            int userid = userServices.getIdByEmail(email);
            workflow.setUpdatedBy(userid);
            workflow.setId(id);
            workflow.setStatus(WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
            repository.save(workflow);
            return true;
        }
        return false;
    }

    public int getEntryCount() {
        return repository.countByStatusa(WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
    }

    public int getDupEntryCount() {
        return repository.countDuplicateEntry();
    }

    public List<String> getLocalRefNumberList() {
        return repository.getLocalRefList();
    }

    @Override
    public List<Workflow> searchByLocalRef(String searchString) {
        searchString = "%" + searchString + "%";
        return repository.findBylocal_uniq_idIgnoreCaseContaining(searchString, WORKFLOW_TYPE_ENTRY_STATUS.getIntValue());
    }
}
