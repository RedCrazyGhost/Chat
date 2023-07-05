package org.redcrazyghost.Server.OBJ;

/**
 * User
 *
 * @author wenxingzhan
 * @date 2021/11/29 12:00
 **/
public class User {
    private String username;
    private String password;
    private String emai;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", emai='" + emai + '\'' +
                '}';
    }

    public User(String username, String password, String emai) {
        this.username = username;
        this.password = password;
        this.emai = emai;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmai() {
        return emai;
    }

    public void setEmai(String emai) {
        this.emai = emai;
    }
}
