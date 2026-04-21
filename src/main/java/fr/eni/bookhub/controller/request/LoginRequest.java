package fr.eni.bookhub.controller.request;


import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
