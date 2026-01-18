package com.parking.parkinglot.common;

import java.util.LinkedList;
import java.util.List;


public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String password;

    private List<CarDto> cars = new LinkedList<>();

    public UserDto(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<CarDto> getCars() {
        return cars;
    }
}
