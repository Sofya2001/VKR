package ru.chirkova.vkr_asych;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.chirkova.vkr_asych.ModelMessageRequest.MessageBody;
import ru.chirkova.vkr_asych.ModelMessageRequest.MessageHeader;
import ru.chirkova.vkr_asych.ModelMessageRequest.MessageRequest;
import ru.chirkova.vkr_asych.ProducerService.Producer;
//import ru.chirkova.vkr_asych.ProducerService.Producer;
//import ru.chirkova.vkr_asych.ProducerService.Producer;
////import ru.chirkova.vkr_asych.ProducerService.Producer;

import java.time.LocalDateTime;
import java.util.Random;

@SpringBootApplication
public class VkrAsychApplication
       implements CommandLineRunner
{

    public static void main(String[] args) {
        SpringApplication.run(VkrAsychApplication.class, args);
    }



    @Autowired

    private Producer producer;

    @Override
    public void run(String... args) throws Exception {

        MessageRequest request= new MessageRequest(
                new MessageHeader("Message","",String.valueOf(LocalDateTime.now()),"Producer"),
                new MessageBody("async","hello")
        );
        producer.sendMessageWithCallback(request);

    }}

//



////
//
////    MessageRequest mesReq = new MessageRequest(
////            new MessageHeader(generationString(new Random().nextInt(20 - 1) + 1),
////                    String.valueOf(new Random().nextInt(5000 - 1) + 1),
////                    String.valueOf(LocalDateTime.now()),
////                    "ProducerService",
////                    null),
////            new MessageBody(
////                    "async - method",
////                    new String[]{generationString(new Random().nextInt(10 - 1) + 1),
////                            String.valueOf(new Random().nextInt(1000 - 1) + 1)}));
//
//    @Override
//    public void run(String... args) throws Exception {
//        MessageRequest mesReq = new MessageRequest(
//                new MessageHeader(generationString(new Random().nextInt(20 - 1) + 1),
//                        String.valueOf(new Random().nextInt(5000 - 1) + 1),
//                        String.valueOf(LocalDateTime.now()),
//                        "ProducerService",
//                        null),
//                new MessageBody(
//                        "async - method",
//                        new String[]{generationString(new Random().nextInt(10 - 1) + 1),
//                                String.valueOf(new Random().nextInt(1000 - 1) + 1)}));
//
//        producer.sendMessageWithCallback(mesReq);
//    }
//
//    String generationString(int n) {
//
//
//        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        String BetaNumericString = "abcdefghijklmnopqrstuvxyz";
//
//
//        StringBuilder sb = new StringBuilder(n);
//        for (int i = 0; i < n; i++) {
//
//            int index1 = (int) (BetaNumericString.length() * Math.random());
//
//
//            sb.append(BetaNumericString.charAt(index1));
//        }
//        int index2 = (int) (AlphaNumericString.length() * Math.random());
//        return AlphaNumericString.charAt(index2) + sb.toString();
//    }
//}
////
