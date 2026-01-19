package com.parking.parkinglot.ejb;

import com.parking.parkinglot.common.CarDto;
import com.parking.parkinglot.entities.Car;
import com.parking.parkinglot.entities.Country;
import jakarta.ejb.EJBException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.logging.Logger;

public class CountryBean {

    private static final Logger LOG = Logger.getLogger(CountryBean.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public List<Country> findAllCountries(){
        LOG.info("findAllCountries");
        try {
            TypedQuery<Country> typedQuery = entityManager.createQuery("SELECT c FROM Country c", Country.class);
            List<Country> countries = typedQuery.getResultList();
            return countries;
        }catch(Exception e){
            throw new EJBException(e);
        }
    }
}
