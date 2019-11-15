package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(name = "customer_auth",schema ="public")
@NamedQueries({
        @NamedQuery(name = "getTokenViaID",query = "select u from CustomerAuthTokenEntity u where u.id = :id"),
        @NamedQuery(name = "getTokenViaUuid",query = "select u from CustomerAuthTokenEntity u where u.uuid = :uuid"),
        @NamedQuery(name ="getToken",query = "select u from CustomerAuthTokenEntity u where u.accessToken = :token")

})
public class CustomerAuthTokenEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =GenerationType.IDENTITY )
    private Integer id;

    @Column(name = "uuid")
    private String uuid;


    @Column(name ="customer_id")
//    @NotNull
    private Integer customerId;

    @Column(name ="access_token")
//    @NotNull
    private String accessToken;

    @Column(name ="login_at")
    private ZonedDateTime loginAt;

    @Column(name = "logout_at")
    private ZonedDateTime logoutAt;

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(ZonedDateTime loginAt) {
        this.loginAt = loginAt;
    }

    public ZonedDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(ZonedDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }
}
