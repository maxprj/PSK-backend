package com.psk.backend.common;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public enum Error {

    USER_NOT_FOUND {
        @Override
        public String message() {
            return "User with id %s not found";
        }
    },

    USER_CONFIRMATION_ERROR {
        @Override
        public String message() {
            return "Error confirming user %s ";
        }
    },

    OBJECT_NOT_FOUND {
        @Override
        public String message() {
            return "Object of type %s with id %s not found";
        }
    },

    UNEXPECTED_ERROR {
        @Override
        public String message() {
            return "Unexpected error";
        }
    },

    MERGE_ERROR {
        @Override
        public String message() {
            return "The trips %s and %s cannot be merged";
        }
    };

    private static final Logger LOG = getLogger(ErrorItem.class);

    public ErrorItem entity(Object... params) {
        return new ErrorItem(this, String.format(message(), params));
    }

    public ErrorItem entity(String message) {
        return new ErrorItem(this, message);
    }

    public ErrorItem entity(Exception ex) {
        LOG.debug(ex.getMessage(), ex);
        return new ErrorItem(this, ex);
    }

    public abstract String message();
}