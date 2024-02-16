package com.example.traficoreto.structure;

public class User {
    private int id;
    private String mail;
    private String password;
    private String name;
    private String city;
    private String country;
    private Integer age;
    private String street;

    public User(int id, String mail, String password, String name, String city, String country, Integer age, String street) {
        this.id = id;
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.city = city;
        this.country = country;
        this.age = age;
        this.street = street;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
