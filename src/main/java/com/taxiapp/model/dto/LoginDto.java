package com.taxiapp.model.dto;


import lombok.*;

import javax.validation.constraints.*;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    private String password;

}
