package org.example.service.actions.impl.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.service.actions.ActionHandler;
import org.example.model.User;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;
import org.example.utils.IdParserUtils;

@Slf4j
public class DeleteUserHandler implements ActionHandler {
    private final UserService userService = new UserServiceImpl();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IdParserUtils.InvalidIdException {
        Long userId = IdParserUtils.parseId(req.getParameter("userId"));
        if (currentUser != null && "admin".equals(currentUser.getRole().toString().toLowerCase())) {
            userService.deleteUserById(userId);
            log.info("User {} deleted successfully", currentUser.getUsername());
        } else {
            log.warn("Unauthorized attempt to delete user.");
            throw new SecurityException("Unauthorized attempt to delete user.");
        }
    }

    @Override
    public String getRedirectUrl(HttpServletRequest req) throws IdParserUtils.InvalidIdException {
        return "/users";
    }
}
