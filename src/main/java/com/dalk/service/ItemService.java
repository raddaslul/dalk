package com.dalk.service;

import com.dalk.domain.*;
import com.dalk.exception.ex.ItemNotFoundException;
import com.dalk.exception.ex.LackPointException;
import com.dalk.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
@Slf4j
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
        String text1 = responseBody.substring(responseBody.indexOf("translatedText") + 17);
        return text1.substring(0, text1.indexOf("engineType") - 3); //뒤에 점찍히는거 뺄거면 -4 넣을거면 -3

    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text) throws IOException, NoSuchAlgorithmException {
        HttpURLConnection con = connect(apiUrl);
        Random random = SecureRandom.getInstanceStrong();
        int num = random.nextInt(11);
        String postParams;
        List<String> list = Arrays.asList("en","ja","zh-CN","vi","id","th","de","ru","es","it","fr");

        postParams = "source=ko&target=" + list.get(num) + "&text=" + text;
//        postParams = "source=ko&target=en&text=" + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
        try {
            con.setRequestMethod("POST");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
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

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
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
    private final ItemRepository itemRepository;
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    //아이템 구매
    @Transactional
    public void buyItem(ItemType item, User user) {

        Item userItem = itemRepository.findByUser_IdAndItemCode(user.getId(), item.getItemCode());
        user.buyItem(item, userItem);
        userRepository.save(user);
        Point point = new Point(item.getItemName() + "구매", -item.getPrice(), user.getTotalPoint(), user);
        pointRepository.save(point);
    }

    //아이템 사용
    @Transactional
    public void useItem(ItemType item, User user) {
        Item userItem = itemRepository.findByUser_IdAndItemCode(user.getId(), item.getItemCode());
        userRepository.save(user);
        user.useItem(userItem);
    }

    //단어 거꾸로 하기
    public static String reverseWord(String word) {
        StringBuffer sb = new StringBuffer(word);
        return sb.reverse().toString();
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
