package pl.maksturzynski.cinemabooking.domain.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "hall")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
