package az.compar.fileprovider.util.dto.user;

import az.compar.fileprovider.entity.User;

public class GetUserDto {
    private Long id;
    private String email;

    public GetUserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
