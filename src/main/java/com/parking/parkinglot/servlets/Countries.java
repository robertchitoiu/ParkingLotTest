package com.parking.parkinglot.servlets;

import com.parking.parkinglot.ejb.CountryBean;
import com.parking.parkinglot.entities.Country;
import jakarta.annotation.security.DeclareRoles;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;


@DeclareRoles({"READ_COUNTRIES", "WRITE_COUNTRIES"})
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"READ_COUNTRIES"}),
        httpMethodConstraints = {@HttpMethodConstraint(value = "POST", rolesAllowed = {"WRITE_COUNTRIES"})})
@WebServlet(name = "Countries", value = "/Countries")
public class Countries extends HttpServlet {

    @Inject
    CountryBean countryBean;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {

        List<Country> countries = countryBean.findAllCountries();
        request.setAttribute("countries", countries);

        request.getRequestDispatcher("/WEB-INF/pages/users/countries.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
    }
}