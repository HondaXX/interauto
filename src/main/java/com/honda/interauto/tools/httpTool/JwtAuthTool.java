package com.honda.interauto.tools.httpTool;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.honda.interauto.entity.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class JwtAuthTool {
    private static final Logger logger = LogManager.getLogger(JwtAuthTool.class);

    private static final String SECRET = "Honda";
    private static final String EXP = "exp";
    private static final String PAYLOAD = "payload";

    public static String getToken(UserEntity user){
        try{
            String tokenStr = JWT.create().withAudience(String.valueOf(user.getId())).sign(Algorithm.HMAC256(user.getPassword()));
            return tokenStr;
        }catch (Exception e){
            logger.error("========>get token error");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){
        UserEntity userDto = new UserEntity();
        userDto.setId(888);
        userDto.setName("aa");
        userDto.setPassword("bb");
        String s = getToken(userDto);
        System.out.println(s);
    }
}
