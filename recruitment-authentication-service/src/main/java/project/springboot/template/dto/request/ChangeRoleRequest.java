package project.springboot.template.dto.request;

import lombok.Data;

@Data
public class ChangeRoleRequest {
    Long accountId;
    String email;
    Long roleId;
}
