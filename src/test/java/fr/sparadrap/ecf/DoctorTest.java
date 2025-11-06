package fr.sparadrah.ecf.test;

import fr.sparadrah.ecf.model.person.Doctor;
import fr.sparadrah.ecf.utils.exception.SaisieException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {
    Doctor doctor;

    @BeforeEach
    void setUp() throws SaisieException {
        doctor = new Doctor("Doc", "Abi","10 Parru", "52000", "Parus","0123456997","ano@daim.fr","12345678590");
    }

    @AfterEach
    void tearDown() {
        doctor = null;
    }

    @Test
    void getLicenseNumber() {
        assertEquals("12345678590", doctor.getRpps());
    }

    @Test
    void setLicenseNumber() throws SaisieException {
        doctor.setRpps("12345678698");
        assertEquals("12345678698", doctor.getRpps());
    }

}