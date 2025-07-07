package com.example.praventa.repository;

import com.example.praventa.model.User;
import com.example.praventa.model.UserListWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String FILE_PATH = "D:/Kuliah/Project/praventa/data/users.xml";

    public static User findByUsernameAndPassword(String username, String password) {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return null;

            JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            UserListWrapper wrapper = (UserListWrapper) unmarshaller.unmarshal(file);

            for (User user : wrapper.getUsers()) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<User> loadUsers() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return new ArrayList<>();

            JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            UserListWrapper wrapper = (UserListWrapper) unmarshaller.unmarshal(file);
            return wrapper.getUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveUsers(List<User> users) {
        try {
            JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            UserListWrapper wrapper = new UserListWrapper();
            wrapper.setUsers(users);

            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // Buat folder kalau belum ada
            marshaller.marshal(wrapper, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser(User newUser) {
        List<User> users = loadUsers();
        users.add(newUser);
        saveUsers(users);
    }
}
