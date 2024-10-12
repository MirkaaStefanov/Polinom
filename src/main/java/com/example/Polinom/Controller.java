package com.example.Polinom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private PolinomRepository polynomialRepository;
    @Autowired
    private PolinomService polinomService;

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("polynomial", new Polinom());
        return "polynomial_form";
    }

    @PostMapping("/submit")
    public String submitPolynomial(@ModelAttribute Polinom polynomial, Model model) {
        polynomialRepository.save(polynomial);
        model.addAttribute("polynomial", polynomial);
        return "check_root";
    }

    @GetMapping("/check")
    public String checkRoot(@RequestParam Long id, @RequestParam double root, Model model) {
        Polinom polynomial = polynomialRepository.findById(id).orElse(null);
        List<Double> polinomCoef = polynomial.getCoefficients();

        double result = polinomService.ifRoot(polinomCoef, root).get(polinomService.ifRoot(polinomCoef, root).size()-1);

        model.addAttribute("polynomial", polynomial);

        if (result == 0) {
            model.addAttribute("message", "Числото " + root + " е корен на полинома.");
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "Числото " + root + " не е корен на полинома. Остатък: " + result);
            model.addAttribute("messageType", "error");
        }

        return "check_root";
    }

    @GetMapping("/roots")
    public String showRoots(@RequestParam Long id, Model model) {
        Polinom polynomial = polynomialRepository.findById(id).orElse(null);
        if (polynomial != null) {
            List<String> roots = polinomService.theRoots(polynomial.getCoefficients());
            model.addAttribute("polynomial", polynomial);
            model.addAttribute("roots", roots);
        } else {
            model.addAttribute("message", "Полиномът не е намерен.");
            model.addAttribute("messageType", "error");
        }
        return "roots";
    }
}
