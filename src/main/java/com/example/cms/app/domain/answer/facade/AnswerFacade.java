package com.example.cms.app.domain.answer.facade;

import com.example.cms.app.domain.answer.service.AnswerService;
import com.example.cms.app.domain.useranswer.dto.VoteDto;
import org.springframework.stereotype.Component;

@Component
public class AnswerFacade {

    private final AnswerService answerService;


    public AnswerFacade(AnswerService answerService) {
        this.answerService = answerService;
    }


    public void addVote(VoteDto voteDto) {
        answerService.addVote(voteDto);


    }
}
