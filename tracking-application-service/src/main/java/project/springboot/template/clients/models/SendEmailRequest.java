package project.springboot.template.clients.models;

import lombok.Data;

@Data
public class SendEmailRequest {
    private String recipient;
    private String subject;
    private String content;
    private boolean isHtml;
}
