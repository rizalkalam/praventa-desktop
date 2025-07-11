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

                // ✅ Update hanya data yang baru
                if (updatedUser.getLifestyleData() != null)
                    existingUser.setLifestyleData(updatedUser.getLifestyleData());

                if (updatedUser.getRecommendation() != null)
                    existingUser.setRecommendation(updatedUser.getRecommendation());

                // 🔁 Gabungkan family disease history
                List<FamilyDiseaseHistory> newList = updatedUser.getFamilyDiseaseHistoryList();
                if (newList != null) {
                    List<FamilyDiseaseHistory> existingList = existingUser.getFamilyDiseaseHistoryList();
                    if (existingList == null) existingList = new ArrayList<>();
                    existingList.addAll(newList);
                    existingUser.setFamilyDiseaseHistoryList(existingList);
                }

                // ⛔ Jangan ganti dengan updatedUser secara keseluruhan!
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
}
