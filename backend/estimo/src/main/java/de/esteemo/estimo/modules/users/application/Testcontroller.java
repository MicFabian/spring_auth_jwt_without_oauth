package de.esteemo.estimo.modules.users.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Testcontroller {

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('USER')")
    public String register() {
        return "test";
    }
}