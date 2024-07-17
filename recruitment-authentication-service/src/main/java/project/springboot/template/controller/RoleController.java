package project.springboot.template.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.springboot.template.dto.response.RoleResponse;
import project.springboot.template.entity.common.ApiResponse;
import project.springboot.template.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getRoles() {
        // Your logic here
        return ResponseEntity.ok(ApiResponse.success(this.roleService.getRoles()));
    }

}
