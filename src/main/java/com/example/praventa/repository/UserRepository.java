package com.example.praventa.repository;

import com.example.praventa.model.questionnaire.QuestionAnswer;
import com.example.praventa.model.questionnaire.QuestionnaireResult;
import com.example.praventa.model.users.*;
import com.example.praventa.utils.XmlUtils;
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

            JAXBContext context = JAXBContext.newInstance(
                    UserListWrapper.class,
                    User.class,
                    QuestionnaireResult.class,
                    QuestionAnswer.class,
                    PersonalData.class,
                    LifestyleData.class,
                    PersonalDisease.class,
                    FamilyDiseaseHistory.class
            );

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
        try {
            File file = new File(FILE_PATH); // lokasi users.xml
            UserListWrapper wrapper = XmlUtils.loadUsers(file);
            List<User> users = wrapper.getUsers();

            for (int i = 0; i < users.size(); i++) {
                User existingUser = users.get(i);
                if (existingUser.getId() == updatedUser.getId()) {

                    // Hanya update field yang boleh diubah
                    if (updatedUser.getUsername() != null)
                        existingUser.setUsername(updatedUser.getUsername());

                    if (updatedUser.getEmail() != null)
                        existingUser.setEmail(updatedUser.getEmail());

                    if (updatedUser.getPhoneNumber() != null)
                        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

                    if (updatedUser.getProfilePicture() != null)
                        existingUser.setProfilePicture(updatedUser.getProfilePicture());

                    // Simpan kembali
                    users.set(i, existingUser);
                    wrapper.setUsers(users);
                    XmlUtils.saveUsers(wrapper, file);
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveToXml(User updatedUser) {
        try {
            File file = new File("D:/Kuliah/Project/praventa/data/users.xml");
            UserListWrapper wrapper = XmlUtils.loadUsers(file);

            if (updatedUser != null) {
                wrapper.replaceUser(updatedUser);
                XmlUtils.saveUsers(wrapper, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
