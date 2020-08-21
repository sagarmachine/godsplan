package com.godsplan.demo.service;

import com.godsplan.demo.entity.Client;
import com.godsplan.demo.repo.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;


@Service
@Slf4j
public class ClientServiceImpl implements IClientService {


    @Value("${admin.email}")
    String adminEmail;


    @Autowired
    JavaMailSender emailSender;

    @Autowired
    ClientRepository clientRepository;

    public  void addClient(Client client){

       Optional<Client> client1=clientRepository.findByEmail(client.getEmail());
       if(client1.isPresent()) {
           log.info("exist");
           client.setId(client1.get().getId());
           client.setCreatedOn(new Date());
       }

        clientRepository.save(client);

    }


    public void deleteClient(String email){
        Optional<Client> client1=clientRepository.findByEmail(email);
        if(!client1.isPresent())
            throw  new RuntimeException(email+ " not found");

        clientRepository.delete(client1.get());

    }


    public void verifyClient(String to,String url){

        sendSimpleMessage(to,url);
    }


    public void sendSimpleMessage(String to,String url) {
        log.info("mail "+to);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            String inlineImage = "<div>Verify Your Account By clicking below link : <br>"+url+"</div>";
            helper.setText(inlineImage, true);
            helper.setSubject("gods plan verification");
            helper.setTo(to);
            helper.setFrom(adminEmail);
            emailSender.send(message);
            helper.setTo(to);
            emailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
