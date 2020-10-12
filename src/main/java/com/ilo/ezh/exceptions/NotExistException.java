package com.ilo.ezh.exceptions;

public class NotExistException extends MyException {
    public NotExistException(String login) {
        super("User " + login + " not exist");
    }
}
