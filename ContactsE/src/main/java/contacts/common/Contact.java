package contacts.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static contacts.Main.println;

//abstract class represents entity for contacts database
public abstract class Contact implements Serializable {
    protected String name;
    protected String phone;
    protected LocalDateTime created;
    protected LocalDateTime edited;

    public Contact() {
        created = LocalDateTime.now();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhone(String phone) {
        if (phone.equals("")) {
            this.phone = "[no number]";
        } else if (isPhoneValid(phone)) {
            this.phone = phone;
        } else {
            println("Wrong number format!");
            this.phone = "[no number]";
        }
    }

    public String getPhone() {
        return phone;
    }

    public boolean hasNumber() {
        return phone != null;
    }

    public boolean isPhoneValid(String phone) {
        String regex1 = "\\+?\\s?" + "\\(?\\w+\\)?" + "[\\w\\s-]*";
        String regex2 = "\\+?" + "[\\w\\s-]+" + "\\(?\\w{2,}\\)?" + "[\\w\\s-]*";
        Pattern pattern1 = Pattern.compile(regex1, Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile(regex2, Pattern.CASE_INSENSITIVE);

        Pattern pattern3 = Pattern.compile("\\w+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern3.matcher(phone);
        boolean noOneNumberGroup = true;
        int groupsNumber = 0;
        while (matcher.find()) {
            groupsNumber++;
            if (matcher.end() - matcher.start() < 2 && groupsNumber > 1) {
                noOneNumberGroup = false;
            }
        }

        return (pattern1.matcher(phone).matches() || pattern2.matcher(phone).matches()) && noOneNumberGroup;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setEdited() {
        this.edited = LocalDateTime.now();
    }

    public LocalDateTime getEdited() {
        return edited;
    }

    public abstract String toStringFull();

    public abstract String stringForSearch();

    public abstract String[] getFields();

    public abstract void setField(String fieldName, String value);

}
