package com.dalk.service.papago;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// 네이버 기계번역 (Papago SMT) API 예제
@Service
public class PapagoService {

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
        String text1 = responseBody.substring(78);
        return text1.substring(0, text1.indexOf(",")-1);
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
}
