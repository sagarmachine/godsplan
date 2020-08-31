package com.godsplan.demo.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false,unique = false)
    String email;

    @Column(nullable = false)
     String firstName;

    @Column(nullable = false)
   String lastName;

    @Column(nullable = false)
    @CreationTimestamp
     @JsonFormat(pattern = "MM-dd-yyyy")
    Date createdOn;

    @Column(nullable = false)
 String city;

    @Column(nullable = false)
   String country;

 @Column(nullable = false)
 String  category;

 @Column(nullable = false)
 String publicFigure;

 @Column(nullable = false)
  boolean verified=false;


   String code;


   String background;

    public boolean getVerified() {
        return verified;
    }
}
