package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvQuestionDaoTest {

    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        TestFileNameProvider provider = () -> "questions.csv";
        csvQuestionDao = new CsvQuestionDao(provider);
    }

    @Test
    void shouldLoadAllQuestionsFromClasspathResource() {
        List<Question> questions = csvQuestionDao.findAll();

        assertThat(questions).isNotEmpty();
        assertThat(questions).hasSize(3);
    }

    @Test
    void shouldParseQuestionTextAndAnswersCorrectly() {
        List<Question> questions = csvQuestionDao.findAll();

        Question first = questions.get(0);
        assertThat(first.text()).isNotBlank();
        assertThat(first.answers()).isNotEmpty();
        assertThat(first.answers()).allSatisfy(answer -> {
            assertThat(answer.text()).isNotBlank();
        });
    }

    @Test
    void shouldHaveExactlyOneCorrectAnswerPerQuestion() {
        List<Question> questions = csvQuestionDao.findAll();

        assertThat(questions).allSatisfy(question -> {
            long correctCount = question.answers().stream()
                    .filter(a -> a.isCorrect())
                    .count();
            assertThat(correctCount)
                    .as("Question '%s' should have exactly one correct answer", question.text())
                    .isEqualTo(1L);
        });
    }
}
