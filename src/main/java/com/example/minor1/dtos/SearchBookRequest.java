package com.example.minor1.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookRequest {

    @NotBlank
    private String key;

    @NotBlank
    private String value;

}
