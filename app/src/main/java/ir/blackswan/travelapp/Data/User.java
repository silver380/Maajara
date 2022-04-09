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
}
