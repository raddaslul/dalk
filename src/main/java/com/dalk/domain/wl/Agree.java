package com.dalk.domain;


import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Agree {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long agreeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column
    private Boolean isAgree;
    @Column
    private Integer agreeCnt = 0;

//    @Column
//    private Boolean isDisAgree;
//    @Column
//    private Integer agreeCnt = 0;
//    @Column
//    private Integer disAgreeCnt = 0;



    public Agree(Comment comment, User user,boolean requestDto) {
        this.comment = comment;
        this.user = user;
        this.isAgree=requestDto;
    }


}
