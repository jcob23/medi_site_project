package pl.medisite.infrastructure.pjp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.medisite.infrastructure.pjp_api.api.AgregatyPomiarwApi;
import pl.medisite.infrastructure.pjp_api.model.AggregateDTO;
import pl.medisite.infrastructure.pjp_api.model.AggregatePM10DataDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PjpClient {

    private final AgregatyPomiarwApi agregatyPomiarwApi;
    private static final String allcounty = "Katowice,Gdańsk,Warszawa,Kraków,Wroclaw,Białystok,Szczecin,Poznań,Lublin";

    public List<AirInformation> getAirInformation(){
        List<AirInformation> collect = getAirInformationFromApi().stream().map(AirInformationMapper::mapAggregateDTO).toList();
        return collect;
    }

    private List<AggregateDTO> getAirInformationFromApi(){
        AggregatePM10DataDTO countryAirInformation = Optional.ofNullable(agregatyPomiarwApi.getAggregatePm10DataUsingGET(0, 200, null, allcounty, null).block())
                .orElseThrow(() -> new RuntimeException("Nie znaleziono informacji o pwietrzu w polsce"));
        return countryAirInformation.getListaDanychZagregowanych();

    }
}
