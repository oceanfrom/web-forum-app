package org.example.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdParserUtils {
    private IdParserUtils() {}

    public static Long parseIdWithSubstring(String path) throws InvalidIdException {
        if (path == null || path.isEmpty()) {
            log.warn("Invalid id, will throw exception.");
            throw new InvalidIdException("Invalid ID.");
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        Long topicId = null;
        try {
            topicId = Long.parseLong(path);
        } catch (NumberFormatException e) {
            log.error("Invalid id in path: {}", path, e);
            throw new InvalidIdException("Invalid ID format.");
        }

        return topicId;
    }

    public static Long parseId(String path) throws InvalidIdException {
        if (path == null || path.isEmpty()) {
            log.info("Invalid ID, will throw exception.");
            throw new InvalidIdException("Invalid ID.");
        }
        Long id = null;
        try {
            id = Long.parseLong(path);
        } catch (NumberFormatException e) {
            log.error("Invalid ID format in path: {}", path, e);
            throw new InvalidIdException("Invalid ID format.");
        }
        return id;
    }



    public static class InvalidIdException extends Exception {
        public InvalidIdException(String message) {
            super(message);
        }
    }
}
