package com.dan.spring.boot.nexus.pojo;

public class Worker {
    private String name;
    private String skill;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", skill='" + skill + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
