package project.springboot.template.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.springboot.template.dto.request.CreateJobRequest;
import project.springboot.template.dto.request.UpdateJobRequest;
import project.springboot.template.dto.response.JobDetailResponse;
import project.springboot.template.dto.response.JobResponse;
import project.springboot.template.entity.Job;
import project.springboot.template.entity.common.ApiException;
import project.springboot.template.repository.JobRepository;
import project.springboot.template.util.ObjectUtil;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JobDetailResponse createJob(CreateJobRequest createJobRequest) {
        Job.JobBuilder jobBuilder = Job.builder()
                .department(createJobRequest.getDepartment())
                .endDate(createJobRequest.getEndDate())
                .startDate(createJobRequest.getStartDate())
                .description(createJobRequest.getDescription())
                .title(createJobRequest.getTitle())
                .targetNumber(createJobRequest.getTargetNumber())
                .salaryRangeTo(createJobRequest.getSalaryRangeTo())
                .salaryRangeFrom(createJobRequest.getSalaryRangeFrom())
                .status(createJobRequest.isStatus())
                .requiredExperience(createJobRequest.getRequiredExperience())
                .jobType(createJobRequest.getJobType())
                .workingPlace(createJobRequest.getWorkingPlace());

        if (!createJobRequest.getKeywords().isEmpty()) {
            String keyworkAsString = String.join("-", createJobRequest.getKeywords());
            jobBuilder.keywords(keyworkAsString);
        }
        if (!createJobRequest.getRecruiters().isEmpty()) {
            String recruiterAsString = String.join("-", createJobRequest.getRecruiters());
            jobBuilder.recruiters(recruiterAsString);
        }
        Job job = jobBuilder.build();
        Job savedJob = jobRepository.save(job);
        return ObjectUtil.copyProperties(savedJob, new JobDetailResponse(), JobDetailResponse.class, true);
    }

    public List<JobResponse> getHiringJob() {
        List<Job> hiringJobs = jobRepository.findAllByStatusIs(true);
        List<JobResponse> jobResponses = hiringJobs.stream()
                .map(job -> {
                    JobResponse jobResponse = ObjectUtil.copyProperties(job, new JobResponse(), JobResponse.class, true);
                    jobResponse.setKeywords(this.extractKeyword(job.getKeywords()));
                    jobResponse.setRecruiters(this.extractKeyword(job.getRecruiters()));
                    return jobResponse;
                }).collect(Collectors.toList());

        return jobResponses;
    }

    public List<JobResponse> getAllJob() {
        List<Job> hiringJobs = jobRepository.findAll();
        List<JobResponse> jobResponses = hiringJobs.stream()
                .map(job -> {
                    JobResponse jobResponse = ObjectUtil.copyProperties(job, new JobResponse(), JobResponse.class, true);
                    jobResponse.setKeywords(this.extractKeyword(job.getKeywords()));
                    return jobResponse;
                }).collect(Collectors.toList());

        return jobResponses;
    }

    public JobDetailResponse getHiringDetailJob(Long id) {
        Job hiringJob = jobRepository.findJobByIdAndStatus(id, true)
                .orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không tìm thấy job:" + id));
        if (hiringJob.isStatus() == false) {
            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Công việc không khả dụng");
        }
        JobDetailResponse jobResponse = ObjectUtil.copyProperties(hiringJob, new JobDetailResponse(), JobDetailResponse.class, true);
        jobResponse.setKeywords(this.extractKeyword(hiringJob.getKeywords()));
        return jobResponse;
    }

    public JobDetailResponse getDetailJob(Long id) {
        Job hiringJob = jobRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không tìm thấy job:" + id));
        JobDetailResponse jobResponse = ObjectUtil.copyProperties(hiringJob, new JobDetailResponse(), JobDetailResponse.class, true);
        jobResponse.setKeywords(this.extractKeyword(hiringJob.getKeywords()));
        jobResponse.setRecruiters(this.extractKeyword(hiringJob.getRecruiters()));
        return jobResponse;
    }


    private Set<String> extractKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new HashSet<>();
        }
        return Set.of(keyword.split("-"));
    }

    public JobResponse changeJobStatus(Long id, boolean status) {
        Job job = this.jobRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không tìm thấy công việc:" + id));
        job.setStatus(status);
        this.jobRepository.save(job);
        return ObjectUtil.copyProperties(job, new JobResponse(), JobResponse.class, true);
    }

    public JobDetailResponse updateJob(Long id, UpdateJobRequest updateJobRequest) {
        Job updatedJob = this.jobRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không tìm thấy công việc:" + id));

        updatedJob.setDepartment(updateJobRequest.getDepartment());
        updatedJob.setEndDate(updateJobRequest.getEndDate());
        updatedJob.setDescription(updateJobRequest.getDescription());
        updatedJob.setTitle(updateJobRequest.getTitle());
        updatedJob.setTargetNumber(updateJobRequest.getTargetNumber());
        updatedJob.setSalaryRangeTo(updateJobRequest.getSalaryRangeTo());
        updatedJob.setSalaryRangeFrom(updateJobRequest.getSalaryRangeFrom());
        updatedJob.setStatus(updateJobRequest.isStatus());
        updatedJob.setRequiredExperience(updateJobRequest.getRequiredExperience());

        if (!updateJobRequest.getKeywords().isEmpty()) {
            String keyworkAsString = String.join("-", updateJobRequest.getKeywords());
            updatedJob.setKeywords(keyworkAsString);
        }
        Job savedJob = jobRepository.save(updatedJob);
        return ObjectUtil.copyProperties(savedJob, new JobDetailResponse(), JobDetailResponse.class, true);
    }

    public Boolean deleteJob(Long id) {
        try {
            Job job = this.jobRepository.findById(id)
                    .orElseThrow(() -> ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Không tìm thấy công việc:" + id));
            this.jobRepository.delete(job);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
