package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (Reader reader = new InputStreamReader(
                new ClassPathResource(fileNameProvider.getTestFileName()).getInputStream(),
                StandardCharsets.UTF_8)) {

            var strategy = new ColumnPositionMappingStrategy<QuestionDto>();
            strategy.setType(QuestionDto.class);

            var beans = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withMappingStrategy(strategy)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();

            return beans.stream()
                    .map(QuestionDto::toDomainObject)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new QuestionReadException("Error reading questions from " + fileNameProvider.getTestFileName(), e);
        }
    }
}
