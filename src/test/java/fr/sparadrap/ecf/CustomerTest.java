package fr.sparadrap.ecf;

import fr.sparadrap.ecf.model.person.MutualInsurance;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.utils.DateFormat;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static fr.sparadrap.ecf.utils.UserInput.capitalize;
import static org.junit.jupiter.api.Assertions.*;
class CustomerTest {
    Customer customer;
    MutualInsurance macif;

    @BeforeEach
    void setUp() throws SaisieException {
        macif = new MutualInsurance("Bourso", "Marne", 0.3, "address","51000","Ville", "0612324565","email@email.com");

        customer = new Customer("Recto", "Verso", "Adresse 1", "51000", "Chalons en Champagne", "03 26 68 03 00", "rectoverso@gmail.com", "1825194528231", "28/02/2010", macif, null);
    }

    @AfterEach
    void tearDown() {
        customer = null;
    }

    @Test
    public void testConstructeurArgsValide() throws Exception {
        // Assert
        assertEquals("Verso", customer.getFirstName());
        assertEquals("Recto", customer.getLastName());
        assertEquals("Adresse 1", customer.getAddress());
        assertEquals("51000", customer.getPostCode());
        assertEquals("Chalons en Champagne", customer.getCity());
        assertEquals("rectoverso@gmail.com", customer.getEmail());
        assertEquals("03 26 68 03 00", customer.getPhone());
        assertEquals("1825194528231", customer.getNir());
        assertEquals("28/02/2010", DateFormat.formatDate(customer.getBirthDate(), "dd/MM/yyyy"));
        assertEquals(macif, customer.getMutualInsurance());
        assertEquals(null, customer.getDoctor());

    }

    @Test
    public void testConstructeurArgsinvalide() throws Exception {

        assertNotEquals("Clark", customer.getFirstName());
        assertNotEquals("Kent", customer.getLastName());
    }

    @Test
    void getFirstName() {
        assertEquals("Verso", customer.getFirstName());
    }

    @ParameterizedTest(name = "{0} : le setter fonctionne correctement")
    @ValueSource(strings = {"Verso", "verso", "VERSO ", " verSo", "Heat",} )
    void setFirstNameTest_Success(String name) {
        customer.setFirstName(name);
        assertEquals(capitalize(name.trim()), customer.getFirstName());
    }

    @Test
    void getLastName() {
    }

    @ParameterizedTest(name = "{0} : le setter fonctionne correctement")
    @ValueSource(strings = {"Verso", "verso", "VERSO ", " verSo", "Heat",} )
    void setLastNameTest_Success(String name) {
        customer.setLastName(name);
        assertEquals(capitalize(name.trim()), customer.getLastName());
    }

    @Test
    void getAddress() {
        assertEquals("Adresse 1", customer.getAddress());
    }


    @ParameterizedTest(name = "{0} : le setter fonctionne correctement")
    @ValueSource(strings = {"30 av. des Hauts", "rue des fleurs", "av.de Paris"} )
    void setAdressTest_Success(String address) {
        customer.setAddress(address);
        assertEquals(address.trim(), customer.getAddress());
    }

    @Test
    void getPostCode() {
        assertEquals("51000", customer.getPostCode());
    }

    @ParameterizedTest(name = "{0} : le setter fonctionne correctement")
    @ValueSource(strings = {"75000", "01520", "51520"} )
    void setPostCodeTest_Success(String cp) throws SaisieException {
        customer.setPostCode(cp);
        assertEquals(cp.trim(), customer.getPostCode());
    }

    @Test
    void getCity() {
        assertEquals("Chalons en Champagne", customer.getCity());
    }

    @Test
    void setCity() {
        customer.setCity("other");
        assertEquals("other", customer.getCity());
    }

    @Test
    void getPhone() {
        assertEquals("03 26 68 03 00", customer.getPhone());
    }

    @ParameterizedTest(name = "{0} : le setter fonctionne correctement")
    @ValueSource(strings = {"0123456789", "01 23 45 67 89", "01.23.45.67.89", "+33-1.23.45.67.89", "+33 - 123 456 789"} )
    void testSetPhone_Success(String phone) throws SaisieException {
        customer.setPhone(phone);
        assertEquals(phone, customer.getPhone());
    }

    @ParameterizedTest(name = "{0} : le setter leve une exception correctement")
    @ValueSource(strings = {"Verso", "reD51@"} )
    void testSetEmail_Fail(String email) {
        assertThrows(Exception.class, () -> customer.setEmail(email));

    }

    @Test
    void getEmail() {
        assertEquals("rectoverso@gmail.com", customer.getEmail());
    }

    @ParameterizedTest(name = "{0} : le setter fonctionne correctement")
    @ValueSource(strings = {"leo@mail.com", "link103@live.fr", "reD51@orange.fr"} )
    void testSetEmail_Success(String email) throws SaisieException {
        customer.setEmail(email);
        assertEquals(email.trim().toLowerCase(), customer.getEmail());
    }


    @Test
    void testGetNIR() {
        assertEquals("1825194528231", customer.getNir());
    }


    @ParameterizedTest(name = "{0} : le setter fonctionne correctement")
    @ValueSource(strings = {"1850751234567", "2920339876543", "1901314567890"} )
    void testSetNir_Success(String nir) throws SaisieException {
        customer.setNir(nir);
        assertEquals(nir, customer.getNir());

    }

    @Test
    void testGetBirthDate() {assertEquals(DateFormat.parseDateFromString("28/02/2010"), customer.getBirthDate());
    }


    @ParameterizedTest(name = "{0} : le setter fonctionne correctement")
    @ValueSource(strings = {"18/12/1982", "24/10/2004", "02/02/1990"} )
    void testSetBirthDate_Success(String date) throws SaisieException {
        customer.setBirthDateFromString(date);
        assertEquals(DateFormat.parseDateFromString(date), customer.getBirthDate());
        System.out.println(date + " " + customer.getBirthDate());
    }

    @Test
    void testGetMutualInsurance() {
        assertEquals("Bourso", customer.getMutualInsurance().getCompagnyName());

    }

    @Test
    void setInsuranceCompagny() throws SaisieException {
        MutualInsurance a = new MutualInsurance("m1", "ter", 0.05, "adress", "75000", "aze", "0789654123", "mail@mail.fr");
        customer.setMutualInsurance(a);
        assertEquals(a.getCompagnyName(), customer.getMutualInsurance().getCompagnyName());
    }

}