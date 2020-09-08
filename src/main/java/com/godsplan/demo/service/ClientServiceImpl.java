package com.godsplan.demo.service;

import com.godsplan.demo.entity.Client;
import com.godsplan.demo.repo.ClientRepository;
import lombok.extern.slf4j.Slf4j;
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
       client.setName(client.getFirstName()+" "+client.getLastName());
       if(client1.isPresent()) {
           log.info("exist");
           client.setId(client1.get().getId());
           client.setCreatedOn(new Date());
       }

       client.setPublicFigure(client.getPublicFigure().toUpperCase());

        clientRepository.save(client);

    }


    public void deleteClient(String email){
        Optional<Client> client1=clientRepository.findByEmail(email);
        if(!client1.isPresent())
            throw  new RuntimeException(email+ " not found");

        clientRepository.delete(client1.get());

    }


    public void verifyClient(String name, String category,String to,String url){

        sendSimpleMessage(name,category,to,url);
    }

    @Override
    public void verifyClaim(String email,String to,String url) {

        Optional<Client> clientOptional= clientRepository.findByEmail(email);

        sendClaimVerificationMail(clientOptional.get(),to,url);
    }


    public void sendSimpleMessage(String name, String category,String to,String url) {
        //log.info("mail "+to);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            String inlineImage = "<h3>Recipient</h3>\n" +
                    "<br/>\n" +
                    "<hr/>\n" +
                    "<br/>\n" +
                    "<strong>Your Confirmation Email To "+ (category.equals("yes")?"Acknowledge":category.equals("no")?"Deny":"Neither Acknowledge nor  Deny")+" God </strong>\n" +
                    "<br/>\n" +
                    "<hr/>\n" +
                    "<br/>\n" +
                    ""+name+"\n" +
                    "<br/>\n" +
                    "<br/>\n" +
                    "<p>you have indicated that you want to join the people who <strong>"+ (category.equals("yes")?"Acknowledge":category.equals("no")?"Deny":"Neither Acknowledge nor Deny")+"</strong> God</p>\n" +
                    "<br/>\n" +
                    "<p>However, in order to complete your registration you must verify your email within 24 hours.</p>\n" +
                    "<br/>\n" +
                    "\n" +
                    "<a href='"+url+"'>Confirm Your Email</a>\n" +
                    "<br/>\n" +
                    "<br/>\n" +
                    "<p>or copy and paste the following url into your browser</p><br/>\n"+
                    url;

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


    public void sendClaimVerificationMail(Client client,String to,String url){
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            String inlineImage = "<h3>Recipient</h3>\n" +
                    "<br/>\n" +
                    "<hr/>\n" +
                    "<br/>\n" +
                    "<strong>Your Claim Confirmation Email</strong>"+
                    "<br/>\n" +
                    "<hr/>\n" +
                    "<br/>\n" +
                    "By Clicking the below link you will be processed and reach out by our team for confirmation that weather or not you are :" +
                    "<br/>\n" +
                    "<br/>\n" +
                    "<strong>"+client.getFirstName()+" "+client.getLastName()+"</strong> a " +
                    "<strong>"+(client.getPublicFigure().equalsIgnoreCase("PF1")?"Scientist":"Public Figure ")+"</strong><br/><br/>" +
                    "<br/>\n" +
                    "<p>However, in order to initiate your claim you must verify your email within 24 hours.</p>\n" +
                    "<br/>\n" +
                    "\n" +
                    "<a href='"+url+"'>Confirm Your Email</a>\n" +
                    "<br/>\n" +
                    "<br/>\n" +
                    "<p>or copy and paste the following url into your browser</p><br/>\n"+
                    url;
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
