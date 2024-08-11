package project.springboot.template.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.springboot.template.dto.request.DepartmentRequestDTO;
import project.springboot.template.dto.request.JobTypeRequestDTO;
import project.springboot.template.dto.request.SkillRequestDTO;
import project.springboot.template.dto.request.WorkingAddressRequestDTO;
import project.springboot.template.dto.response.DepartmentResponseDTO;
import project.springboot.template.dto.response.JobTypeResponseDTO;
import project.springboot.template.dto.response.SkillResponseDTO;
import project.springboot.template.dto.response.WorkingAddressResponseDTO;
import project.springboot.template.entity.common.ApiPage;
import project.springboot.template.entity.common.ApiResponse;
import project.springboot.template.service.DepartmentService;
import project.springboot.template.service.JobTypeService;
import project.springboot.template.service.SkillService;
import project.springboot.template.service.WorkingAddressService;

import java.util.List;

@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController {

    private final JobTypeService jobTypeService;
    private final DepartmentService departmentService;
    private final SkillService skillService;
    private final WorkingAddressService workingAddressService;

    public ConfigurationController(JobTypeService jobTypeService, JobTypeService jobTypeService1, DepartmentService departmentService, SkillService skillService, WorkingAddressService workingAddressService) {
        this.jobTypeService = jobTypeService1;
        this.departmentService = departmentService;
        this.skillService = skillService;
        this.workingAddressService = workingAddressService;
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

    @GetMapping("/job-types/{id}")
    public ResponseEntity<ApiResponse<JobTypeResponseDTO>> getJobTypeById(@PathVariable Long id) {
        JobTypeResponseDTO responseDTO = jobTypeService.getJobTypeById(id);
        return ResponseEntity.ok(ApiResponse.success(responseDTO));
    }

    @DeleteMapping("/job-types/{id}")
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

    /* This is endpoint for Skills */
    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<SkillResponseDTO>> createSkill(@RequestBody SkillRequestDTO skillRequestDTO) {
        SkillResponseDTO skill = skillService.createSkill(skillRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(skill));
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<List<SkillResponseDTO>>> getAllSkill() {
        List<SkillResponseDTO> skills = skillService.getSkills();
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(this.skillService.deleteSkillById(id)));
    }

    /**
     * Working Address Configuration
     */
    @PostMapping("/working-addresses")
    public ResponseEntity<ApiResponse<WorkingAddressResponseDTO>> createWorkingAddress(@RequestBody WorkingAddressRequestDTO workingAddressRequestDTO) {
        WorkingAddressResponseDTO workingAddressResponseDTO = workingAddressService.create(workingAddressRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(workingAddressResponseDTO));
    }

    @PutMapping("/working-addresses/{id}")
    public ResponseEntity<ApiResponse<WorkingAddressResponseDTO>> updateWorkingAddress(@PathVariable Long id, @RequestBody WorkingAddressRequestDTO workingAddressRequestDTO) {
        WorkingAddressResponseDTO workingAddressResponseDTO = workingAddressService.update(id, workingAddressRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(workingAddressResponseDTO));
    }

    @DeleteMapping("/working-addresses/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workingAddressService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/working-addresses/{id}")
    public ResponseEntity<ApiResponse<WorkingAddressResponseDTO>> getWorkingAddressById(@PathVariable Long id) {
        WorkingAddressResponseDTO workingAddressResponseDTO = workingAddressService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(workingAddressResponseDTO));
    }

    @GetMapping("/working-addresses")
    public ResponseEntity<ApiResponse<List<WorkingAddressResponseDTO>>> getAllWorkingAddress() {
        List<WorkingAddressResponseDTO> workingAddressResponseDTOs = workingAddressService.getAll();
        return ResponseEntity.ok(ApiResponse.success(workingAddressResponseDTOs));
    }
}
