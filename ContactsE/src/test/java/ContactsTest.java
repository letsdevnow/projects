import contacts.common.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ContactsTest {
    @Test
    void testPersonClass() {
        Person person = new Person("Ivan", "Ivanov", "+79261112233");
        person.setField("gender", "m");
        Assertions.assertEquals(Gender.MALE, person.getGender());

        person.setField("name", "Sergey");
        Assertions.assertEquals("Sergey", person.getName());
    }

    @Test
    void testOrganizationClass() {
        Organization organization = new Organization();
        organization.setField("address", "Moscow, Vysokay str. 12");
        Assertions.assertEquals("Moscow, Vysokay str. 12", organization.getAddress());

        organization.setField("number", "+71003334445");
        Assertions.assertEquals("+71003334445", organization.getPhone());
    }}