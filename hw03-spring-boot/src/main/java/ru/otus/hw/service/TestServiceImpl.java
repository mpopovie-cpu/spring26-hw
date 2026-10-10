package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            boolean isAnswerValid = askQuestionAndValidateAnswer(i + 1, question);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private boolean askQuestionAndValidateAnswer(int questionNumber, Question question) {
        ioService.printFormattedLine("%d) %s", questionNumber, question.text());
        List<?> answers = question.answers();
        for (int j = 0; j < answers.size(); j++) {
            ioService.printFormattedLine("%d: %s", j + 1, answers.get(j));
        }
        int answerIndex = ioService.readIntForRangeWithPrompt(
                1,
                answers.size(),
                "Select answer number:",
                "Invalid input, please try again");
        var selectedAnswer = question.answers().get(answerIndex - 1);
        return selectedAnswer.isCorrect();
    }
}
