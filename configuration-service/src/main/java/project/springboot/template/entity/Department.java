package project.springboot.template.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "_department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}
