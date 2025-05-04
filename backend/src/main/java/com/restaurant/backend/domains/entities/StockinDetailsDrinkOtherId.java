package com.restaurant.backend.domains.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockInDetailsDrinkOtherId implements Serializable {
    private Integer stoId;
    private Integer itemId;
}