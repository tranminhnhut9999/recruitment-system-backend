package project.springboot.template.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "_working_address")
public class WorkingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
}
