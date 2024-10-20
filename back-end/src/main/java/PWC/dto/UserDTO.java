package PWC.dto;

import PWC.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;

public class UserDTO {

    private String email;
    private String role;
private long id;
    private String firstname;
    private String lastname;    private Integer tel;
    private String informations;
    private byte[] image;

    public String getInformations() {
        return informations;
    }

    public void setInformations(String informations) {
        this.informations = informations;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getTel() {
        return tel;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }

    public UserDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public UserDTO(String email, String role, long id, Integer tel, String informations, byte[] image, String firstname, String lastname) {
        this.id =id;
        this.email = email;
        this.role = role;
        this.tel = tel;
        this.informations = informations;
        this.image = image;
        this.firstname=firstname;
        this.lastname=lastname;
    }
    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().toString());
        dto.setId(user.getId());
        dto.setInformations(user.getInformations());
        dto.setImage(user.getImage());
        dto.setTel(user.getTel());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());

        return dto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Other getters and setters for additional fields
}