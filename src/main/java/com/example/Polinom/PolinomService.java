package com.example.Polinom;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PolinomService {


    public String formatRoot(double chislo) {
        StringBuilder result = new StringBuilder();
        double absChislo = Math.abs(chislo);


        if (chislo % 1 == 0) {
          result.append((int)chislo);
        } else if (isPerfectSquare(absChislo)) { // Ако може да се представи като корен
            if (chislo < 0) {
                result.append("-√").append((int) absChislo);
            } else {
                result.append("√").append((int) absChislo);
            }
        } else { // Проверяваме дали може да се представи като приближена стойност на корен
            double tolerance = 1.0E-6; // Точност при проверка
            for (int i = 1; i < 1000; i++) {
                if (Math.abs(chislo - Math.sqrt(i)) < tolerance || Math.abs(chislo + Math.sqrt(i)) < tolerance) {
                    if (chislo < 0) {
                        result.append("-√").append(i);
                    } else {
                        result.append("√").append(i);
                    }
                    return result.toString().trim();
                }
            }
            result.append(simplifyFraction(chislo)); // Ако не може, опитваме да го представим като дробно
        }
        return result.toString().trim();
    }

    // Метод за представяне на числото в дробен вид
    public String simplifyFraction(double number) {
        if (number == (int) number) {
            return String.valueOf((int) number); // Връщаме като цяло число
        }
        double tolerance = 1.0E-6; // Точност при проверка
        for (int denominator = 1; denominator <= 1000; denominator++) {
            double numerator = number * denominator;
            if (Math.abs(numerator - Math.round(numerator)) < tolerance) {
                return Math.round(numerator) + "/" + denominator;
            }
        }
        return String.valueOf(number); // Връщаме оригиналното число, ако не е дроб
    }

    // Метод за проверка на перфектен квадрат
    private boolean isPerfectSquare(double number) {
        double sqrt = Math.sqrt(number);
        return sqrt == Math.floor(sqrt);
    }

    // Метод за изчисляване на корените с дискриминантата
    public List<String> uravnenieSDiskriminanta(List<Double> polinomCoef) {
        List<String> koreni = new ArrayList<>();
        double a = polinomCoef.get(0);
        double b = polinomCoef.get(1);
        double c = polinomCoef.get(2);
        // Изчисляване на дискриминантата
        double D = b * b - 4 * a * c;
        if (D > 0) {
            // Дискриминантата е положителна: два реални корена (включително ирационални)
            double sqrtD = Math.sqrt(D);
            double x1 = (-b + sqrtD) / (2 * a);
            double x2 = (-b - sqrtD) / (2 * a);
            koreni.add(formatRoot(x1));
            koreni.add(formatRoot(x2));
        } else if (D == 0) {
            double x = -b / (2 * a);
            koreni.add(formatRoot(x));
            koreni.add(formatRoot(x));
        } else {
            // Дискриминантата е отрицателна: няма реални корени, има комплексни корени
            koreni.add("unreal"); // добавяме само реалната част
        }
        return koreni;
    }


    // Метод за проверка дали число е корен на полином
    public List<Double> ifRoot(List<Double> polinomCoef, double root) {
        List<Double> coefficients = polinomCoef;
        List<Double> newPolinom = new ArrayList<>();
        double result = coefficients.get(0); // Започваме с най-високата степен
        newPolinom.add(result);

        for (int i = 1; i < coefficients.size(); i++) {
            result = result * root + coefficients.get(i);
            newPolinom.add(result);
        }
        return newPolinom;
    }

    // Метод за намиране на възможни корени
    private List<Double> findPossibleRoots(List<Double> polinomCoef) {
        List<Integer> p = new ArrayList<>();
        List<Integer> q = new ArrayList<>();
        List<Double> koreni = new ArrayList<>();
        List<Double> coefficients = polinomCoef;
        Double chislo = Math.abs(coefficients.get(coefficients.size() - 1));
        for (int i = 1; i <= chislo; i++) {
            if (chislo % i == 0) {
                p.add(i);
                p.add(i * (-1));
            }
        }
        Double starshi = Math.abs(coefficients.get(0));
        for (int i = 1; i <= starshi; i++) {
            if (starshi % i == 0) {
                q.add(i);
                q.add(i * (-1));
            }
        }
        for (int i = 0; i < p.size(); i += 2) {
            for (int j = 0; j < q.size(); j += 2) {
                double koren = (double) p.get(i) / q.get(j);
                if (!koreni.contains(koren)) {
                    koreni.add(koren);
                    koreni.add(koren * (-1));
                }
            }
        }
        return koreni;
    }

    public List<String> theRoots(List<Double> polinomCoef) {
        List<Double> possibleRoots = findPossibleRoots(polinomCoef);
        List<String> koreni = new ArrayList<>();

        if (polinomCoef.size() == 3) {
            return uravnenieSDiskriminanta(polinomCoef);
        }
        for (int i = 0; i < possibleRoots.size(); i++) {
            List<Double> updatedCoef = ifRoot(polinomCoef, possibleRoots.get(i));
            int size = updatedCoef.size();
            double result = updatedCoef.get(size - 1);
            if (result == 0) {
                koreni.add(simplifyFraction(possibleRoots.get(i)));

                polinomCoef = updatedCoef.subList(0, size - 1);
                i = -1;
                if (polinomCoef.size() == 3) {

                    koreni.addAll(uravnenieSDiskriminanta(polinomCoef));
                    break;
                }
            }
        }
        return koreni;
    }

}
