package project.springboot.template.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import project.springboot.template.config.RabbitMQConfig;
import project.springboot.template.dto.response.JobStatisticsResponse;
import project.springboot.template.entity.Job;
import project.springboot.template.entity.common.ApiException;
import project.springboot.template.repository.JobRepository;

import java.util.Map;

@Component
public class EventListener {
    private static final Logger log = LoggerFactory.getLogger(EventListener.class);
    private final JobRepository jobRepository;
    private final ObjectMapper objectMapper;

    public EventListener(JobRepository jobRepository, ObjectMapper objectMapper) {
        this.jobRepository = jobRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.NEW_APPLICATION_QUEUE)
    public void handleNewApplicationCreated(@Payload String message, @Headers Map<String, Object> headers) {
        try {
            JobStatisticsResponse jobStatisticsResponse = objectMapper.readValue(message, JobStatisticsResponse.class);
            Job job = this.jobRepository.findById(jobStatisticsResponse.getJobId()).orElseThrow(() -> new RuntimeException("Không tìm thấy công việc cần cập nhật"));
            job.setNumberApplications(jobStatisticsResponse.getTotalApplied());
            jobRepository.save(job);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        System.out.println("Received specific event: " + message);
    }

    @RabbitListener(queues = RabbitMQConfig.GENERAL_EVENT_QUEUE)
    public void handleGeneralEvent(@Payload String message, @Headers Map<String, Object> headers) {
        System.out.println("Received general event: " + message);
    }
}