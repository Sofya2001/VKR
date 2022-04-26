package ru.chirkova.vkr_asych;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.chirkova.vkr_asych.ModelMessageRequest.MessageBody;
import ru.chirkova.vkr_asych.ModelMessageRequest.MessageHeader;
import ru.chirkova.vkr_asych.ModelMessageRequest.MessageRequest;
import ru.chirkova.vkr_asych.ProducerService.Producer;

import java.time.LocalDateTime;
import java.util.Random;

@SpringBootApplication
public class VkrAsychApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(VkrAsychApplication.class, args);
    }
    @Autowired
    private Producer producer;

//    @Override
//    public void run(String... args) throws Exception {
//        MessageRequest mesReq= new MessageRequest(
//                new MessageHeader(generationString(new Random().nextInt(20 - 1)+ 1),
//                        String.valueOf(new Random().nextInt(5000 - 1)+ 1),
//                        String.valueOf(LocalDateTime.now()),
//                        generationString(new Random().nextInt(15 - 1)+ 1),
//                        String.valueOf(new Random().nextInt(1000 - 1)+ 1)),
//                        new MessageBody(
//                                generationString(new Random().nextInt(10 - 1)+ 1) + "- method",
//                                new String[]{ generationString(new Random().nextInt(10 - 1)+ 1),
//                                        String.valueOf(new Random().nextInt(1000 - 1)+ 1)}));
//        producer.sendMessageWithCallback(mesReq);
//    }
    @Override
    public void run(String... args) throws Exception {
        MessageRequest mesReq= new MessageRequest(
                new MessageHeader(generationString(new Random().nextInt(20 - 1)+ 1),
                        String.valueOf(new Random().nextInt(5000 - 1)+ 1),
                        String.valueOf(LocalDateTime.now()),
                        generationString(new Random().nextInt(15 - 1)+ 1),
                        String.valueOf(new Random().nextInt(1000 - 1)+ 1)),
                new MessageBody(
                        generationString(new Random().nextInt(10 - 1)+ 1) + "- method",
                        new String[]{ generationString(new Random().nextInt(10 - 1)+ 1),
                                String.valueOf(new Random().nextInt(1000 - 1)+ 1)}));
        producer.sendMessageWithCallback(mesReq);
    }

     String generationString(int n) {

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
