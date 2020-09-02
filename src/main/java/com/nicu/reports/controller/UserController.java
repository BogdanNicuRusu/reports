package com.nicu.reports.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicu.reports.api.UserCredentials;
import com.nicu.reports.dto.ReportDTO;
import com.nicu.reports.dto.UserDTO;
import com.nicu.reports.service.ReportService;
import com.nicu.reports.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final ReportService reportService;

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration successful",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid username or password") })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserCredentials userCredentials) {
        UserDTO user = userService.register(userCredentials);
        return new ResponseEntity<>(user, CREATED);
    }
}
