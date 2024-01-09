package pl.medisite.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.medisite.controller.DTO.DiseaseDTO;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.service.DiseaseService;
import pl.medisite.util.SecurityHelper;

import java.util.Set;

@Controller
@RequestMapping
@AllArgsConstructor
@Slf4j
public class DiseaseController {

    private DiseaseService diseaseService;

    private SecurityHelper securityHelper;


    @GetMapping("/patient/diseases/{email}")
    public String showPatientDiseases(@PathVariable String email,
                                      Model model,
                                      Authentication authentication
    ) throws AccessDeniedException {
        securityHelper.checkUserAccessToPatientInformation(email, (User) authentication.getPrincipal());
        Set<DiseaseEntity> diseases = diseaseService.getDiseases(email);
        model.addAttribute("patientDiseases", diseases);
        return "patient_diseases";
    }

    @GetMapping("/doctor/edit_disease/{diseaseId}")
    public String showEditPatientDiseases(
            @PathVariable("diseaseId") Integer diseaseId,
            Model model
    ) {
        DiseaseDTO diseaseDTO = diseaseService.getDisease(diseaseId);
        model.addAttribute("diseaseDTO", diseaseDTO);
        return "doctor_edit_disease";
    }

    @GetMapping("/doctor/patient_diseases/{patientEmail}")
    public String showPatientDiseasesByEmail(
            @PathVariable("patientEmail") String patientEmail,
            HttpSession session,
            Model model
    ) throws AccessDeniedException {
        securityHelper.checkDoctorAccessToPatientInformation(
                patientEmail, (String) session.getAttribute("userEmail"));
        Set<DiseaseEntity> diseases = diseaseService.getDiseases(patientEmail);
        model.addAttribute("patientDiseases", diseases);
        model.addAttribute("diseaseDTO", new DiseaseDTO(patientEmail));
        return "patient_diseases";
    }

    @PostMapping("/doctor/patient_diseases")
    public String addDisease(
            @Valid @ModelAttribute("diseaseDTO") DiseaseDTO diseaseDTO) {
        diseaseService.addDiseaseToPatient(diseaseDTO);
        return "redirect:/doctor/patient_diseases/" + diseaseDTO.getPatientEmail() + "?";
    }
    @PutMapping("/doctor/edit_disease")
    public String editDisease(
            @ModelAttribute DiseaseDTO diseaseDTO,
            Model model
    ) {
        diseaseService.updateDisease(diseaseDTO);
        model.addAttribute("edited", true);
        return "redirect:/doctor/patient_diseases/" + diseaseDTO.getPatientEmail()+"?";
    }
}
