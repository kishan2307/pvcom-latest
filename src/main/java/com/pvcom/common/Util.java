package com.pvcom.common;

import com.pvcom.model.UserWorkflows;
import com.pvcom.model.Workflow;
import com.pvcom.projections.UserIdNames;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Util {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZab01238878787909099456789";

    public static String generateString(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return builder.toString();
    }

    public static HashMap<WorkflowConstants.FiedConstants, Object> getEntrySequence(Workflow workflow, List<UserIdNames> userIdNames) {
        HashMap<WorkflowConstants.FiedConstants, Object> objects = new HashMap<>();
        objects.put(WorkflowConstants.FiedConstants.DATE, workflow.getDate());
        objects.put(WorkflowConstants.FiedConstants.RECEIVE_DATE, workflow.getCompany_receive_date());
        objects.put(WorkflowConstants.FiedConstants.COUNTRY, workflow.getCountry());
        objects.put(WorkflowConstants.FiedConstants.SUBMISSION_COUNTY, workflow.getSubmission_country());
        objects.put(WorkflowConstants.FiedConstants.SOURCE, workflow.getSource());
        objects.put(WorkflowConstants.FiedConstants.SERIOUSNESS, workflow.getSeriousness());
        objects.put(WorkflowConstants.FiedConstants.LOCAL_REF, workflow.getLocal_uniq_id());
        objects.put(WorkflowConstants.FiedConstants.LPNAME, workflow.getLp_name());
        objects.put(WorkflowConstants.FiedConstants.VALID, workflow.getValid());
        objects.put(WorkflowConstants.FiedConstants.ENTRY_CREATED_BY, workflow.getCreatedBy());
        objects.put(WorkflowConstants.FiedConstants.EXPEDIT, workflow.getExpedite());
        objects.put(WorkflowConstants.FiedConstants.RESON, workflow.getReason());
        objects.put(WorkflowConstants.FiedConstants.TRIAGE_COMMENT, workflow.getTriageComment());
        objects.put(WorkflowConstants.FiedConstants.DRUG, workflow.getDrugs());

        objects.put(WorkflowConstants.FiedConstants.ENTRY_CREATED_BY, getUserNameById(workflow.getCreatedBy(),userIdNames));
        objects.put(WorkflowConstants.FiedConstants.ENTRY_UPDATED_BYE, getUserNameById(workflow.getUpdatedBy(),userIdNames));

        if (workflow.getDe() != null) {
            objects.put(WorkflowConstants.FiedConstants.DE_DONE_DATE, workflow.getDe().getDoneDate());
            objects.put(WorkflowConstants.FiedConstants.DE_COMMENT, workflow.getDe().getComment());
            objects.put(WorkflowConstants.FiedConstants.DE_DONE_BY,getUserNameById(workflow.getDe().getDoneBy(),userIdNames));
        }
        if (workflow.getQc() != null) {
            objects.put(WorkflowConstants.FiedConstants.QC_DONE_DATE, workflow.getQc().getDoneDate());
            objects.put(WorkflowConstants.FiedConstants.QC_COMMENT, workflow.getQc().getComment());
            objects.put(WorkflowConstants.FiedConstants.QC_DONE_BY,getUserNameById(workflow.getQc().getDoneBy(),userIdNames));
        }
        if (workflow.getMr() != null) {
            objects.put(WorkflowConstants.FiedConstants.MR_DONE_DATE, workflow.getMr().getDoneDate());
            objects.put(WorkflowConstants.FiedConstants.MR_COMMENT, workflow.getMr().getComment());
            objects.put(WorkflowConstants.FiedConstants.MR_DONE_BY,getUserNameById(workflow.getMr().getDoneBy(),userIdNames));
        }
        if (workflow.getFs() != null) {
            objects.put(WorkflowConstants.FiedConstants.FS_DONE_DATE, workflow.getFs().getDoneDate());
            objects.put(WorkflowConstants.FiedConstants.FS_COMMENT, workflow.getFs().getComment());
            objects.put(WorkflowConstants.FiedConstants.FS_SUBMISSION_COUTRY, workflow.getFs().getCountry());
            objects.put(WorkflowConstants.FiedConstants.FS_DONE_BY,getUserNameById(workflow.getFs().getDoneBy(),userIdNames));
        }
        return objects;
    }

    public static HashMap<WorkflowConstants.FiedConstants, Object> getCasesDataSequence(UserWorkflows cases, List<UserIdNames> userIdNames) {
        HashMap<WorkflowConstants.FiedConstants, Object> objects = new HashMap<>();
        if (cases != null) {
            if (cases.getWorkflow() != null) {
                objects.putAll(getEntrySequence(cases.getWorkflow(),userIdNames));
            }
            objects.put(WorkflowConstants.FiedConstants.WWUID, cases.getWorldWideUniqId());
            objects.put(WorkflowConstants.FiedConstants.DE_USER, getUserNameById(cases.getDeUserId(),userIdNames));
            objects.put(WorkflowConstants.FiedConstants.QC_USER, getUserNameById(cases.getQcUserId(),userIdNames));
            objects.put(WorkflowConstants.FiedConstants.MR_USER, getUserNameById(cases.getMrUserId(),userIdNames));
            objects.put(WorkflowConstants.FiedConstants.FS_USER, getUserNameById(cases.getFsUserId(),userIdNames));
            objects.put(WorkflowConstants.FiedConstants.USER_ID, getUserNameById(cases.getUserId(),userIdNames));

            objects.put(WorkflowConstants.FiedConstants.CASE_CREATED_BY, getUserNameById(cases.getCreatedBy(),userIdNames));
            objects.put(WorkflowConstants.FiedConstants.CASE_UPDATED_BY, getUserNameById(cases.getUpdatedBy(),userIdNames));
        }
        return objects;
    }

    public static String getUserNameById(int id, List<UserIdNames> userIdNames) {
        String name = "";
        if (userIdNames != null) {
            for (UserIdNames names : userIdNames) {
                if (id == names.getId()) {
                    name = names.getFirstName() + " " + names.getLastName();
                    break;
                }
            }
        }
        return name;
    }
}
