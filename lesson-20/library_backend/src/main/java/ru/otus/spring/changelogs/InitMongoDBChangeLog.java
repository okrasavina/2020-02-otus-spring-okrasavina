package ru.otus.spring.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Comment;
import ru.otus.spring.domain.Genre;

import java.time.LocalDate;
import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBChangeLog {

    private Author ilyaIlf;
    private Author eugenePetrov;
    private Author valentinPikul;
    private Genre comedy;
    private Genre adventure;
    private Genre historicalNovel;
    private Book goldenCalf;

    @ChangeSet(order = "000", id = "dropDB", author = "okrasavina", runAlways = true)
    public void dropDB(MongoTemplate mongoTemplate) {
        mongoTemplate.getDb().drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "okrasavina", runAlways = true)
    public void initAuthors(MongoTemplate mongoTemplate) {
        ilyaIlf = mongoTemplate.save(new Author("Илья Ильф", LocalDate.of(1897, 10, 15)));
        eugenePetrov = mongoTemplate.save(new Author("Евгений Петров", LocalDate.of(1902, 12, 13)));
        valentinPikul = mongoTemplate.save(new Author("Валентин Пикуль", LocalDate.of(1928, 07, 13)));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "okrasavina", runAlways = true)
    public void initGenres(MongoTemplate mongoTemplate) {
        comedy = mongoTemplate.save(new Genre("Комедия",
                "Драматический жанр литературы, в центре которого стоит комическое смешное событие"));
        adventure = mongoTemplate.save(new Genre("Приключения",
                "Один из видов художественной литературы, основным содержанием которой является захватывающий рассказ о реальных или вымышленных событиях."));
        historicalNovel = mongoTemplate.save(new Genre("Исторический роман",
                "Произведение художественной литературы, события которого разворачиваются на фоне исторических событий и с участием реальных исторических личностей."));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "okrasavina", runAlways = true)
    public void initBooks(MongoTemplate mongoTemplate) {
        mongoTemplate.save(new Book("12 стульев", "Герой романа Остап Бендер из мелкого жулика превращается в романтического героя, лишнего человека, глубоко укорененного в русской традиции.",
                List.of(ilyaIlf, eugenePetrov), List.of(comedy, adventure)));
        goldenCalf = mongoTemplate.save(new Book("Золотой теленок", "Приключения великого комбинатора Остапа Бендера.",
                List.of(ilyaIlf, eugenePetrov), List.of(comedy, adventure)));
        mongoTemplate.save(new Book("Честь имею", "Исповедь офицера Российского Генштаба.",
                List.of(valentinPikul), List.of(historicalNovel)));
        mongoTemplate.save(new Book("Слово и дело", "Роман-хроника времен Анны Иоановны.",
                List.of(valentinPikul), List.of(historicalNovel)));
        mongoTemplate.save(new Book("Пером и шпагой", "История жизни Шарля де Бомона, шевалье де Еона - воистину великого дипломата и шпиона.",
                List.of(valentinPikul), List.of(historicalNovel)));
    }

    @ChangeSet(order = "004", id = "initComments", author = "okrasavina", runAlways = true)
    public void initComments(MongoTemplate mongoTemplate) {
        mongoTemplate.save(new Comment("Читать эту книгу легко и приятно", goldenCalf));
        mongoTemplate.save(new Comment("Книга, ставшая классикой!", goldenCalf));
    }
}
