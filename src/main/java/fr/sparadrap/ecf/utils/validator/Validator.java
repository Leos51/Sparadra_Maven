package fr.sparadrap.ecf.utils.validator;


import fr.sparadrap.ecf.utils.RegexPatterns;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public final class Validator {
    // Patterns compilés pour de meilleures performances
    private static final Pattern EMAIL_PATTERN = Pattern.compile(RegexPatterns.EMAIL_REGEX);
    private static final Pattern NAME_PATTERN = Pattern.compile(RegexPatterns.NAME_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(RegexPatterns.PHONE_REGEX);
    private static final Pattern POSTCODE_PATTERN = Pattern.compile(RegexPatterns.POSTCODE_REGEX);
    private static final Pattern NIR_PATTERN = Pattern.compile(RegexPatterns.NIR_REGEX);
    private static final Pattern RPPS_PATTERN = Pattern.compile(RegexPatterns.RPPS_REGEX);
    private static final Pattern DATE_PATTERN = Pattern.compile(RegexPatterns.DATE_PATTERN);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private  Validator() {
    }

    public static boolean isValidName(String name){

        return name != null && !name.trim().isEmpty() && NAME_PATTERN.matcher(name).matches();
    }


    public static boolean isValidEmail(String email) {

        return email != null && EMAIL_PATTERN.matcher(email.trim().toLowerCase()).matches();
    }

    public static boolean isValidPostalCode(String postalCode) {
        return postalCode != null && POSTCODE_PATTERN.matcher(postalCode.trim().toLowerCase()).matches()  ;
    }

    public static boolean isValidPhone(String phone){

        return PHONE_PATTERN.matcher(phone.trim().toLowerCase()).matches();
    }

    public static boolean isValidNIR(String nir) {

        return nir != null && NIR_PATTERN.matcher(nir.trim().toLowerCase()).matches();
    }

    public static boolean isValidRPPS(String rpps) {
        return rpps != null && RPPS_PATTERN.matcher(rpps.trim()).matches();
    }

    public static boolean isValidDate(String date) {
        return date.matches(RegexPatterns.DATE_PATTERN);
    }


}
