package com.plog.backend.domain.neighbor.entity;

import com.plog.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "neighbor")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@Builder
public class Neighbor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long neighborId;

    @ManyToOne
    @JoinColumn(name = "neighbor_from", referencedColumnName = "userId")
    private User neighborFrom;

    @ManyToOne
    @JoinColumn(name = "neighbor_to", referencedColumnName = "userId")
    private User neighborTo;

    @Column
    private int neighborType;

    public NeighborType getNeighborType() {return NeighborType.neighborType(neighborType);}
    public void setNeighborType(NeighborType neighborType) {this.neighborType = neighborType.getValue();}

}
