package project.springboot.template.service;

import org.springframework.stereotype.Service;
import project.springboot.template.dto.request.WorkingAddressRequestDTO;
import project.springboot.template.dto.response.WorkingAddressResponseDTO;
import project.springboot.template.entity.WorkingAddress;
import project.springboot.template.repository.WorkingAddressRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkingAddressService {

    private final WorkingAddressRepository workingAddressRepository;

    public WorkingAddressService(WorkingAddressRepository workingAddressRepository) {
        this.workingAddressRepository = workingAddressRepository;
    }

    public WorkingAddressResponseDTO create(WorkingAddressRequestDTO workingAddressRequestDTO) {
        WorkingAddress workingAddress = new WorkingAddress();
        workingAddress.setAddress(workingAddressRequestDTO.getAddress());
        workingAddress = workingAddressRepository.save(workingAddress);
        return convertToResponseDTO(workingAddress);
    }

    public WorkingAddressResponseDTO update(Long id, WorkingAddressRequestDTO workingAddressRequestDTO) {
        WorkingAddress workingAddress = workingAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkingAddress not found"));
        workingAddress.setAddress(workingAddressRequestDTO.getAddress());
        workingAddress = workingAddressRepository.save(workingAddress);
        return convertToResponseDTO(workingAddress);
    }

    public void delete(Long id) {
        workingAddressRepository.deleteById(id);
    }

    public WorkingAddressResponseDTO getById(Long id) {
        WorkingAddress workingAddress = workingAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkingAddress not found"));
        return convertToResponseDTO(workingAddress);
    }

    public List<WorkingAddressResponseDTO> getAll() {
        return workingAddressRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private WorkingAddressResponseDTO convertToResponseDTO(WorkingAddress workingAddress) {
        WorkingAddressResponseDTO workingAddressResponseDTO = new WorkingAddressResponseDTO();
        workingAddressResponseDTO.setId(workingAddress.getId());
        workingAddressResponseDTO.setAddress(workingAddress.getAddress());
        return workingAddressResponseDTO;
    }
}
