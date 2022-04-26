package ru.chirkova.vkr_asych.ModelMessageRequest;


import com.fasterxml.jackson.databind.ObjectMapper;
import ru.chirkova.vkr_asych.ModelMessageAnswer.ErrorMessage;
import ru.chirkova.vkr_asych.ModelMessageAnswer.MessageCallBack;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;


public class Test {

    public static void main(String[] args)  {
//        MessageCallBack e = new MessageCallBack(new MessageHeader("aed","awd",String.valueOf(LocalDateTime.now()),"awdaw","awda"),
//                            new MessageBody("SF",new String[]{"sDF"}));
//        System.out.println("-- before serialization --");
//        System.out.println(e);
//
//
//        ObjectMapper om = new ObjectMapper();
//        String jsonString = om.writerWithDefaultPrettyPrinter().writeValueAsString(e);
//        System.out.println("-- after serialization --");
//        System.out.println(jsonString);

        // Получаем размер n

        int x=new Random().nextInt(50 - 1)+ 1;

        System.out.println(generationString(x));

    }


            // функция для генерации случайной строки длиной n

    static String generationString(int n) {

        // выбрал символ случайный из этой строки
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String BetaNumericString = "abcdefghijklmnopqrstuvxyz";

        // создаем StringBuffer размером AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            // генерируем случайное число между
            // 0 переменной длины AlphaNumericString
            int index1 = (int)(BetaNumericString.length()  * Math.random());

            // добавляем символ один за другим в конец sb
            sb.append(BetaNumericString.charAt(index1));
        }
        int index2 = (int)(AlphaNumericString.length()  * Math.random());
        return AlphaNumericString.charAt(index2) + sb.toString();
    }


}
