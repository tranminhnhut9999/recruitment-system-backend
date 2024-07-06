package project.springboot.template.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import project.springboot.template.dto.request.JobTypeRequestDTO;
import project.springboot.template.dto.response.JobTypeResponseDTO;
import project.springboot.template.entity.JobType;
import project.springboot.template.repository.JobTypeRepository;
import project.springboot.template.util.ObjectUtil;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobTypeService {

    private static final Logger log = LoggerFactory.getLogger(JobTypeService.class);
    private final JobTypeRepository jobTypeRepository;

    public JobTypeService(JobTypeRepository jobTypeRepository) {
        this.jobTypeRepository = jobTypeRepository;
    }

    public JobTypeResponseDTO createJobType(JobTypeRequestDTO jobTypeRequestDTO) {
        JobType jobType = new JobType();
        jobType.setName(jobTypeRequestDTO.getName());
        JobType savedJobType = this.jobTypeRepository.save(jobType);
        return mapToResponseDTO(savedJobType);
    }

    public List<JobTypeResponseDTO> getAllJobTypes() {
        return jobTypeRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public JobTypeResponseDTO getJobTypeById(Long id) {
        return jobTypeRepository.findById(id)
                .map(this::mapToResponseDTO)
                .orElse(null);
    }

    public Boolean deleteJobTypeById(Long id) {
        try {
            jobTypeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    private JobTypeResponseDTO mapToResponseDTO(JobType jobType) {
        return ObjectUtil.copyProperties(jobType, new JobTypeResponseDTO(), JobTypeResponseDTO.class, true);
    }

}
