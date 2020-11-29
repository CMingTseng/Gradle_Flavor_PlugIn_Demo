package com.github.ximik3.gradle.cloud.api.data.request.gd;

import com.google.gson.annotations.SerializedName;

//TODO  Ref : https://developers.google.com/identity/sign-in/android/backend-auth
//      Ref : https://ncona.com/2015/02/consuming-a-google-id-token-from-a-server/
public class AuthBody {
    /**
     * These six fields are included in all Google ID Tokens. iss : https://accounts.google.com sub
     * : 110169484474386276334 azp : 1008719970978-hb24n2dstb40o45d4feuo2ukqmcc6381.apps.googleusercontent.com
     * aud : 1008719970978-hb24n2dstb40o45d4feuo2ukqmcc6381.apps.googleusercontent.com iat :
     * 1433978353 exp : 1433981953
     */

    @SerializedName("iss")
    private String iss;
    @SerializedName("sub")
    private String sub;
    @SerializedName("azp")
    private String azp;
    @SerializedName("aud")
    private String aud;
    @SerializedName("iat")
    private String iat;
    @SerializedName("exp")
    private String exp;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAzp() {
        return azp;
    }

    public void setAzp(String azp) {
        this.azp = azp;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getIat() {
        return iat;
    }

    public void setIat(String iat) {
        this.iat = iat;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    /**
     * These seven fields are only included when the user has granted the "profile" and "email"
     * OAuth scopes to the application. email : testuser@gmail.com email_verified : true name : Test
     * User picture : https://lh4.googleusercontent.com/-kYgzyAWpZzJ/ABCDEFGHI/AAAJKLMNOP/tIXL9Ir44LE/s99-c/photo.jpg
     * given_name : Test family_name : User locale : en
     */

    @SerializedName("email")
    private String email;
    @SerializedName("email_verified")
    private String emailVerified;
    @SerializedName("name")
    private String name;
    @SerializedName("picture")
    private String picture;
    @SerializedName("given_name")
    private String givenName;
    @SerializedName("family_name")
    private String familyName;
    @SerializedName("locale")
    private String locale;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
