package com.example.cms.security.endpoint;

import com.example.cms.security.domain.answer.facade.AnswerFacade;
import com.example.cms.security.domain.useranswer.dto.VoteDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answer")
public class AnswerEndpoint {

    private final AnswerFacade answerFacade;


    public AnswerEndpoint(AnswerFacade answerFacade) {
        this.answerFacade = answerFacade;
    }



    @PostMapping("/vote")
    public void vote(@RequestBody VoteDto voteDto){
        answerFacade.addVote(voteDto);
    }
}
