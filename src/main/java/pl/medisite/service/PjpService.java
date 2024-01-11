package pl.medisite.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.medisite.infrastructure.pjp.AirInformation;
import pl.medisite.infrastructure.pjp.PjpClient;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PjpService {

    private PjpClient pjpClient;

    public List<AirInformation> getCountryPm10Info() {
        return pjpClient.getAirInformation();
    }

}
