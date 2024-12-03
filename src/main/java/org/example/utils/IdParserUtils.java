package org.example.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdParserUtils {

    public static Long parseId(String path) throws InvalidIdException {
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

    public static Long parseCommentId(String path) throws InvalidIdException {
        if (path == null || path.isEmpty()) {
            log.warn("Invalid commentId, will throw exception.");
            throw new InvalidIdException("Invalid Comment ID.");
        }
        Long commentId = null;
        try {
            commentId = Long.parseLong(path);
        } catch (NumberFormatException e) {
            log.error("Invalid commentId in path: {}", path, e);
            throw new InvalidIdException("Invalid Comment ID format.");
        }
        return commentId;
    }

    public static Long parseCategoryId(String categoryIdStr) throws InvalidIdException {
        Long categoryId;
        try {
            categoryId = Long.parseLong(categoryIdStr.trim());
        } catch (NumberFormatException e) {
            log.error("Invalid category ID format: {}", categoryIdStr, e);
            throw new InvalidIdException("Invalid Category ID format.");
        }
        return categoryId;
    }

    public static class InvalidIdException extends Exception {
        public InvalidIdException(String message) {
            super(message);
        }
    }
}
