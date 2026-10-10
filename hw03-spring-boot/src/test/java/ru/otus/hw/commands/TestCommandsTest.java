package ru.otus.hw.commands;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class TestCommandsTest {

    @Autowired
    private TestCommands testCommands;

    @MockBean
    private TestRunnerService testRunnerService;

    @MockBean
    private StudentService studentService;

    @Test
    void shouldInvokeTestRunnerService() {
        testCommands.runTest("John Doe");
        verify(testRunnerService).runForStudent(any());
    }
}
