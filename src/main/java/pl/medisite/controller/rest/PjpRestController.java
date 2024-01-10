package pl.medisite.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.medisite.infrastructure.pjp.AirInformation;
import pl.medisite.service.PjpService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/pjp")
public class PjpRestController {

    private PjpService pjpService;

    @GetMapping(value = "/test", produces = "application/ld+json")
    public List<AirInformation> testPjp() {
        return pjpService.findInfo();
    }
}
