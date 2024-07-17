package project.springboot.template.service;

import org.springframework.stereotype.Service;
import project.springboot.template.dto.response.RoleResponse;
import project.springboot.template.entity.Role;
import project.springboot.template.repository.RoleRepository;
import project.springboot.template.util.ObjectUtil;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleResponse> getRoles() {
        List<Role> roles = this.roleRepository.findAll();
        return roles.stream()
                .map(role -> ObjectUtil.copyProperties(role, new RoleResponse(), RoleResponse.class, true)).collect(Collectors.toList());
    }
}
