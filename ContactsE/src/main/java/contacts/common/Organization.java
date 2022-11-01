package contacts.common;

import java.time.format.DateTimeFormatter;

//represents organization entity in contacts
public class Organization extends Contact {
    private String address;

    public Organization() {
        super();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toStringFull() {
        return "Organization name: " + name + "\n" +
                "Address: " + address + "\n" +
                "Number: " + (phone == null ? "[no data]" : phone) + "\n" +
                "Time created: " + created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n" +
                "Time last edit: " + (edited == null ? "[no data]" : edited.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

    }

    public String stringForSearch() {
        return name + " " + address + " " + phone;
    }

    public String[] getFields() {
        return new String[]{"name", "address", "number"};
    }

    public void setField(String fieldName, String value) {
        switch (fieldName) {
            case "name" -> this.setName(value);
            case "address" -> this.setAddress(value);
            case "number" -> this.setPhone(value);
        }
    }

}
