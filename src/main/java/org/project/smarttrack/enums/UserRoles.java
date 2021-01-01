package org.project.smarttrack.enums;

public enum UserRoles {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private String name = "";

    UserRoles(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
