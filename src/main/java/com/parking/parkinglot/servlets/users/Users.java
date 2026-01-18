package com.parking.parkinglot.servlets.users;

import com.parking.parkinglot.common.UserDto;
import com.parking.parkinglot.ejb.InvoiceBean;
import com.parking.parkinglot.ejb.UsersBean;
import jakarta.annotation.security.DeclareRoles;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.lang.Long.parseLong;

@DeclareRoles({"READ_USERS", "WRITE_USERS", "INVOICING"})
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"READ_USERS"}),
        httpMethodConstraints = {@HttpMethodConstraint(value = "POST", rolesAllowed = {"WRITE_USERS"})})
@WebServlet(name = "Users", value = "/Users")
public class Users extends HttpServlet {

    @Inject
    private UsersBean usersBean;
    @Inject
    private InvoiceBean invoiceBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserDto> usersList = usersBean.findAllUsers();
        request.setAttribute("users", usersList);

        if (request.isUserInRole("INVOICING")) {
            if (!invoiceBean.getUserIds().isEmpty()) {
                Collection<String> usernames = usersBean.findUsernamesByUserIds(invoiceBean.getUserIds());
                request.setAttribute("invoices", usernames);
            }
        } else {
            request.setAttribute("invoices", "No avail.");
        }

        request.getRequestDispatcher("/WEB-INF/pages/users/users.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] userIdsAsString = request.getParameterValues("user_ids");
        if (userIdsAsString != null){
            List<Long> userIds = Arrays.stream(userIdsAsString).map(s -> parseLong(s)).toList();
            invoiceBean.getUserIds().addAll(userIds);
        }
        response.sendRedirect(request.getContextPath() + "/Users");
    }
}
