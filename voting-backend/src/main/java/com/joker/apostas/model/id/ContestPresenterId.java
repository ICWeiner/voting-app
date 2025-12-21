package com.joker.apostas.model.id;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContestPresenterId implements Serializable {

    private Long contestId;
    private Long presenterId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContestPresenterId that)) return false;
        return Objects.equals(contestId, that.contestId)
                && Objects.equals(presenterId, that.presenterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contestId, presenterId);
    }
}
