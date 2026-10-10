package ru.otus.hw.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TestServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    private TestServiceImpl testService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void shouldExecuteTestForStudentAndReturnCorrectResult() {
        Student student = new Student("John", "Doe");
        Question question1 = new Question("Question 1?",
                List.of(new Answer("Yes", true), new Answer("No", false)));
        Question question2 = new Question("Question 2?",
                List.of(new Answer("A", false), new Answer("B", true)));
        when(questionDao.findAll()).thenReturn(List.of(question1, question2));
        when(ioService.readIntForRangeWithPrompt(eq(1), eq(2), anyString(), anyString()))
                .thenReturn(1)
                .thenReturn(2);

        var result = testService.executeTestFor(student);

        assertThat(result.getStudent()).isEqualTo(student);
        assertThat(result.getAnsweredQuestions()).hasSize(2);
        assertThat(result.getRightAnswersCount()).isEqualTo(2);
        verify(questionDao, atLeastOnce()).findAll();
        verify(ioService, atLeastOnce()).printFormattedLine(anyString(), anyInt(), anyString());
    }

    @Test
    void shouldRecordWrongAnswerWhenUserPicksIncorrectOption() {
        Student student = new Student("Jane", "Doe");
        Question question = new Question("Only one?",
                List.of(new Answer("Correct", true), new Answer("Wrong", false)));
        when(questionDao.findAll()).thenReturn(List.of(question));
        when(ioService.readIntForRangeWithPrompt(eq(1), eq(2), anyString(), anyString()))
                .thenReturn(2);

        var result = testService.executeTestFor(student);

        assertThat(result.getRightAnswersCount()).isZero();
        assertThat(result.getAnsweredQuestions()).hasSize(1);
    }

    @Test
    void shouldReturnEmptyResultWhenNoQuestionsLoaded() {
        Student student = new Student("Sam", "Smith");
        when(questionDao.findAll()).thenReturn(List.of());

        var result = testService.executeTestFor(student);

        assertThat(result.getAnsweredQuestions()).isEmpty();
        assertThat(result.getRightAnswersCount()).isZero();
    }
}
