package com.parking.parkinglot.servlets.cars;

import com.parking.parkinglot.common.CarDto;
import com.parking.parkinglot.ejb.CarsBean;
import jakarta.inject.Inject;
import jakarta.annotation.security.DeclareRoles;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@DeclareRoles({"READ_CARS", "WRITE_CARS"})
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {"READ_CARS"}),
        httpMethodConstraints = {@HttpMethodConstraint(value = "POST", rolesAllowed = {"WRITE_CARS"})})
@WebServlet(name = "Cars", value = "/Cars")
public class Cars extends HttpServlet {

    @Inject
    private CarsBean carsBean;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


//        Principal userPrincipal = request.getUserPrincipal();;
//        Realm realm = request.getServletContext().getRealm();
//
//        // Check for a role defined directly as a <security-role>
//         realm.hasRole(getWrapper(), userPrincipal, role);

//        boolean b =  request.isUserInRole("READ_CARS");

        List<CarDto> cars = carsBean.findAllCars();
        request.setAttribute("cars", cars);
        request.setAttribute("numberOfFreeParkingSpots", 10);
        request.getRequestDispatcher("/WEB-INF/pages/cars/cars.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] carIdsAsString = request.getParameterValues("car_ids");
        if (carIdsAsString != null) {
            List<Long> carIds = Arrays.stream(carIdsAsString).map(Long::parseLong).toList();
            carsBean.deleteById(carIds);
        }
        response.sendRedirect(request.getContextPath() + "/Cars");
    }
}
