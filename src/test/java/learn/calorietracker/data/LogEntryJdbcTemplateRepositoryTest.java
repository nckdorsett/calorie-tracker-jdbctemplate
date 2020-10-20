package learn.calorietracker.data;

import learn.calorietracker.models.LogEntry;
import learn.calorietracker.models.LogEntryType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogEntryJdbcTemplateRepositoryTest {

    LogEntryJdbcTemplateRepository repository;

    public LogEntryJdbcTemplateRepositoryTest() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DbTestConfig.class);
        repository = context.getBean(LogEntryJdbcTemplateRepository.class);
    }

    @BeforeAll
    static void oneTimeSetup() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DbTestConfig.class);
        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        jdbcTemplate.update("call set_known_good_state();");
    }

    @Test
    void shouldFindAll() {
        List<LogEntry> all = repository.findAll();

        assertNotNull(all);
        assertTrue(all.size() >= 2);

        LogEntry expected = new LogEntry();
        expected.setId(1);
        expected.setLoggedOn("2020-01-01 09:00:00");
        expected.setType(LogEntryType.BREAKFAST);
        expected.setDescription("Scrambled eggs");
        expected.setCalories(210);

        assertTrue(all.contains(expected)
        && all.stream().anyMatch(i -> i.getId() == 2));
    }

    @Test
    void shouldFindById() {

        LogEntry expected = new LogEntry();
        expected.setId(1);
        expected.setLoggedOn("2020-01-01 09:00:00");
        expected.setType(LogEntryType.BREAKFAST);
        expected.setDescription("Scrambled eggs");
        expected.setCalories(210);

        LogEntry actual = repository.findById(1);
        assertEquals(expected, actual);

    }

    @Test
    void shouldNotFindByMissingId() {

        LogEntry actual = repository.findById(9000);
        assertNull(actual);
    }

    @Test
    void shouldFindByType() {

        List<LogEntry> actual = repository.findByType(LogEntryType.BREAKFAST);
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void shouldNotFindByType() {
        List<LogEntry> actual = repository.findByType(LogEntryType.SNACK);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void shouldAdd() {
        LogEntry expected = new LogEntry();
        expected.setLoggedOn("2020-01-01 09:00:00");
        expected.setType(LogEntryType.SECOND_BREAKFAST);
        expected.setDescription("Scrambled eggs");
        expected.setCalories(210);

        LogEntry actual = repository.create(expected);
        expected.setId(4);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }





}