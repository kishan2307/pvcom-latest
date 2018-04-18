package com.pvcom.common;

public interface WorkflowConstants {

    public static enum DeaultValues {
        WORKFLOW_TYPE_ENTRY_STATUS("0"),
        WORKFLOW_TYPE_WORKLOW_STATUS("1"),

        USER_ROLE_ADMIN("admin"),
        USER_ROLE_DE("de"),
        USER_ROLE_QC("qc"),
        USER_ROLE_MR("mr"),
        USER_ROLE_FS("fs"),
        USER_ROLE_GUEST("guest"),

        WORKFLOW_STATUS_DRAFT("0"),
        WORKFLOW_STATUS_DE("1"),
        WORKFLOW_STATUS_QC("2"),
        WORKFLOW_STATUS_MR("3"),
        WORKFLOW_STATUS_CLOSE("5"),
        WORKFLOW_STATUS_FS("4"),;
        String value;

        DeaultValues(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getIntValue() {
            return Integer.parseInt(value);
        }


        public static int getWorkflowStatusBy(String role) {
            if ("de".equals(role)) {
                return Integer.parseInt(WORKFLOW_STATUS_DE.getValue());
            } else if ("qc".equals(role)) {
                return Integer.parseInt(WORKFLOW_STATUS_QC.getValue());
            } else if ("mr".equals(role)) {
                return Integer.parseInt(WORKFLOW_STATUS_MR.getValue());
            } else if ("fs".equals(role)) {
                return Integer.parseInt(WORKFLOW_STATUS_MR.getValue());
            } else {
                return -1;
            }
        }

        public static String geRoleTitle(String role) {
            if ("de".equals(role)) {
                return "Data Entry";
            } else if ("qc".equals(role)) {
                return "Quality Check";
            } else if ("mr".equals(role)) {
                return "Medical Review";
            } else if ("fs".equals(role)) {
                return "Submission";
            } else if ("guest".equals(role)) {
                return "Triage/Entry";
            } else if ("admin".equals(role)) {
                return "ADMIN";
            }
            return role;
        }

    }

    public static enum ErrorCodes {
        SUCCESS(100, "success"),
        INVALID_USERNAME(101, "invalid username."),
        EMPTY_USERNAME(102, "please enter username."),
        EMAIL_SEND_FAIL(103, "Fail to send email."),
        INTERNAL_ERROR(111, "internal server error");
        int code;
        String desc;

        ErrorCodes(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    public static enum FiedConstants {
        DATE("Date (IRD)"), RECEIVE_DATE("Company received date"),
        LOCAL_REF("Local reference number"), WWUID("WW unique id"),
        LPNAME("LP name if sharing"),
        VALID("Valid"),
        RESON("Reason if non valid"), TRIAGE_COMMENT("Triage Comments"),
        COUNTRY("Country"), SUBMISSION_COUNTY("Submission countries"),
        DRUG("Suspect drug"), EXPEDIT("Expedite"),
        ENTRY_CREATED_BY("Triage done by"), ENTRY_UPDATED_BYE("Triage change by"), SERIOUSNESS("Seriousness"),
        SOURCE("Source"),

        DE_DONE_DATE("DE done date"), DE_COMMENT("DE Comment"),DE_DONE_BY("DE done by"),
        QC_DONE_DATE("QC done date"), QC_COMMENT("QC Comment"),QC_DONE_BY("QC done by"),
        MR_DONE_DATE("Mr done date"), MR_COMMENT("MR Comment"),MR_DONE_BY("MR done by"),
        FS_DONE_DATE("Submission done date"), FS_COMMENT("Submission comment"), FS_SUBMISSION_COUTRY("Final submission countries"),FS_DONE_BY("Submission done by"),

        CASE_CREATED_BY("Case created by"), CASE_UPDATED_BY,
        DE_USER("DE done by"), QC_USER("QC done by"), MR_USER("MR done by"), FS_USER("Submission done by"), USER_ID("Current User"), STATUS,;

        String name;

        FiedConstants(String desc) {
            this.name = desc;
        }

        FiedConstants() {

        }

        public String getName() {
            return name;
        }
    }

}
