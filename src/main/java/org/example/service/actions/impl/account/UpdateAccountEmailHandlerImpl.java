package org.example.service.actions.impl.account;

import jakarta.servlet.http.HttpServletRequest;
import org.example.service.actions.AccountActionHandler;
import org.example.model.User;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;

import java.io.IOException;

public class UpdateAccountEmailHandlerImpl implements AccountActionHandler {
    private final UserService userService = new UserServiceImpl();

    @Override
    public String execute(HttpServletRequest req, User currentUser)  throws IOException{
        String email = req.getParameter("email");
        return userService.updateEmail(currentUser, email);
    }

}
