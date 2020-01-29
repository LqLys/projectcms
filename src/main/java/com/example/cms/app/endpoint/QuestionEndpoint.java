package com.example.cms.app.endpoint;

import com.example.cms.app.domain.question.facade.QuestionFacade;
import com.example.cms.app.domain.travelgroup.dto.CreateQuestionDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question")
public class QuestionEndpoint {

    private final QuestionFacade questionFacade;

    public QuestionEndpoint(QuestionFacade questionFacade) {
        this.questionFacade = questionFacade;
    }

    @PostMapping("/add")
    public void addQuestionWithAnswers(@RequestBody CreateQuestionDto createQuestionDto) {
        questionFacade.createQuestionWithAnswers(createQuestionDto);
    }
}
