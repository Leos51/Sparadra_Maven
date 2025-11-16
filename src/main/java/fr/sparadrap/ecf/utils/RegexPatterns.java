package fr.sparadrap.ecf.utils;


import java.util.regex.Pattern;

public class RegexPatterns {
    public static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";
    public static final String NAME_REGEX = "^[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+$";

    public static final String PHONE_REGEX = "^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2})$";

    public static final String POSTCODE_REGEX = "^(?:0[1-9]|[1-8]\\d|9[0-8])\\d{3}$";

    public static final String NIR_REGEX = "^[12][0-9]{12}[0-9]{2}$";

    public static final String RPPS_REGEX = "^\\d{11}$";
    public static final String DATE_PATTERN = "^(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[0-2])\\/([0-9]{4})$";
}
