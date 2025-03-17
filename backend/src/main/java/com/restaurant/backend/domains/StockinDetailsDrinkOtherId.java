package com.restaurant.backend.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class StockinDetailsDrinkOtherId implements java.io.Serializable {
    private static final long serialVersionUID = -570138521681582346L;
    @NotNull
    @Column(name = "sto_id", nullable = false)
    private Integer stoId;

    @NotNull
    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StockinDetailsDrinkOtherId entity = (StockinDetailsDrinkOtherId) o;
        return Objects.equals(this.itemId, entity.itemId) &&
                Objects.equals(this.stoId, entity.stoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, stoId);
    }

}