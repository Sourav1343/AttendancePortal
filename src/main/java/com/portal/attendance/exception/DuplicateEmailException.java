package com.portal.attendance.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
               super(message);
           }
}
