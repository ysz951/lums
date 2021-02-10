package com.zhou.lums.payload;

import com.zhou.lums.model.User.Role;

public class UserSummary {
    private Long id;
    private String username;
    private String name;
    private boolean blocked;
    private Role role;

    public UserSummary() {

    }
    public UserSummary(Long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }



    public UserSummary(Long id, String username, String name, boolean blocked, Role role) {
        super();
        this.id = id;
        this.username = username;
        this.name = name;
        this.blocked = blocked;
        this.role = role;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public boolean isBlocked() {
        return blocked;
    }



    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }



    public Role getRole() {
        return role;
    }



    public void setRole(Role role) {
        this.role = role;
    }


}
