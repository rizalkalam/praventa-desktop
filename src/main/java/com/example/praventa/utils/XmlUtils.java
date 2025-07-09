package com.example.praventa.utils;

import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class XmlUtils {
    /**
     * Membaca data pengguna dari file XML.
     */
    public static UserListWrapper loadUsers(File file) throws Exception {
        if (!file.exists() || file.length() == 0) {
            return new UserListWrapper(); // file kosong → return list kosong
        }

        JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (UserListWrapper) unmarshaller.unmarshal(file);
    }

    /**
     * Menyimpan data pengguna ke file XML.
     */
    public static void saveUsers(UserListWrapper wrapper, File file) throws Exception {
        JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, file);
    }

    /**
     * Menyimpan satu pengguna (update atau tambah) ke dalam file XML.
     */
    public static void saveUser(User updatedUser, File file) throws Exception {
        UserListWrapper wrapper = loadUsers(file);
        wrapper.replaceUser(updatedUser);
        saveUsers(wrapper, file);
    }
}
