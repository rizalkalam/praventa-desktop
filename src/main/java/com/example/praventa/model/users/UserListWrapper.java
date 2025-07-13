package com.example.praventa.model.users;


import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserListWrapper {

    @XmlElement(name = "user")
    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void replaceUser(User updatedUser) {
        if (updatedUser == null || updatedUser.getUsername() == null) return;

        for (int i = 0; i < users.size(); i++) {
            User existingUser = users.get(i);
            if (updatedUser.getUsername().equals(existingUser.getUsername())) {

                // ✅ Update hanya data lifestyle
                if (updatedUser.getLifestyleData() != null)
                    existingUser.setLifestyleData(updatedUser.getLifestyleData());

                // ✅ Update hanya data recommendation
                if (updatedUser.getRecommendation() != null)
                    existingUser.setRecommendation(updatedUser.getRecommendation());

                // ✅ Gabungkan familyDiseaseHistory tanpa duplikat
                List<FamilyDiseaseHistory> newList = updatedUser.getFamilyDiseaseHistoryList();
                if (newList != null && !newList.isEmpty()) {
                    List<FamilyDiseaseHistory> existingList = existingUser.getFamilyDiseaseHistoryList();
                    if (existingList == null) {
                        existingList = new ArrayList<>();
                    }

                    for (FamilyDiseaseHistory newHistory : newList) {
                        if (!existingList.contains(newHistory)) {
                            existingList.add(newHistory);
                        }
                    }
                    existingUser.setFamilyDiseaseHistoryList(existingList);
                }

                // 🛠 Update lainnya bisa kamu tambahkan di sini jika ada...

                // ⛔ Jangan replace seluruh objek agar tidak menimpa data lain
                users.set(i, existingUser);
                return;
            }
        }

        // Jika user belum ada, tambahkan
        users.add(updatedUser);
    }

    public User getUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public User getUserByUsername(String username) {
        if (username == null) return null;

        for (User user : users) {
            if (username.equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }
}
