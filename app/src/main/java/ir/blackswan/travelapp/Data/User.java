package ir.blackswan.travelapp.Data;

import static ir.blackswan.travelapp.Utils.Utils.convertStringToDate;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Objects;

import ir.blackswan.travelapp.Utils.MyPersianCalender;

public class User implements Serializable {
    /*
     "id": 1,
    "email": "a@b.com",
    "first_name": "1",
    "last_name": "1",
    "date_of_birth": "1900-10-10",
    "gender": "Female",
    "biography": "a",
    "languages": "a",
    "phone_number": "123",
    "telegram_id": "",
    "whatsapp_id": ""

     /*
    @Field("date-of-birth") String date,
                               @Field("gender") String gender,
                               @Field("biography") String biography,
                               @Field("languages") String languages,
                               @Field("phone_number") String phoneNumber,
                               @Field("telegram_id") String telegramId,
                               @Field("whatsapp_id") String whatsappId
     */

    private int user_id = -1;
    private boolean isValidatedByAdmin;
    @Expose
    private String first_name, last_name, email, ssn;
    @Expose
    private String date_of_birth;
    @Expose
    private String gender;
    @Expose
    private String biography;
    @Expose
    private String languages;
    @Expose
    private String phone_number, telegram_id, whatsapp_id;
    private boolean requested_for_upgrade;
    private String upgrade_note;

    private boolean is_tour_leader;
    private String picture;
    private int number_of_tickets;
    private String certificate;
    private double avg_rate;

    public User(String first_name, String last_name, String email, String ssn, String date_of_birth,
                String gender, String biography, String languages, String phone_number,
                String telegram_id, String whatsapp_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.ssn = ssn;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.biography = biography;
        this.languages = new Gson().toJson(languages.split("\\n"));
        this.phone_number = phone_number;
        this.telegram_id = telegram_id;
        this.whatsapp_id = whatsapp_id;
    }

    public User(String first_name, String last_name, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    public String getSsn() {
        return ssn;
    }

    public String[] getLanguagesArray() {
        try {
            if (languages == null)
                return null;
            return new Gson().fromJson(languages, String[].class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public String getLanguages() {
        return languages;
    }

    public boolean isRequested_for_upgrade() {
        return requested_for_upgrade;
    }

    public String getUpgrade_note() {
        return upgrade_note;
    }

    public String getPersianGender() {
        return gender.equals("Male") ? "مرد" : "زن";
    }

    public String getLanguagesWithSeparator(String separator) {
        String[] languages = getLanguagesArray();
        if (languages == null)
            return "";
        StringBuilder lan = new StringBuilder();
        for (int i = 0; i < languages.length; i++) {
            lan.append(languages[i]);
            if (i < languages.length - 1)
                lan.append(separator);
        }
        return lan.toString();
    }

    public String getPicture() {
        return picture;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setNumber_of_tickets(int number_of_tickets) {
        this.number_of_tickets = number_of_tickets;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setTelegram_id(String telegram_id) {
        this.telegram_id = telegram_id;
    }

    public void setWhatsapp_id(String whatsapp_id) {
        this.whatsapp_id = whatsapp_id;
    }

    public void setAvg_rate(double avg_rate) {
        this.avg_rate = avg_rate;
    }

    public boolean isValidatedByAdmin() {
        return isValidatedByAdmin;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public boolean is_tour_leader() {
        return is_tour_leader;
    }

    public int getNumber_of_tickets() {
        return number_of_tickets;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public String getBiography() {
        return biography;
    }

    public MyPersianCalender getPersianBirthDate() {
        if (date_of_birth == null)
            return null;
        try {
            MyPersianCalender persianDate = new MyPersianCalender();
            persianDate.setDate(convertStringToDate(date_of_birth));
            return persianDate;
        } catch (ParseException e) {
            return null;
        }
    }

    public String getPhone_number() {
        return getNullifEmpty(phone_number);
    }

    public String getTelegram_id() {
        return getNullifEmpty(telegram_id);
    }

    public String getNullifEmpty(String str) {
        if (str == null || str.equals(""))
            return null;
        return str;
    }

    public String getWhatsapp_id() {
        return getNullifEmpty(whatsapp_id);
    }

    public double getAvg_rate() {
        return avg_rate;
    }

    public String getNameAndLastname() {
        return first_name + " " + last_name;
    }

    public String getFullNameWithPrefix() {
        if (gender == null)
            return getNameAndLastname();
        return gender.equals("Male") ? "آقای " + getNameAndLastname() : "خانم " + getNameAndLastname();
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", isValidatedByAdmin=" + isValidatedByAdmin +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", gender='" + gender + '\'' +
                ", biography='" + biography + '\'' +
                ", languages='" + languages + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", telegram_id='" + telegram_id + '\'' +
                ", whatsapp_id='" + whatsapp_id + '\'' +
                ", is_tour_leader=" + is_tour_leader +
                ", picture='" + picture + '\'' +
                ", ticket=" + number_of_tickets +
                ", certificate='" + certificate + '\'' +
                ", rate=" + avg_rate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return user_id == user.user_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id);
    }
}
