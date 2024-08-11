package project.springboot.template.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.springboot.template.dto.request.SendEmailRequest;
import project.springboot.template.entity.common.ApiException;
import project.springboot.template.service.EmailService;
import project.springboot.template.util.SecurityUtil;


@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public void sendEmailByStaff(@RequestBody SendEmailRequest request) {
        String senderEmail = SecurityUtil.getCurrentUserEmail().orElseThrow(() -> ApiException.create(HttpStatus.FORBIDDEN));

        if (request.isHtml()) {
            emailService.sendHtmlMessage(senderEmail, request.getRecipient(), request.getSubject(), request.getContent());
        } else {
            emailService.sendSimpleMessage(senderEmail, request.getRecipient(), request.getSubject(), request.getContent());
        }
    }

    @PostMapping("/system")
    public void sendSystemEmail(@RequestBody SendEmailRequest request) {
        String senderEmail = SecurityUtil.getCurrentUserEmail().orElseThrow(() -> ApiException.create(HttpStatus.FORBIDDEN));

        if (request.isHtml()) {
            emailService.sendHtmlMessage(senderEmail, request.getRecipient(), request.getSubject(), request.getContent());
        } else {
            emailService.sendSimpleMessage(senderEmail, request.getRecipient(), request.getSubject(), request.getContent());
        }
    }
}
