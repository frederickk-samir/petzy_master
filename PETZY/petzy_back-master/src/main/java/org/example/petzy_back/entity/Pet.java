package org.example.petzy_back.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pets")
@Data
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private String species;

    @Column(nullable = false, length = 50)
    private String breed;

    @Column(nullable = false, length = 50)
    private String gender;
    @Column(nullable = false, length = 50)
    private String age;
    @Column(nullable = false, length = 50)
    private String weight;

    //Relation Many-to-one with user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
}
