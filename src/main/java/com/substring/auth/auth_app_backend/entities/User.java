package com.substring.auth.auth_app_backend.entities;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String image;
    private boolean enable = true;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    private Provider provider =Provider.LOCAL;
    private Set<Role> roles = new HashSet<>();
}
