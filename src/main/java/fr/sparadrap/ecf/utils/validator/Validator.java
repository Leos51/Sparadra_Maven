package fr.sparadrah.ecf.utils.validator;


import fr.sparadrah.ecf.utils.RegexPatterns;

public class Validator {

    public static boolean isValidName(String name){
        return name.matches(RegexPatterns.NAME_REGEX);
    }


    public static boolean isValidEmail(String email) {
        return email.matches(RegexPatterns.EMAIL_REGEX);
    }

    public static boolean isValidPostalCode(String postalCode) {
        return postalCode.matches(RegexPatterns.POSTCODE_REGEX)  ;
    }

    public static boolean isValidPhone(String phone){
        return phone.matches(RegexPatterns.PHONE_REGEX);
    }

    public static boolean isValidNIR(String nir) {
        return nir.matches(RegexPatterns.NIR_REGEX);
    }

    public static boolean isValidDate(String date) {
        return date.matches(RegexPatterns.DATE_PATTERN);
    }
}
