package project.springboot.template.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import project.springboot.template.config.constants.EAccountStatus;
import project.springboot.template.config.constants.EEduLevel;
import project.springboot.template.config.constants.EGender;
import project.springboot.template.entity.Role;

import javax.persistence.*;
import java.time.Instant;

@Data
public class UpdateProfileRequest {
    private String firstname = "";
    private String lastname = "";
    private String perPhone = "";
    private String perAddress1 = "";
    private String perAddress2 = "";
    private String citizenID = "";
    private String emergencyContactName = "";
    private String emergencyPhoneNumber = "";
    private String workingPlace = "";
    private EGender gender = EGender.OTHER;
    private MultipartFile image;
    private Instant dob;
    private String roleCode;
}
