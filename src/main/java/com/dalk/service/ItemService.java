package com.dalk.service;

import com.dalk.domain.Category;
import com.dalk.domain.Item;
import com.dalk.domain.Point;
import com.dalk.domain.User;
import com.dalk.dto.responseDto.UserInfoResponseDto;
import com.dalk.exception.ex.ItemNotFoundException;
import com.dalk.exception.ex.LackPointException;
import com.dalk.repository.ItemRepository;
import com.dalk.repository.PointRepository;
import com.dalk.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

// 네이버 기계번역 (Papago SMT) API 예제
@Service
@AllArgsConstructor
@Transactional
public class ItemService {

    //파파고
    public static String papago(String string) throws IOException, NoSuchAlgorithmException {
        String clientId = "8r31LT9MJMcXuUniJSm1";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "lt8SRWXOgS";//애플리케이션 클라이언트 시크릿값";

        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
        String text;
        try {
            text = URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = post(apiURL, requestHeaders, text); //74개 짜르기
        String text1 = responseBody.substring(responseBody.indexOf("translatedText")+17);
        return text1.substring(0, text1.indexOf("engineType")-4); //뒤에 점찍히는거 뺄거면 -4 넣을거면 -3
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text) throws IOException, NoSuchAlgorithmException {
        HttpURLConnection con = connect(apiUrl);
        Random random = SecureRandom.getInstanceStrong();
        int num = random.nextInt(11);
        String postParams;
        if (num == 1) {
            postParams = "source=ko&target=en&text=" + text;
        } else if(num==2) {
            postParams = "source=ko&target=ja&text=" + text;
        }else if(num==3) {
            postParams = "source=ko&target=zh-CN&text=" + text;
        }else if(num==4) {
            postParams = "source=ko&target=vi&text=" + text;
        }else if(num==5) {
            postParams = "source=ko&target=id&text=" + text;
        }else if(num==6) {
            postParams = "source=ko&target=th&text=" + text;
        }else if(num==7) {
            postParams = "source=ko&target=de&text=" + text;
        }else if(num==8) {
            postParams = "source=ko&target=ru&text=" + text;
        }else if(num==9) {
            postParams = "source=ko&target=es&text=" + text;
        }else if(num==10) {
            postParams = "source=ko&target=it&text=" + text;
        }else{
            postParams = "source=ko&target=fr&text=" + text;
        }
//        postParams = "source=ko&target=en&text=" + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    //여기부터 아이템 쓰는 곳곳

    private final Long onlyMePrice = 100L;
    private final Long bigFontPrice = 100L;
    private final Long myNamePrice = 100L;
    private final Long papagoPrice = 100L;
    private final Long reversePrice = 100L;
    private final Integer exBuyPrice = 100;

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    //아이템 구매
    public void buyItem(String item, User user) {
        Item buyitem = itemRepository.findById(user.getItem().getId()).orElseThrow(
                () -> new ItemNotFoundException("아이템이 없습니다")
        );
//        Point recentPoint = pointRepository.findTopByUserIdOrderByCreatedAt(user.getId()); //얘는 왜 예외처리 안뜸?
        switch (item) {
            case "onlyMe":
                buyitem.setOnlyMe(buyitem.getOnlyMe() + 1);
                itemRepository.save(buyitem);
                itemBuy(user, onlyMePrice, "나만 말하기");
                break;
            case "bigFont":
                buyitem.setBigFont(buyitem.getBigFont() + 1);
                itemRepository.save(buyitem);
                itemBuy(user, bigFontPrice, "내글자 크게하기");
                break;
            case "myName":
                buyitem.setMyName(buyitem.getMyName() + 1);
                itemRepository.save(buyitem);
                itemBuy(user, myNamePrice, "모두 내이름으로 바꾸기");
                break;
            case "papago":
                buyitem.setPapago(buyitem.getPapago() + 1);
                itemRepository.save(buyitem);
                itemBuy(user, papagoPrice, "파파고 랜덤 번역");
                break;
            case "reverse":
                buyitem.setReverse(buyitem.getReverse() + 1);
                itemRepository.save(buyitem);
                itemBuy(user, reversePrice, "글자 뒤집기");
                break;
            case "exBuy":
                user.setEx(user.getEx()+exBuyPrice);
                userRepository.save(user);
                itemBuy(user, Long.valueOf(exBuyPrice),"경험치");
                break;
        }
    }

    //아이템 구매
    private void itemBuy(User user, Long price, String item) {
        if (user.getTotalPoint() >= price) {
            user.setTotalPoint(user.getTotalPoint()-price);
            userRepository.save(user);
            Point point = new Point(item + " 구매", -price, user.getTotalPoint(), user);
            pointRepository.save(point);
        } else {
            throw new LackPointException("보유한 포인트가 부족합니다");
        }
    }

    //아이템 사용
    public void useItem(String item, User user) {
        Item useitem = itemRepository.findById(user.getItem().getId()).orElseThrow(
                () -> new ItemNotFoundException("아이템이 없습니다")
        );
        switch (item) {
            case "onlyMe":
                if(useitem.getOnlyMe()>=1) {
                    useitem.setOnlyMe(useitem.getOnlyMe() - 1);
                    itemRepository.save(useitem);
                }else {
                    throw  new ItemNotFoundException("아이템이 없습니다");
                }
                break;
            case "bigFont":
                if(useitem.getBigFont()>=1) {
                    useitem.setBigFont(useitem.getBigFont() - 1);
                    itemRepository.save(useitem);
                }else {
                    throw  new ItemNotFoundException("아이템이 없습니다");
                }
                break;
            case "myName":
                if(useitem.getMyName()>=1) {
                    useitem.setMyName(useitem.getMyName() - 1);
                    itemRepository.save(useitem);
                }else {
                    throw  new ItemNotFoundException("아이템이 없습니다");
                }
                break;
            case "papago":
                if(useitem.getPapago()>=1) {
                    useitem.setPapago(useitem.getPapago() - 1);
                    itemRepository.save(useitem);
                }else {
                    throw  new ItemNotFoundException("아이템이 없습니다");
                }
                break;
            case "reverse":
                if(useitem.getReverse()>=1) {
                    useitem.setReverse(useitem.getReverse() - 1);
                    itemRepository.save(useitem);
                }else {
                    throw  new ItemNotFoundException("아이템이 없습니다");
                }
                break;
        }
    }

    //단어 거꾸로 하기
    public static String reverseWord(String word) {
        StringBuffer sb = new StringBuffer(word);
        return sb.reverse().toString();
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
