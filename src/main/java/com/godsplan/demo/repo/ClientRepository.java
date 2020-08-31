package com.godsplan.demo.repo;

import com.godsplan.demo.entity.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientRepository  extends JpaRepository<Client,Long> {

    Optional<Client> findByEmail(String email);

  //
    //  List<Client> findAllByOrderByCountryCreatedOnDesc();

    //@Query(  value = "select COUNT(email),country from client group by country")
    @Query(nativeQuery = true, value = "select COUNT(email),country,category,code from client where category='no' group by country  UNION select COUNT(email),country,category,code from client where category='yes' group by country  UNION select COUNT(email),country,category,code from client where category='undecided' group by country"  )
    List<Object> getCountryCount();



        @Query(value="from Client where (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client> findByFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc( String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where  (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc( String firstName,String lastName,String email);

        @Query(value="from Client where category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client> findByCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String category, String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String category,String firstName,String lastName,String email);


        @Query(value="from Client where country=:country AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client> findByCountryAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country, String category, String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where country=:country AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country, String category,String firstName,String lastName,String email);


    @Query(value="from Client where country=:country AND  (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client> findByCountryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country , String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where country=:country AND  (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country , String firstName,String lastName,String email);

    @Query("select DISTINCT country, code from Client")
    List<String> getAllCountries();

    //
    // List<Client> find

    //all verified not others

        @Query(value="from Client where verified=true AND  (Not publicFigure=:other)AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByVerifiedIsTrueAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String other,String firstName,String lastName,String email,Pageable pageable);
    @Query(value=" select count(email) from Client where verified=true AND  (Not publicFigure=:other)AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByVerifiedIsTrueAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String other,String firstName,String lastName,String email);

    //all not verified not others

    @Query(value="from Client where verified=false AND  (Not publicFigure=:other)AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByVerifiedIsFalseAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String other,String firstName,String lastName,String email,Pageable pageable);
    @Query(value=" select count(email) from Client where verified=false AND  (Not publicFigure=:other)AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByVerifiedIsFalseAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String other ,String firstName,String lastName,String email);


    //all particular public figure

        @Query(value="from Client where publicFigure=:publicFigure AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where publicFigure=:publicFigure AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String firstName,String lastName,String email);

    //all particular verified public figure

    @Query(value="from Client where publicFigure=:publicFigure AND verified=true AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByVerifiedIsTrueAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where publicFigure=:publicFigure AND verified=true AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByVerifiedIsTrueAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String firstName,String lastName,String email);

    //all particular not verified public figure

    @Query(value="from Client where publicFigure=:publicFigure AND verified=false AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByVerifiedIsFalseAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where publicFigure=:publicFigure AND verified=false AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByVerifiedIsFalseAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String firstName,String lastName,String email);


    // ____________________________________________________by category

        @Query(value="from Client where verified=true AND (NOT publicFigure=:other) AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByVerifiedIsTrueAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String other,String category,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where verified=true AND (NOT publicFigure=:other) AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByVerifiedIsTrueAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String other,String category,String firstName,String lastName,String email);

    //all not verified not others

    @Query(value="from Client where verified=false AND (NOT publicFigure=:other) AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByVerifiedIsFalseAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String other,String category,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where verified=false AND (NOT publicFigure=:other) AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByVerifiedIsFalseAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String other,String category,String firstName,String lastName,String email);


    //all particular public figure

    @Query(value="from Client where  publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String category,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where  publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String category,String firstName,String lastName,String email);

    //all particular verified public figure

    @Query(value="from Client where verified=true AND  publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByVerifiedIsTrueAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String category,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where verified=true AND  publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByVerifiedIsTrueAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String category,String firstName,String lastName,String email);

    //all particular not verified public figure

    @Query(value="from Client where verified=false AND  publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByVerifiedIsFalseAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String category,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where verified=false AND  publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByVerifiedIsFalseAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String publicFigure,String category,String firstName,String lastName,String email);




//######################################## Country


    //all verified not others

        @Query(value="from Client where country=:country AND verified=true AND (NOT publicFigure=:other) AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByCountryAndVerifiedIsTrueAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String other,String firstName,String lastName,String email,Pageable pageable);
   @Query(value="select count(email) from Client where country=:country AND verified=true AND (NOT publicFigure=:other)  AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndVerifiedIsTrueAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String other,String firstName,String lastName,String email);

    //all not verified not others

    //@Query(value="from Client where country=:country AND verified=false AND (NOT publicFigure=:other) AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByCountryAndVerifiedIsFalseAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String other,String firstName,String lastName,String email,Pageable pageable);
    //@Query(value="select  count(email) from Client where country=:country AND verified=false AND (NOT publicFigure=:other) AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndVerifiedIsFalseAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String other,String firstName,String lastName,String email);


    //all particular public figure

    @Query(value="from Client where country=:country  AND publicFigure=:publicFigure AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByCountryAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where country=:country  AND publicFigure=:publicFigure AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String firstName,String lastName,String email);

    //all particular verified public figure

    @Query(value="from Client where country=:country AND verified=true AND publicFigure=:publicFigure AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByCountryAndVerifiedIsTrueAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where country=:country AND verified=true AND publicFigure=:publicFigure AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndVerifiedIsTrueAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String firstName,String lastName,String email);

    //all particular not verified public figure

    @Query(value="from Client where country=:country AND verified=false AND publicFigure=:publicFigure AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByCountryAndVerifiedIsFalseAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where country=:country AND verified=false AND publicFigure=:publicFigure AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndVerifiedIsFalseAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String firstName,String lastName,String email);


    // ____________________________________________________by category

        @Query(value="from Client where country=:country AND verified=true AND (NOT publicFigure=:other)AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByCountryAndVerifiedIsTrueAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String other,String category,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where country=:country AND verified=true AND (NOT publicFigure=:other)AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndVerifiedIsTrueAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String other,String category,String firstName,String lastName,String email);

    //all not verified not others

    @Query(value="from Client where country=:country AND verified=false AND (NOT publicFigure=:other)AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByCountryAndVerifiedIsFalseAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String other,String category,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where country=:country AND verified=false AND (NOT publicFigure=:other)AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndVerifiedIsFalseAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String other,String category,String firstName,String lastName,String email);


    //all particular public figure

    @Query(value="from Client where country=:country  AND publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByCountryAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String category,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where country=:country  AND publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String category,String firstName,String lastName,String email);

    //all particular verified public figure

    @Query(value="from Client where country=:country AND verified=true AND  publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByCountryAndVerifiedIsTrueAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String category,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where country=:country AND verified=true AND  publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndVerifiedIsTrueAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String category,String firstName,String lastName,String email);

    //all particular not verified public figure

    @Query(value="from Client where country=:country AND verified=false AND  publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    List<Client>findByCountryAndVerifiedIsFalseAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String category,String firstName,String lastName,String email,Pageable pageable);
    @Query(value="select count(email) from Client where country=:country AND verified=false AND  publicFigure=:publicFigure AND category=:category AND (firstName like %:firstName% OR lastName like %:lastName% OR email like %:email%)")
    Integer countByCountryAndVerifiedIsFalseAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(String country,String publicFigure,String category,String firstName,String lastName,String email);



    List<Client> findByPublicFigureIsNotOrderByCreatedOnDesc(String other,Pageable pageable);
    Integer countByPublicFigureIsNotOrderByCreatedOnDesc(String other);

    List<Client> findByPublicFigureIsNotAndCountryOrderByCreatedOnDesc(String other,String country,Pageable pageable);
    Integer countByPublicFigureIsNotAndCountryOrderByCreatedOnDesc(String other,String country);



}
