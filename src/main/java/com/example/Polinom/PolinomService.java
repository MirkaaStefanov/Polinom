package com.example.Polinom;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PolinomService {

    public double ifRoot(Polinom polinom, double root) {
        List<Integer> coefficients = polinom.getCoefficients();
        // Initialize result based on polynomial evaluation
        double result = coefficients.get(0); // Start with the highest degree coefficient

        // Evaluate the polynomial at the given root
        for (int i = 1; i < coefficients.size(); i++) {
            result = result * root + coefficients.get(i);
        }
        return result;
    }

    private List<Double> findPossibleRoots(Polinom polinom) {
        List<Integer> p = new ArrayList<>();
        List<Integer> q = new ArrayList<>();
        List<Double> koreni = new ArrayList<>();
        List<Integer> coefficients = polinom.getCoefficients();

        int chislo = Math.abs(coefficients.get(coefficients.size() - 1));
        for (int i = 1; i <= chislo; i++) {
            if (chislo % i == 0) {
                p.add(i);
                p.add(i * (-1));
            }
        }
        int starshi = Math.abs(coefficients.get(0));
        for (int i = 1; i <= starshi; i++) {
            if (starshi % i == 0) {
                q.add(i);
                q.add(i * (-1));
            }
        }
        for (int i = 0; i < p.size(); i += 2) {
            for (int j = 0; j < q.size(); j += 2) {
                double koren = (double) p.get(i) / q.get(j);
                koreni.add(koren);
                koreni.add(koren * (-1));
            }
        }
        return koreni;
    }


    public List<Double> theRoots(Polinom polinom) {

        List<Double> possibleRoots = findPossibleRoots(polinom);
        List<Double> koreni = new ArrayList<>();
        for (int i = 0; i < possibleRoots.size(); i++) {
            double result = ifRoot(polinom, possibleRoots.get(i));
            if (result == 0) {
                koreni.add(possibleRoots.get(i));
            }
        }
        return koreni;
    }


}
