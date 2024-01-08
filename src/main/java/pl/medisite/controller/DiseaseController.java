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
import pl.medisite.service.DoctorService;
import pl.medisite.service.PatientService;
import pl.medisite.util.SecurityHelper;

import java.util.Set;

@Controller
@RequestMapping
@AllArgsConstructor
@Slf4j
public class DiseaseController {


    PatientService patientService;
    DoctorService doctorService;
    SecurityHelper securityHelper;
    DiseaseService diseaseService;

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

    @GetMapping("/doctor/patient_diseases_email/{patientEmail}")
    public String showPatientDiseasesByEmail(
            @PathVariable("patientEmail") String patientEmail,
            HttpSession session,
            Model model
    ) throws AccessDeniedException {
        securityHelper.checkDoctorAccessToPatientInformation(
                patientEmail, (String) session.getAttribute("userEmail"));
        Set<DiseaseEntity> diseases = diseaseService.getDiseases(patientEmail);
        model.addAttribute("patientDiseasesList", diseases);
        model.addAttribute("diseaseDTO", new DiseaseDTO());
        return "patient_diseases";
    }

    @GetMapping("/doctor/edit_disease")
    public String showEditPatientDiseases(
            @RequestParam("disease") DiseaseEntity diseaseEntity,
            Model model
    ) {
        model.addAttribute("diseaseEntity", diseaseEntity);
        return "doctor_edit_disease";
    }

    @PostMapping("/doctor/patient_disease")
    public String addDisease(
            @RequestParam("appointmentId") Integer appointmentId,
            @Valid @ModelAttribute("diseaseDTO") DiseaseDTO diseaseEntity,
            RedirectAttributes redirectAttributes) {
        diseaseService.addDiseaseToPatientByAppointmentId(appointmentId, diseaseEntity);
        redirectAttributes.addAttribute("appointmentId", appointmentId);
        return "redirect:/doctor/patient_diseases";
    }
}
