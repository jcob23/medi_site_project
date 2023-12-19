package pl.medisite.controller.buisness;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.service.PatientService;

import java.util.Set;


@Controller
@RequestMapping("/patient")
@AllArgsConstructor
@Slf4j
public class PatientController {

    private PatientService patientService;

    @GetMapping()
    public String showPatientPage() {
        return "patientPage";
    }

    @GetMapping("/diseases/{email}")
    public String showPatientDiseases(@PathVariable String email, Model model){
        Set<DiseaseEntity> diseases = patientService.getDiseases(email);
        model.addAttribute("patientDiseases", diseases);
        return "patient_diseases";
    }

    @DeleteMapping()
    public String deletePatient(Authentication authentication, Model model) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String email = principal.getUsername();
        patientService.deletePatient(email);
        model.addAttribute("deleted", true);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "login";
    }

}
