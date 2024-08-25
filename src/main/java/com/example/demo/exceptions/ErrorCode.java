package com.example.demo.exceptions;

public enum ErrorCode {

    EMPTY_FILE_EXCEPTION("S3-001", "The uploaded file is empty or filename is null."),
    NO_FILE_EXTENTION("S3-002", "The uploaded file does not have an extension."),
    INVALID_FILE_EXTENTION("S3-003", "The file extension is not allowed."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD("S3-004", "An I/O error occurred while uploading the image."),
    PUT_OBJECT_EXCEPTION("S3-005", "An error occurred while putting the object to S3."),
    IO_EXCEPTION_ON_IMAGE_DELETE("S3-006", "An I/O error occurred while deleting the image from S3.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}