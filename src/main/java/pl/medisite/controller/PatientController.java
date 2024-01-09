package pl.medisite.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.DoctorDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.mapper.DoctorEntityMapper;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DoctorService;
import pl.medisite.service.PatientService;
import pl.medisite.service.UserService;
import pl.medisite.util.Constants;
import pl.medisite.util.SecurityHelper;

import java.awt.print.Pageable;
import java.util.AbstractMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/patient")
@AllArgsConstructor
@Slf4j
public class PatientController {

    private UserService userService;
    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;

    private SecurityHelper securityHelper;
    private HttpSession session;

    @GetMapping()
    public String showPatientPage(Model model) {
        String email = (String) session.getAttribute("userEmail");
        PersonDTO patient = patientService.getPatient(email);
        model.addAttribute("patient", patient);
        return "profile";
    }

    @GetMapping("/doctors")
    public String showDoctorsList(Model model, @RequestParam(defaultValue = "1") Integer page) {
        PageRequest pageable = PageRequest.of(page-1, Constants.ELEMENTS_ON_PAGE, Sort.by(Sort.Direction.ASC,"surname"));
        AbstractMap.SimpleEntry<Integer, List<PersonDTO.DoctorDTO>> allDoctors = userService.getAllDoctors(pageable);
        model.addAttribute("personsData", allDoctors.getValue());
        model.addAttribute("numberOfPages", allDoctors.getKey());
        return "patient_doctor_list";
    }

    @GetMapping("/appointments")
    public String showPatientAppointments(
            Model model,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(defaultValue = "1")  Integer page
    ) throws AccessDeniedException {
        PageRequest pageable = PageRequest.of(page-1, Constants.ELEMENTS_ON_PAGE, Sort.by(Sort.Direction.ASC,"appointmentStart"));
        String email = (String) session.getAttribute("userEmail");
        AbstractMap.SimpleEntry<Integer, List<AppointmentDTO>> appointments = appointmentService.getPatientAppointments(email, filter,pageable);
        model.addAttribute("patientAppointments", appointments.getValue());
        model.addAttribute("numberOfPages", appointments.getKey());
        if( !Objects.isNull(filter) ){
            model.addAttribute("filter", filter);
        }
        return "patient_appointments";
    }

    @GetMapping("/book_appointment")
    public String showBookAppointment(@RequestParam(name = "email") @Email String email, Model model) {
        PersonDTO.DoctorDTO doctor = doctorService.getDoctor(email);
        Set<AppointmentDTO> appointments = appointmentService.getDoctorFutureFreeAppointments(email);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);
        return "patient_book_appointment";
    }

    @PutMapping("/book_appointment/{appointmentId}")
    public String bookAppointment(
            @PathVariable(name = "appointmentId") Integer appointmentId,
            RedirectAttributes redirectAttributes) {
        String userEmail = (String) session.getAttribute("userEmail");
        appointmentService.bookAppointment(appointmentId, userEmail);
        redirectAttributes.addFlashAttribute("booked", true);
        return "redirect:/patient/appointments";
    }

    @DeleteMapping("/delete_appointment/{appointmentId}")
    public String deleteAppointment(
            @PathVariable("appointmentId") Integer appointmentId,
            RedirectAttributes redirectAttributes
    )  {
        appointmentService.deleteAppointment(appointmentId);
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/patient/appointments";
    }

    @PutMapping("/update")
    public String updateInformation(@Valid @ModelAttribute("patient") PersonDTO user, Model model){
        patientService.updatePatient(user);
        model.addAttribute("update", true);
        return "profile";
    }


    @DeleteMapping("/{email}")
    public String deletePatient(@PathVariable(name = "email") @Email String email, Model model, Authentication authentication) {
        securityHelper.compareUserEmailWithRequestEmail((User) authentication.getPrincipal(),email);
        patientService.deletePatient(email);
        model.addAttribute("deleted", true);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "login";
    }


}
