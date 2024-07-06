package project.springboot.template.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.springboot.template.dto.request.DepartmentRequestDTO;
import project.springboot.template.dto.response.DepartmentResponseDTO;
import project.springboot.template.dto.response.JobTypeResponseDTO;
import project.springboot.template.entity.Department;
import project.springboot.template.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.springboot.template.util.ObjectUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);
    @Autowired
    private DepartmentRepository departmentRepository;

    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequestDTO) {
        Department department = new Department();
        department.setName(departmentRequestDTO.getName());
        Department savedDepartment = departmentRepository.save(department);
        return mapToResponseDTO(savedDepartment);
    }

    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public DepartmentResponseDTO getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(this::mapToResponseDTO)
                .orElse(null);
    }

    private DepartmentResponseDTO mapToResponseDTO(Department department) {
        return ObjectUtil.copyProperties(department, new DepartmentResponseDTO(), DepartmentResponseDTO.class, true);
    }

    public Boolean deleteDepartmentById(Long id) {
        try {
            departmentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
