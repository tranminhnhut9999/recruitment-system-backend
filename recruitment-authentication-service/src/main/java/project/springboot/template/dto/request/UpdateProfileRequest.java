package project.springboot.template.dto.request;

import lombok.Data;
import project.springboot.template.config.constants.EAccountStatus;
import project.springboot.template.config.constants.EEduLevel;
import project.springboot.template.config.constants.EGender;
import project.springboot.template.entity.Role;

import javax.persistence.*;

@Data
public class UpdateProfileRequest {
    private String firstname;
    private String lastname;

    private String password;
    private String compPhone;
    private String perPhone;
    private String perAddress1;
    private String perAddress2;
    private String citizenID;
    private String emergencyContactName;
    private String emergencyPhoneNumber;
    private String avatarImg;

    // Only HR could update this field
    private String workingPlace;
    private EEduLevel eduLevel;
    private EGender gender;
}
