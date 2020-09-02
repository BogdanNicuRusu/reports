package com.nicu.reports.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nicu.reports.api.UserCredentials;
import com.nicu.reports.dto.UserDTO;
import com.nicu.reports.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class LoginController {

    private final UserService userService;

    @Operation(summary = "Login a user based on credentials")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = UserDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid username or password") })
    @PostMapping(value = "/login")
    public ResponseEntity<UserDTO> login(@RequestBody @Valid UserCredentials userCredentials) {
        UserDTO user = userService.login(userCredentials);
        return new ResponseEntity<>(user, OK);
    }
}
