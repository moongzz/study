package com.example.demo.di.src;

public class UserService {

    public void createUser(String name) {
        System.out.println(name + "님을 생성했습니다.");
    }

    public void deleteUser(String name) {
        System.out.println(name + "님을 삭제했습니다.");
    }
}
