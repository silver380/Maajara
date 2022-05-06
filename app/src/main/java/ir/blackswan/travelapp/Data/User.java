package ir.blackswan.travelapp.Data;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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
    private boolean isBanned, isValidatedByAdmin;
    @Expose
    private String first_name, last_name, email;
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

    private boolean is_tour_leader;
    private Path profilePicturePath;
    private int ticket;
    private Path policeClearanceDocPath;
    private double rate;

    public User(String first_name, String last_name, String email, String date_of_birth,
                String gender, String biography, String languages,
                String phone_number, String telegram_id, String whatsapp_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.biography = biography;
        this.languages = languages;
        this.phone_number = phone_number;
        this.telegram_id = telegram_id;
        this.whatsapp_id = whatsapp_id;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public void setValidatedByAdmin(boolean validatedByAdmin) {
        isValidatedByAdmin = validatedByAdmin;
    }

    public void setIs_tour_leader(boolean is_tour_leader) {
        this.is_tour_leader = is_tour_leader;
    }

    public void setProfilePicturePath(Path profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setPoliceClearanceDocPath(Path policeClearanceDocPath) {
        this.policeClearanceDocPath = policeClearanceDocPath;
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

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isBanned() {
        return isBanned;
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

    public Path getProfilePicturePath() {
        return profilePicturePath;
    }

    public int getTicket() {
        return ticket;
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

    public Path getPoliceClearanceDocPath() {
        return policeClearanceDocPath;
    }


    public String getPhone_number() {
        return getNullifEmpty(phone_number);
    }

    public String getTelegram_id() {
        return getNullifEmpty(telegram_id);
    }

    public String getNullifEmpty(String str){
        if (str == null || str.equals(""))
            return null;
        return str;
    }
    public String getWhatsapp_id() {
        return getNullifEmpty(whatsapp_id);
    }

    public double getRate() {
        return rate;
    }

    public String getNameAndLastname() {
        return first_name + " " + last_name;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", isBanned=" + isBanned +
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
                ", profilePicturePath=" + profilePicturePath +
                ", ticket=" + ticket +
                ", policeClearanceDocPath=" + policeClearanceDocPath +
                ", rate=" + rate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return user_id == user.user_id ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id);
    }
}
