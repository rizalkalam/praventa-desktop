package com.example.praventa.repository;

import com.example.praventa.model.articles.Article;
import com.example.praventa.model.articles.ArticleListWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepository {
    private final String path = "D:\\Kuliah\\Project\\praventa\\data\\articles.xml";

    public List<Article> loadArticles() {
        try {
            File file = new File(path);
            JAXBContext context = JAXBContext.newInstance(ArticleListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            ArticleListWrapper wrapper = (ArticleListWrapper) um.unmarshal(file);
            return wrapper.getArticleList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveArticles(List<Article> articles) {
        try {
            File file = new File(path);
            JAXBContext context = JAXBContext.newInstance(ArticleListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            ArticleListWrapper wrapper = new ArticleListWrapper();
            wrapper.setArticleList(articles);
            marshaller.marshal(wrapper, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
