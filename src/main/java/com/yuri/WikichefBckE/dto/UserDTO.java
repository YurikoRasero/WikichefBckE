package com.yuri.WikichefBckE.dto;
import com.yuri.WikichefBckE.modelo.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Role role;

    }


