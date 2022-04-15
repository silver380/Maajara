package ir.blackswan.travelapp.Data;

import java.util.List;

public class User {
    private boolean isBanned , isValidatedByAdmin;
    private String name , lastName , email;
    private boolean isTourGuide;
    private Path profilePicturePath;
    private int ticket;
    private long birthDay;
    private boolean sex;
    private String bio;
    private Path policeClearanceDocPath;
    private List<String> languages;
    private String phoneNumber , telegramUsername , whatsappNumber;
    private List<Path> relatedDocsPaths;
    private double rate;

    public User(String name, String lastName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
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

    public void setBirthDay(long birthDay) {
        this.birthDay = birthDay;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPoliceClearanceDocPath(Path policeClearanceDocPath) {
        this.policeClearanceDocPath = policeClearanceDocPath;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
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

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
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

    public long getBirthDay() {
        return birthDay;
    }

    public boolean isSex() {
        return sex;
    }

    public String getBio() {
        return bio;
    }

    public Path getPoliceClearanceDocPath() {
        return policeClearanceDocPath;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public double getRate() {
        return rate;
    }

    public String getNameAndLastname(){
        return name + " " + lastName;
    }
}
