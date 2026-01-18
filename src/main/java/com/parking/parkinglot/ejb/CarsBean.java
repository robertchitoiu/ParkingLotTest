package com.parking.parkinglot.ejb;

import com.parking.parkinglot.common.CarDto;
import com.parking.parkinglot.common.CarPhotoDto;
import com.parking.parkinglot.entities.Car;
import com.parking.parkinglot.entities.CarPhoto;
import com.parking.parkinglot.entities.User;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class CarsBean {

    private static final Logger LOG = Logger.getLogger(CarsBean.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    UsersBean usersBean;

    public List<CarDto> findAllCars(){
        LOG.info("findAllCars");
        try {
            TypedQuery<Car> typedQuery = entityManager.createQuery("SELECT c FROM Car c", Car.class);
            List<Car> cars = typedQuery.getResultList();
            return copyCarsToDto(cars);
        }catch(Exception e){
            throw new EJBException(e);
        }
    }


    public void create(String licensePlate, String parkingSpot, Long ownerId) {
        LOG.info("create car");

        Car car = new Car();
        car.setLicensePlate(licensePlate);
        car.setParkingSpot(parkingSpot);

        User owner = usersBean.findUser(ownerId);
        owner.setId(ownerId);
        owner.getCars().add(car);
        car.setOwner(owner);

        entityManager.persist(car);
    }

    public List<CarDto> copyCarsToDto(Collection<Car> cars) {
        if (cars == null || cars.isEmpty()) {
            return new ArrayList<CarDto>();
        }
        return cars.stream().map(car -> new CarDto(car.getId(), car.getLicensePlate(), car.getParkingSpot(), car.getOwner().getUsername())).toList();
    }

    public CarDto findById(Long carId) {
        if (carId == null) {
            return null;
        }
        Car car = entityManager.find(Car.class, carId);
        CarDto dto = null;
        if (car != null) {
            dto = new CarDto(car.getId(), car.getLicensePlate(), car.getParkingSpot(), car.getOwner().getUsername());
        } else {
            dto = new CarDto();
        }
        return dto;
    }

    public void update(Long carId, String licensePlate, String parkingSpot, Long ownerId) {
        LOG.info("update");
        if (carId == null) {
            return;
        }
        User owner = usersBean.findUser(ownerId);

        Car car = entityManager.find(Car.class, carId);
        User oldOwner = car.getOwner();

        car.setLicensePlate(licensePlate);
        car.setParkingSpot(parkingSpot);
        if(!owner.equals(oldOwner)){
            car.setOwner(owner);
            owner.getCars().add(car);
            oldOwner.getCars().remove(car);
        }
        entityManager.merge(car);
    }

    public void deleteById(Collection<Long> carIds) {
        if (carIds == null || carIds.isEmpty()) {
            return;
        }
        Car c;
        for (Long carId : carIds) {
            c = entityManager.find(Car.class, carId);
            if (c != null) {
                entityManager.remove(c);
            }
        }

    }

    public void addPhotoToCar(Long carId, String filename, String fileType, byte[] fileContent) {
        LOG.info("addPhotoToCar");
        CarPhoto photo = new CarPhoto();
        photo.setFilename(filename);
        photo.setFileType(fileType);
        photo.setFileContent(fileContent);

        Car car = entityManager.find(Car.class, carId);
        if (car.getPhoto() != null) {
            entityManager.remove(car.getPhoto());
        }
        car.setPhoto(photo);

        photo.setCar(car);
        entityManager.persist(photo);
    }

    public CarPhotoDto findPhotoByCarId(Integer carId) {
        List<CarPhoto> photos = entityManager
                .createQuery("SELECT p FROM CarPhoto p where p.car.id = :id", CarPhoto.class)
                .setParameter("id", carId)
                .getResultList();
        if (photos.isEmpty()) {
            return null;
        }
        CarPhoto photo = photos.get(0); // the first element
        return new CarPhotoDto(photo.getId(), photo.getFilename(), photo.getFileType(), photo.getFileContent());
    }

}
