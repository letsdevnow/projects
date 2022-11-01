package contacts.common;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static contacts.Main.println;

//represents person entity in contacts
public class Person extends Contact {
    private String surname;
    private LocalDate birthDate;
    private Gender gender;

    public Person() {
        super();
    }

    public Person(String name, String surname, String phone) {
        super();
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setBirthDate(String birthDate) {
        try {
            this.birthDate = LocalDate.parse(birthDate);
        } catch (DateTimeException e) {
            println("Bad birth date!");
        }
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setGender(String gender) {
        if (gender.equalsIgnoreCase("M")) {
            this.gender = Gender.MALE;
        } else if (gender.equalsIgnoreCase("F")) {
            this.gender = Gender.FEMALE;
        } else {
            println("Bad gender!");
        }
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

    @Override
    public String toStringFull() {
        return "Name: " + name + "\n" +
                "Surname: " + surname + "\n" +
                "Birth date: " + (birthDate == null ? "[no data]" : birthDate) + "\n" +
                "Gender: " + (gender == null ? "[no data]" : gender) + "\n" +
                "Number: " + (phone == null ? "[no data]" : phone) + "\n" +
                "Time created: " + created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n" +
                "Time last edit: " + (edited == null ? "[no data]" : edited.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

    }

    public String stringForSearch() {
        return name + " " + surname + " " + birthDate + " " + phone;
    }

    public String[] getFields() {
        return new String[]{"name", "surname", "birth", "gender", "number"};
    }

    public void setField(String fieldName, String value) {
        switch (fieldName) {
            case "name" -> this.setName(value);
            case "surname" -> this.setSurname(value);
            case "birth" -> this.setBirthDate(value);
            case "gender" -> this.setGender(value);
            case "number" -> this.setPhone(value);
        }
    }
}
