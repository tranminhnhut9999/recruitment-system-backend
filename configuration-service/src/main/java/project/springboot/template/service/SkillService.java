package project.springboot.template.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.springboot.template.dto.request.DepartmentRequestDTO;
import project.springboot.template.dto.request.SkillRequestDTO;
import project.springboot.template.dto.response.DepartmentResponseDTO;
import project.springboot.template.dto.response.SkillResponseDTO;
import project.springboot.template.entity.Department;
import project.springboot.template.entity.Skill;
import project.springboot.template.repository.SkillRepository;
import project.springboot.template.util.ObjectUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillService {

    private static final Logger log = LoggerFactory.getLogger(SkillService.class);
    @Autowired
    private SkillRepository skillRepository;

    public SkillResponseDTO createDepartment(SkillRequestDTO skillRequestDTO) {
        Skill skill = new Skill();
        skill.setName(skillRequestDTO.getName());
        Skill savedDepartment = skillRepository.save(skill);
        return mapToResponseDTO(savedDepartment);
    }

    public List<SkillResponseDTO> getAllDepartments() {
        return skillRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public SkillResponseDTO getDepartmentById(Long id) {
        return skillRepository.findById(id)
                .map(this::mapToResponseDTO)
                .orElse(null);
    }

    private SkillResponseDTO mapToResponseDTO(Skill skill) {
        return ObjectUtil.copyProperties(skill, new SkillResponseDTO(), SkillResponseDTO.class, true);
    }

    public Boolean deleteSkillById(Long id) {
        try {
            skillRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
