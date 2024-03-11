package com.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfo {

    @Id
    private String movieInfoId;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Year cannot be null")
    @Positive(message = "Invalid Year")
    private Integer year;
    private List<String> cast;
    private LocalDate release_date;
}
