package org.example.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class IdParserUtils {
    public static Long parseId(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (path == null || path.isEmpty()) {
            log.warn("Invalid id, redirecting to error page.");
            resp.sendRedirect(req.getContextPath() + "/error");
            return null;
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        Long topicId = null;
        try {
            topicId = Long.parseLong(path);
        } catch (NumberFormatException e) {
            log.error("Invalid id in path: {}", path, e);
            resp.sendRedirect(req.getContextPath() + "/error");
            return null;
        }

        return topicId;
    }

    public static Long parseCommentId(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (path == null || path.isEmpty()) {
            log.warn("Invalid commentId, redirecting to error page.");
            resp.sendRedirect(req.getContextPath() + "/error");
            return null;
        }
        Long commentId = null;
        try {
            commentId = Long.parseLong(path);
        } catch (NumberFormatException e) {
            log.error("Invalid commentId in path: {}", path, e);
            resp.sendRedirect(req.getContextPath() + "/error");
            return null;
        }
        return commentId;
    }

    public static Long parseCategoryId(String categoryIdStr, HttpServletResponse resp, HttpServletRequest req) throws IOException {
        Long categoryId;
        try {
            categoryId = Long.parseLong(categoryIdStr.trim());
        } catch (NumberFormatException e) {
            log.error("Invalid category ID format: {}", categoryIdStr, e);
            resp.sendRedirect(req.getContextPath() + "/error");
            return null;
        }
        return categoryId;
    }

}
