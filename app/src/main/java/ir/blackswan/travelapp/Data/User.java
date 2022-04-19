package ir.blackswan.travelapp.Data;

import android.content.Context;

import java.util.List;

import ir.blackswan.travelapp.Utils.SharedPrefManager;

public class User {
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
     */
    private String token;
    private int id;
    private boolean isBanned, isValidatedByAdmin;
    private String first_name, last_name, email;
    private boolean isTourGuide;
    private Path profilePicturePath;
    private int ticket;
    private String date_of_birth;
    private String gender;
    private String biography;
    private Path policeClearanceDocPath;
    private String languages;
    private String phone_number, telegram_id, whatsapp_id;
    private List<Path> relatedDocsPaths;
    private double rate;

    public User(String name, String lastName, String email) {
        this.first_name = name;
        this.last_name = lastName;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public User(Context context, String token) {
        setToken(context, token);
    }

    public void setToken(Context context, String token) {
        this.token = token;
        new SharedPrefManager(context).putString(SharedPrefManager.USER_TOKEN, token);
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public void setValidatedByAdmin(boolean validatedByAdmin) {
        isValidatedByAdmin = validatedByAdmin;
    }

    public void setTourGuide(boolean tourGuide) {
        isTourGuide = tourGuide;
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

    public void setRelatedDocsPaths(List<Path> relatedDocsPaths) {
        this.relatedDocsPaths = relatedDocsPaths;
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

    public int getId() {
        return id;
    }

    public String getName() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isTourGuide() {
        return isTourGuide;
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
        return phone_number;
    }

    public String getTelegram_id() {
        return telegram_id;
    }

    public String getWhatsapp_id() {
        return whatsapp_id;
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
                "token='" + token + '\'' +
                ", name='" + first_name + '\'' +
                ", lastName='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", isTourGuide=" + isTourGuide +
                ", birthDay=" + date_of_birth +
                ", sex=" + gender +
                ", bio='" + biography + '\'' +
                ", policeClearanceDocPath=" + policeClearanceDocPath +
                ", languages=" + languages +
                ", phoneNumber='" + phone_number + '\'' +
                ", telegramUsername='" + telegram_id + '\'' +
                ", whatsappNumber='" + whatsapp_id + '\'' +
                ", relatedDocsPaths=" + relatedDocsPaths +
                ", rate=" + rate +
                '}';
    }
}