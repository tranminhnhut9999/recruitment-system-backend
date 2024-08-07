package project.springboot.template.service;

import io.minio.ObjectWriteResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import project.springboot.template.config.constants.EAccountStatus;
import project.springboot.template.config.constants.EEduLevel;
import project.springboot.template.config.constants.EGender;
import project.springboot.template.dto.request.ChangePasswordRequest;
import project.springboot.template.dto.request.ChangeRoleRequest;
import project.springboot.template.dto.request.RegisterRequest;
import project.springboot.template.dto.request.UpdateProfileRequest;
import project.springboot.template.dto.response.ProfileResponse;
import project.springboot.template.dto.response.RoleResponse;
import project.springboot.template.entity.Account;
import project.springboot.template.entity.Role;
import project.springboot.template.entity.common.ApiException;
import project.springboot.template.repository.AccountRepository;
import project.springboot.template.repository.RoleRepository;
import project.springboot.template.util.ObjectUtil;
import project.springboot.template.util.SecurityUtil;
import project.springboot.template.util.UrlUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Value("${jwt.defaultPassword}")
    private String defaultPassword;
    private final MinioService minioService;
    @Value("${minio.endpoint}")
    String minioUrl;

    public boolean changePassword(ChangePasswordRequest request) {
        try {
            Account account = SecurityUtil.getCurrentUserAccountLogin()
                    .orElseThrow(() -> ApiException.create(HttpStatus.FORBIDDEN).withMessage("Access Denied"));
            // check if the current password is correct
            if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
                throw new IllegalStateException("Wrong password");
            }
            // check if the two new passwords are the same
            if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                throw new IllegalStateException("Password are not the same");
            }

            // update the password
            account.setPassword(passwordEncoder.encode(request.getNewPassword()));

            // save the new password
            accountRepository.save(account);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createNewUser(RegisterRequest request) {
        try {
            boolean existedAccount = accountRepository.findByEmail(request.getEmail()).isPresent();
            if (existedAccount) {
                throw ApiException.create(HttpStatus.PRECONDITION_FAILED).withMessage("email has existed, please choose another one!");
            }
            Account.AccountBuilder accountBuilder = Account.builder()
                    .firstname(request.getFirstName())
                    .lastname(request.getLastName())
                    .email(request.getEmail())
                    .gender(request.getGender())
                    .status(EAccountStatus.ACTIVATE)
                    .dob(request.getDob())
                    .citizenID(request.getCitizenID())
                    .workingPlace(request.getWorkingAddress())
                    .password(passwordEncoder.encode(defaultPassword));

            String roleCode = request.getRole();
            if (roleCode != null && !roleCode.isEmpty()) {
                Role role = roleRepository.findRoleByCode(roleCode)
                        .orElseThrow(() -> ApiException.create(HttpStatus.PRECONDITION_FAILED).withMessage("not found role:" + roleCode));
                accountBuilder.role(role);
            }
            Account newAccount = accountBuilder.build();
            this.accountRepository.save(newAccount);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ProfileResponse getUserProfileByUser() {
        Account account = SecurityUtil.getCurrentUserAccountLogin().orElseThrow(() -> ApiException.create(HttpStatus.FORBIDDEN));
        return convertAccountToResponse(account);
    }

    private static @NotNull ProfileResponse convertAccountToResponse(Account account) {
        ProfileResponse profileResponse = ObjectUtil.copyProperties(account, new ProfileResponse(), ProfileResponse.class, true);
        profileResponse.setStatus(account.getStatus().name());
//        if (account.getEduLevel() != null) {
//            EEduLevel eduLevel = account.getEduLevel();
//            profileResponse.setEduLevelCode(eduLevel.name());
//            profileResponse.setEduLevelDescription(eduLevel.getDescription());
//
//        }
        if (account.getGender() != null) {
            EGender gender = account.getGender();
            profileResponse.setGenderCode(gender.name());
            profileResponse.setGenderDescription(gender.getDescription());
        }
        RoleResponse roleResponse = ObjectUtil.copyProperties(account.getRole(), new RoleResponse(), RoleResponse.class, true);
        profileResponse.setRole(roleResponse);
        return profileResponse;
    }

    public boolean changeAccountStatus(String email, EAccountStatus status) {
        Account changeAccount = this.accountRepository
                .findByEmail(email).orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Account not found with email:" + email));
        changeAccount.setStatus(status);
        this.accountRepository.save(changeAccount);
        return true;
    }

    public List<ProfileResponse> getAccountByRole(String roleCode) {
        List<Account> accountsWithRole = this.accountRepository.findByRole_Code(roleCode);
        return accountsWithRole.stream()
                .map(a -> ObjectUtil.copyProperties(a, new ProfileResponse(), ProfileResponse.class, true)).collect(Collectors.toList());
    }

    public Boolean changeAccountRole(ChangeRoleRequest request) {
        String email = request.getEmail();
        Account changeAccount = this.accountRepository
                .findByEmail(email).orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Account not found with email:" + email));
        Role role = this.roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy vai trò với idL" + request.getRoleId()));
        changeAccount.setRole(role);
        this.accountRepository.save(changeAccount);
        return true;
    }

    public Boolean resetPassword(Long accountId) {
        Account changeAccount = this.accountRepository
                .findById(accountId).orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Account not found with id:" + accountId));
        changeAccount.setPassword(this.passwordEncoder.encode(defaultPassword));
        this.accountRepository.save(changeAccount);
        return true;
    }

    public boolean updateProfile(Long accountId, UpdateProfileRequest request) {
        Account currentUser = SecurityUtil.getCurrentUserAccountLogin().orElseThrow(() -> ApiException.create(HttpStatus.FORBIDDEN));
        Role role = currentUser.getRole();
        if ((role.getCode().equals("ADMIN") || role.getCode().equals("HR_MANAGER")) && !accountId.equals(currentUser.getId())) {
            return this.adminUpdateProfile(accountId, request);
        } else {
            return this.selfUpdateProfile(accountId, request);
        }
    }

    public List<ProfileResponse> getAllAccounts() {
        Account currentAccount = SecurityUtil.getCurrentUserAccountLogin()
                .orElseThrow(() -> ApiException.create(HttpStatus.FORBIDDEN).withMessage("Tài khoàn không tìm thấy"));

        Role role = currentAccount.getRole();
        List<Account> accounts;
        List<String> searchingAccountWithRoles;

        switch (role.getCode()) {
            case "ADMIN":
                searchingAccountWithRoles = List.of("HR_MANAGER", "HR_STAFF");
                accounts = this.accountRepository.findAllIsContainInRoleCode(searchingAccountWithRoles);
                break;
            case "HR_MANAGER":
                searchingAccountWithRoles = List.of("HR_STAFF");
                accounts = this.accountRepository.findAllIsContainInRoleCode(searchingAccountWithRoles);
                break;
            default:
                throw ApiException.create(HttpStatus.FORBIDDEN).withMessage("Tài khoản không có quyền sử dụng chức năng");
        }
        if (!accounts.isEmpty()) {
            return accounts.stream().map(AccountService::convertAccountToResponse).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    // There are some field that user could not update but only ADMIN can do that
    // One function for self update and one for admin
    // Field allow staff update: perAddress1, perAddress2, emergencyContactName, emergencyPhoneNumber, compPhone, perPhone, avatarImg
    private boolean selfUpdateProfile(Long accountId, UpdateProfileRequest request) {
        Account currentStaff = SecurityUtil.getCurrentUserAccountLogin()
                .orElseThrow(() -> ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage("Không tìm thấy người dùng, vui lòng liên hệ admin"));

        currentStaff.setPerAddress1(request.getPerAddress1());
        currentStaff.setPerAddress2(request.getPerAddress2());
        currentStaff.setEmergencyContactName(request.getEmergencyContactName());
        currentStaff.setEmergencyPhoneNumber(request.getEmergencyPhoneNumber());
        currentStaff.setPerPhone(request.getPerPhone());
        MultipartFile staffImg = request.getImage();

        if (staffImg != null) {
            try {
                ObjectWriteResponse objectResponse = this.minioService.uploadFile(staffImg.getOriginalFilename() + Instant.now().toString(), staffImg.getContentType(), staffImg.getInputStream(), staffImg.getSize());
                String accessResourceURL = UrlUtil.buildUrl(minioUrl, objectResponse);
                currentStaff.setAvatarImg(accessResourceURL);
            } catch (IOException e) {
                log.error(e.getMessage());
                return false;
            }
        }
        this.accountRepository.save(currentStaff);
        return true;
    }


    //Fields Admin could update: citizenID, workingAddress, role,gender, dob
    private boolean adminUpdateProfile(Long accountId, UpdateProfileRequest request) {
        Account updatedAccount = this.accountRepository.findById(accountId)
                .orElseThrow(() -> ApiException.create(HttpStatus.METHOD_NOT_ALLOWED).withMessage("Không tìm thấy người tài khoản cập nhật"));

        updatedAccount.setFirstname(request.getFirstname());
        updatedAccount.setLastname(request.getLastname());
        updatedAccount.setWorkingPlace(request.getWorkingPlace());
        updatedAccount.setCitizenID(request.getCitizenID());
        updatedAccount.setDob(request.getDob());
        updatedAccount.setGender(request.getGender());

        if (request.getRoleCode() != null && !request.getRoleCode().isEmpty()) {
            Role role = this.roleRepository.findRoleByCode(request.getRoleCode())
                    .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không thể tìm thấy vai trò:" + request.getRoleCode()));
            updatedAccount.setRole(role);
        }
        this.accountRepository.save(updatedAccount);
        return true;
    }


}
