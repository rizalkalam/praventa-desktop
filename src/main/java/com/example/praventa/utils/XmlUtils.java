package com.example.praventa.utils;

import com.example.praventa.model.articles.Article;
import com.example.praventa.model.articles.ArticleListWrapper;
import com.example.praventa.model.users.User;
import com.example.praventa.model.users.UserListWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class XmlUtils {

    // =========================
    // === User XML Methods ===
    // =========================

    public static UserListWrapper loadUsers(File file) throws Exception {
        if (!file.exists() || file.length() == 0) {
            return new UserListWrapper();
        }

        JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (UserListWrapper) unmarshaller.unmarshal(file);
    }

    public static void saveUsers(UserListWrapper wrapper, File file) throws Exception {
        JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, file);
    }

    public static void saveUser(User updatedUser, File file) throws Exception {
        UserListWrapper wrapper = loadUsers(file);
        wrapper.replaceUser(updatedUser);
        saveUsers(wrapper, file);
    }

    // ============================
    // === Artikel XML Methods ===
    // ============================

    public static ArticleListWrapper loadArtikels(File file) throws Exception {
        if (!file.exists() || file.length() == 0) {
            return new ArticleListWrapper(); // default list kosong
        }

        JAXBContext context = JAXBContext.newInstance(ArticleListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (ArticleListWrapper) unmarshaller.unmarshal(file);
    }

    public static void saveArtikels(ArticleListWrapper wrapper, File file) throws Exception {
        JAXBContext context = JAXBContext.newInstance(ArticleListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, file);
    }

    public static void saveArtikel(Article artikel, File file) throws Exception {
        ArticleListWrapper wrapper = loadArtikels(file);

        // Gunakan getArticleList() bukan getArtikelList()
        wrapper.getArticleList().removeIf(a ->
                a.getJudul().equalsIgnoreCase(artikel.getJudul()) &&
                        a.getTanggal().equalsIgnoreCase(artikel.getTanggal())
        );

        wrapper.getArticleList().add(artikel);
        saveArtikels(wrapper, file);
    }

}