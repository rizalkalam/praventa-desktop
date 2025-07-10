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

                // Gabungkan hanya bagian family disease history
                List<FamilyDiseaseHistory> existingList = existingUser.getFamilyDiseaseHistoryList();
                List<FamilyDiseaseHistory> newList = updatedUser.getFamilyDiseaseHistoryList();

                if (existingList == null) existingList = new ArrayList<>();
                if (newList != null) existingList.addAll(newList);

                existingUser.setFamilyDiseaseHistoryList(existingList);

                // Tidak timpa field lainnya, hanya perbarui bagian ini
                users.set(i, existingUser);
                return;
            }
        }

        // Jika user belum ada, tambahkan langsung
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
