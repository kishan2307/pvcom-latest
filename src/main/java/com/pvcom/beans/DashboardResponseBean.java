package com.pvcom.beans;

import com.pvcom.model.User;
import com.pvcom.model.UserWorkflows;
import lombok.Data;

import java.util.List;

@Data
public class DashboardResponseBean {
    private int totalCount;
    private int deCount;
    private int qcCount;
    private int mrCount;
    private int pendingCount;
    private int fsCount;
    private int doneCount;
    private int entryCount;
    private int duplicatCount;
    private int lpCasesCount;
    private int invalidCount;
    private int potentialCount;
    private List<UserWorkflows> recentUpdated;
    private List<User> recentUpdatedUser;
    private List<String> localReference;
}
