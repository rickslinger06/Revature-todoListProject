package com.revature.toDoList.dto.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ResetPasswordRequest {
    @NotBlank
    private String currentPassword;
    @Size(min = 6)
    @NotBlank
    private String newPassword;
}
