package fr.sparadrap.ecf;

import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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