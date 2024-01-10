package pl.medisite.infrastructure.pjp;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Optional;

@Value
@Builder
public class AirInformation {

    String region;
    String county;
    BigDecimal maxValue;
    BigDecimal meanValue;
    String date;

}
