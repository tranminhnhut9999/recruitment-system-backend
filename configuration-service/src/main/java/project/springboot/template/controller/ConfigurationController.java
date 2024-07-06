package project.springboot.template.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.springboot.template.dto.request.DepartmentRequestDTO;
import project.springboot.template.dto.request.JobTypeRequestDTO;
import project.springboot.template.dto.response.DepartmentResponseDTO;
import project.springboot.template.dto.response.JobTypeResponseDTO;
import project.springboot.template.entity.common.ApiPage;
import project.springboot.template.entity.common.ApiResponse;
import project.springboot.template.service.DepartmentService;
import project.springboot.template.service.JobTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController {

    private final JobTypeService jobTypeService;
    private final DepartmentService departmentService;

    public ConfigurationController(JobTypeService jobTypeService, JobTypeService jobTypeService1, DepartmentService departmentService) {
        this.jobTypeService = jobTypeService1;
        this.departmentService = departmentService;
    }

    @PostMapping("/job-types")
    public ResponseEntity<ApiResponse<JobTypeResponseDTO>> createJobType(@RequestBody JobTypeRequestDTO jobTypeRequestDTO) {
        JobTypeResponseDTO responseDTO = jobTypeService.createJobType(jobTypeRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    @GetMapping("/job-types")
    public ResponseEntity<ApiResponse<List<JobTypeResponseDTO>>> getAllJobTypes() {
        List<JobTypeResponseDTO> jobTypes = jobTypeService.getAllJobTypes();
        return ResponseEntity.ok(ApiResponse.success(jobTypes));
    }

    @GetMapping("/job-type/{id}")
    public ResponseEntity<ApiResponse<JobTypeResponseDTO>> getJobTypeById(@PathVariable Long id) {
        JobTypeResponseDTO responseDTO = jobTypeService.getJobTypeById(id);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    @DeleteMapping("/job-type/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteJobTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(this.jobTypeService.deleteJobTypeById(id)));
    }

    /* This is endpoint for Department*/
    @PostMapping("/departments")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> createDepartment(@RequestBody DepartmentRequestDTO departmentRequestDTO) {
        DepartmentResponseDTO responseDTO = departmentService.createDepartment(departmentRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAllDepartments() {
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments));
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDTO responseDTO = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(departmentService.deleteDepartmentById(id)));
    }
}
