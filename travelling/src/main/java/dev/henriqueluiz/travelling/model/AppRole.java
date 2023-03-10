package dev.henriqueluiz.travelling.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Relation(collectionRelation = "allRoles")
@NoArgsConstructor
@AllArgsConstructor
public class AppRole extends RepresentationModel<AppRole> {
    
    @Id
    @GeneratedValue(
        strategy = SEQUENCE,
        generator = "role_seq"
    )
    @SequenceGenerator(
        name = "role_seq",
        sequenceName = "role_id_seq",
        allocationSize = 1
    )
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @NotBlank
    private String name;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AppRole other = (AppRole) obj;
        if (roleId == null) {
            return other.roleId == null;
        } else return roleId.equals(other.roleId);
    }
}
