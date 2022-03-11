package com.dalk.dto.responseDto.MainPageResponse;

import com.dalk.domain.vote.Vote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoserResponse {
    private String topic;
    private String rate;
    private String totalPoint;
    private String cnt;
    private String topPoint;

    public LoserResponse(Vote vote) {
        if (vote.getTopicACnt() > vote.getTopicBCnt()) {
            this.topic = vote.getBoard().getTopicB();
            this.rate = "0";
            this.totalPoint = String.format("%.0f",vote.getTotalPointB());
            this.cnt = String.valueOf(vote.getTopicBCnt());
            this.topPoint = String.valueOf(vote.getTopPointB());
        } else if(vote.getTopicACnt() < vote.getTopicBCnt()){
            this.topic = vote.getBoard().getTopicA();
            this.rate = "0";
            this.totalPoint = String.format("%.0f",vote.getTotalPointA());
            this.cnt = String.valueOf(vote.getTopicACnt());
            this.topPoint = String.valueOf(vote.getTopPointA());
        }else{
            this.topic = vote.getBoard().getTopicB();
            this.rate = "0";
            this.totalPoint = String.format("%.0f",vote.getTotalPointB());
            this.cnt = String.valueOf(vote.getTopicBCnt());
            this.topPoint = String.valueOf(vote.getTopPointB());
        }
    }
}
