package project.springboot.template.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@Table(name = "_email")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;
    private String subject;
    @Lob // Adjust length if needed
    private String content;
    private Instant sentAt;
    private boolean isHtml;
    private String sender;
}
