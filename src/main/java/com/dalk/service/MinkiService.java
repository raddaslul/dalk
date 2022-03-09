package com.dalk.service;

import com.dalk.domain.Category;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.domain.wl.WarnBoard;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MinkiService {
    private static PointRepository pointRepository;

    //항해99 5기 B반 김민기님이 해줌
    @Autowired
    public MinkiService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public static UserInfoResponseDto userInfo(User user) {
        return new UserInfoResponseDto(user);
    }

    public static List<String> categoryStringList(List<Category> categoryList) {
        List<String> stringList = new ArrayList<>();
        for (Category tag : categoryList) {
            String categoryString = tag.getCategory();
            stringList.add(categoryString);
        }
        return stringList;
    }



}
