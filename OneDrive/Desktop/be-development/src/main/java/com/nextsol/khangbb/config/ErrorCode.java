package com.nextsol.khangbb.config;

public enum ErrorCode {

    // Loi du lieu: 1xxx
    NOT_FOUND("1000"),
    ALREADY_EXIST("1001"),
    NOT_ENOUGH("1002"),
    WRONG_STATUS("1003"),
    CAN_NOT_UPDATE("1004"),
    QUANTITY_MISMATCH("1005"),
    INCOMPLETE("1006"),
    NOT_IN_TENANT("1007"),
    DATA_USING("1008"),
    STATUS_NOT_DONE("1009"),
    DONE_WORK_FLOW("1010"),
    NOT_ENOUGH_MONEY("1011"),
    NOT_SAME_PROJECT("1012"),
    NOT_ADMIN_PROJECT("1013"),
    PROJECT_ALREADY_PAID("1014"),
    INVALID_TRANSACTION("1015"),
    PROJECT_NOT_HIRING("1016"),
    PROJECT_CANT_REVIEW("1017"),
    TRANSACTION_NOT_COMPLETE("1018"),
    DATA_UPLOADING("1019"),
    CONTRIBUTOR_LOCKED("1020"),
    WI_PICKED("1021"),
    NOT_NEW_TRANSACTION("1022"),
    NOT_DEPOSIT_OR_PAYMENT("1023"),
    EXTEND_DEADLINE("1024"),
    PAYMENT_INCOMPLETE("1025"),
    NAME_ALREADY_EXIST("1026"),

    // Loi quyen truy cap: 2xxx
    PERMISSION_DENIED("2000"),

    // Loi nguoi dung: 3xxx
    MISSING_PARAMETER("3000"),
    FILE_EXTENSION_ERR("3001"),
    FILE_MAX_SIZE_ERR("3002"),
    ERROR_PARAMETER("3003"),
    DATA_PARSE_ERROR("3004"),
    INVALID_NAME("3005"),

    // Loi he thong: 4xxx
    UNEXPECTED("4000"),
    CONNECTION("4001"),
    DATA_INTEGRITY_VIOLATION("4002"),
    WS_ERROR("4003")
    ;

    ErrorCode(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }
}
