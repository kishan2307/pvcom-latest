package com.pvcom.repository;

import com.pvcom.model.UserWorkflows;
import com.pvcom.model.Workflow;
import com.pvcom.projections.SearchBean;

import java.util.List;

public interface EntityRepositoryCustom {
    List<Object> search(SearchBean searchBean,String type);
}
