//package com.dalk.domain.wl;
//
//
//import com.dalk.domain.User;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Entity
//@Table(name = "warn_user")
//public class WarnUser {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "is_warn")
//    private Boolean isWarn;
//
//    @Column(name = "warn_user")
//    private String warnUserName;
//
//    @OneToOne
//    @JoinColumn
//    private User user;
//
//
//
//}
