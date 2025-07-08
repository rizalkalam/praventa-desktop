package com.example.praventa.repository;

import com.example.praventa.model.users.BodyMetrics;
import com.example.praventa.model.users.PersonalData;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String FILE_PATH = "D:/Kuliah/Project/praventa/data/users.xml";

    // 🔍 Login
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

    // 📄 Load semua user dari XML
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

    // 💾 Simpan ke XML
    public static void saveUsers(List<User> users) {
        try {
            JAXBContext context = JAXBContext.newInstance(
                    UserListWrapper.class,
                    User.class,
                    PersonalData.class,
                    BodyMetrics.class
            );

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            UserListWrapper wrapper = new UserListWrapper();
            wrapper.setUsers(users);

            File file = new File(FILE_PATH); // atau path absolut seperti "data/user.xml"
            File parentDir = file.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); // hanya buat folder jika memang ada parent path
            }

            marshaller.marshal(wrapper, file);

            System.out.println("✅ Data berhasil disimpan ke user.xml");

        } catch (Exception e) {
            System.err.println("❌ Gagal menyimpan data ke XML:");
            e.printStackTrace();
        }
    }

    // ➕ Register user baru
    public static boolean register(User newUser) {
        List<User> users = loadUsers();

        // Cek duplikat username/email
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(newUser.getUsername()) ||
                    user.getEmail().equalsIgnoreCase(newUser.getEmail())) {
                return false;
            }
        }

        // Set ID otomatis
        int maxId = users.stream().mapToInt(User::getId).max().orElse(0);
        newUser.setId(maxId + 1);

        // ⬇️ Inisialisasi data personal dengan default "__" (jika belum ada)
        if (newUser.getPersonalData() == null) {
            PersonalData pd = new PersonalData();
            pd.setGender("__");
            pd.setBirthDate("__");

            BodyMetrics metrics = new BodyMetrics();
            metrics.setWeight(0);
            metrics.setHeight(0);
            pd.setBodyMetrics(metrics);

            newUser.setPersonalData(pd);
        }

        users.add(newUser);
        saveUsers(users);
        return true;
    }

    // 🔁 Update user yang sedang login
    public static boolean updateUser(User updatedUser) {
        List<User> users = loadUsers();
        boolean updated = false;

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                updated = true;
                break;
            }
        }

        if (updated) {
            saveUsers(users);
        }

        return updated;
    }
}
