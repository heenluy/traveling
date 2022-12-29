package dev.henriqueluiz.travelling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Travel {
    @Id
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "travel_seq"
    )
    @SequenceGenerator(
            name = "travel_seq",
            sequenceName = "travel_id_seq",
            allocationSize = 1
    )
    @Column(name = "travel_id", nullable = false)
    private Long travelId;
    private String destination;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private BigInteger budget;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    public Long getTravelId() {
        return travelId;
    }

    public void setTravelId(Long travelId) {
        this.travelId = travelId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BigInteger getBudget() {
        return budget;
    }

    public void setBudget(BigInteger budget) {
        this.budget = budget;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Travel travel = (Travel) o;
        return Objects.equals(travelId, travel.travelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(travelId);
    }
}