package pl.medisite.infrastructure.pjp;

import jakarta.validation.constraints.NotNull;
import pl.medisite.infrastructure.pjp_api.model.AggregateDTO;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class AirInformationMapper {

    public static AirInformation mapAggregateDTO(AggregateDTO aggregateDTO) {
        Double mean = aggregateDTO.getŚRednia24GodzinnaZWyników1Godzinnych();
        Double max = aggregateDTO.getMaksimumZeŚrednich8Godzinnych();
        return AirInformation.builder()
                .region(aggregateDTO.getWojewództwo())
                .county(aggregateDTO.getPowiat())
                .date(aggregateDTO.getData())
                .maxValue(mean == null ? null : BigDecimal.valueOf(mean))
                .meanValue(max ==null? null : BigDecimal.valueOf(max))
                .build();
    }
}
