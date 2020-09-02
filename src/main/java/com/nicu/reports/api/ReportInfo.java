package com.nicu.reports.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class ReportInfo {

    @NotNull
    @Size(min = 5, message = "report name needs to have at least {min} characters")
    private String name;

    @NotNull
    @Pattern(regexp = "^[A|B|C]+$", message = "report type must be A, B or C")
    private String type;
}
