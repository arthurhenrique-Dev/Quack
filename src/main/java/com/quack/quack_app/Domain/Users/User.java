package com.quack.quack_app.Domain.Users;

import com.quack.quack_app.Domain.ValueObjects.*;

import java.util.ArrayList;
import java.util.UUID;

public class User extends BaseUser{

    private Username username;
    private String photoUrl;
    private Description description;
    private FavoriteGames favoriteGames;
    private Connections friends;
    private Connections followers;

    public User(UUID id, Password password, Email email, Role role, Status status, TokenUpdater passwordUpdater, TokenUpdater emailUpdater, TwoFA twoFA, Username username, String photoUrl, Description description, FavoriteGames favoriteGames, Connections friends, Connections followers) {
        super(id, password, email, role, status, passwordUpdater, emailUpdater, twoFA);
        this.username = username;
        this.photoUrl = photoUrl;
        this.description = description;
        this.favoriteGames = favoriteGames;
        this.friends = friends;
        this.followers = followers;
    }

    public User(Username username, Password password, Email email, Description description, String photoUrl) {
        super(password, email);
        this.username = username;
        this.description = description;
        this.photoUrl = photoUrl;
        this.role = Role.USER;
        this.favoriteGames = new FavoriteGames(new ArrayList<>());
        this.friends = new Connections(new ArrayList<>());
        this.followers = new Connections(new ArrayList<>());
    }

    public void changeUsername(Username username) {
        this.username = username;
    }
    public void changeDescription(Description description) {
        this.description = description;
    }
    public void changePhoto(String newPhotoUrl){
        this.photoUrl = newPhotoUrl;
    }
    public void follow(UUID uuid){
        if (this.followers.connections().contains(uuid)){
            this.friends.addConnection(uuid);
        }
        this.followers.addConnection(uuid);
    }
    public void otherPartAddFriend(UUID uuid){
        this.friends.addConnection(uuid);
    }
    public void otherPartRemoveFriend(UUID uuid){
        this.friends.removeConnection(uuid);
    }
    public void unfollow(UUID uuid){
        if (this.followers.connections().contains(uuid)){
            this.friends.removeConnection(uuid);
        }
        this.followers.removeConnection(uuid);
    }

    public Connections getFollowers() {
        return followers;
    }

    public Connections getFriends() {
        return friends;
    }

    public Username getUsername() {
        return username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public Description getDescription() {
        return description;
    }

    public FavoriteGames getFavoriteGames() {
        return favoriteGames;
    }
}
