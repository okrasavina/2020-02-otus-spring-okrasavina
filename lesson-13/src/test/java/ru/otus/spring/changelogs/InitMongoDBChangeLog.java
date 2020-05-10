package ru.otus.spring.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBChangeLog {
    private Author ilyaIlf;
    private Author eugenePetrov;
    private Author sergeyEsenin;
    private Genre comedy;
    private Genre adventure;
    private Genre lyricPoem;
    private Book defaultBook;
    private Book goldenCalf;

    @ChangeSet(order = "000", id = "dropDB", author = "okrasavina", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "okrasavina", runAlways = true)
    public void initAuthors(MongoTemplate mongoTemplate) {
        ilyaIlf = mongoTemplate.save(new Author("Илья Ильф"));
        eugenePetrov = mongoTemplate.save(new Author("Евгений Петров"));
        sergeyEsenin = mongoTemplate.save(new Author("Сергей Есенин"));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "okrasavina", runAlways = true)
    public void initGenres(MongoTemplate mongoTemplate) {
        comedy = mongoTemplate.save(new Genre("Комедия"));
        adventure = mongoTemplate.save(new Genre("Приключения"));
        lyricPoem = mongoTemplate.save(new Genre("Лирическая поэма"));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "okrasavina", runAlways = true)
    public void initBooks(MongoTemplate mongoTemplate) {
        defaultBook = mongoTemplate.save(new Book(1L, "12 стульев",
                List.of(ilyaIlf, eugenePetrov), List.of(comedy, adventure)));
        goldenCalf = mongoTemplate.save(new Book(2L,
                "Золотой теленок", List.of(ilyaIlf, eugenePetrov), List.of(comedy, adventure)));
        mongoTemplate.save(new Book(3L, "Черный человек",
                List.of(sergeyEsenin), List.of(lyricPoem)));
    }

    @ChangeSet(order = "004", id = "initComments", author = "okrasavina", runAlways = true)
    public void initComments(MongoTemplate mongoTemplate) {
        mongoTemplate.save(new Comment("Читать эту книгу легко и приятно", goldenCalf));
        mongoTemplate.save(new Comment("Книга, ставшая классикой!", goldenCalf));
        mongoTemplate.save(new Comment("Это очень интересная книга", defaultBook));
    }
}
