package project.springboot.template.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
public class SendEmailRequest {
    private String recipient;
    private String subject;
    private String content;
    private boolean isHtml;
}
