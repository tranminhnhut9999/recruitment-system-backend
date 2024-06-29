package project.springboot.template.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EApplyStatus {
    APPLYING("Đang nộp"),
    EXECUTING("Đang xử lý"),
    REJECTED("Đã từ chối"),
    CONTRACTING("Đang đợi hợp đồng"),
    INTERVIEWING("Đang phỏng vấn"),
    ACCEPTED("Đã chấp thuận");

    private final String name;
}
