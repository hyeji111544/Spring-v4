package org.example.springv3.user;

import lombok.Data;

public class UserResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String username;
        private String email;
        private String profile;

        public DTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.profile = user.getProfile();
        }
    }
}
