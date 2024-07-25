package project.springboot.template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.springboot.template.dto.request.*;
import project.springboot.template.dto.response.ProfileResponse;
import project.springboot.template.dto.response.RoleResponse;
import project.springboot.template.entity.common.ApiResponse;
import project.springboot.template.service.AccountService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestBody ChangePasswordRequest request) {
        boolean result = accountService.changePassword(request);
        String message = "";
        if (result) {
            message = "change password successful";
        } else {
            message = "change password failed";
        }
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> register(
            @RequestBody RegisterRequest request
    ) {
        boolean createUserResult = accountService.createNewUser(request);
        if (createUserResult) {
            return ResponseEntity.ok(ApiResponse.success("create user successful"));
        } else {
            return ResponseEntity.ok(ApiResponse.failed("create user failed"));
        }
    }

    // API: InActive and Active account
    @PutMapping("/change-status")
    public ResponseEntity<ApiResponse<String>> changeAccountStatus(@RequestBody ChangeAccountStatusRequest request) {
        this.accountService.changeAccountStatus(request.getEmail(), request.getStatus());
        return ResponseEntity.ok(ApiResponse.success("change account status successful"));
    }

    // API: Get Account Profile
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfileByUser() {
        return ResponseEntity.ok(ApiResponse.success(this.accountService.getUserProfileByUser()));
    }

    // API: Update Profile
    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<Boolean>> updateProfile(@PathVariable Long id, @RequestBody UpdateProfileRequest request) {
        // Your logic here
        return ResponseEntity.ok(ApiResponse.success(this.accountService.updateProfile(id, request)));
    }


    // API: Change Role Account
    @PutMapping("/role")
    public ResponseEntity<ApiResponse<String>> changeAccountRole(@RequestBody ChangeRoleRequest request) {
        this.accountService.changeAccountRole(request);
        return ResponseEntity.ok(ApiResponse.success("change account role successful"));
    }

    // API: Reset Password -> use when user forget password
    @PutMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        // Your logic here
        Boolean result = this.accountService.resetPassword(request.getAccountId());
        if (result) {
            return ResponseEntity.ok(ApiResponse.success("reset password successful"));
        } else {
            return ResponseEntity.ok(ApiResponse.failed("reset password failed"));
        }
    }

    // Verify Token
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifyUser() {
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    @GetMapping("/recruiters")
    public ResponseEntity<ApiResponse<List<ProfileResponse>>> getRecruiters() {
        return ResponseEntity.ok(ApiResponse.success(this.accountService.getAccountByRole("HR_STAFF")));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProfileResponse>>> getAllAccount() {
        return ResponseEntity.ok(ApiResponse.success(this.accountService.getAllAccounts()));
    }


}
