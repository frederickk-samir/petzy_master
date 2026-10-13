package org.example.petzy_back.entity.catalog;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "appointment_statuses")
@Data
public class AppointmentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;
}
