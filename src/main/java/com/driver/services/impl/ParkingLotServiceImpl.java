package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot(name, address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot = new Spot();
        // setting parking lot by given id
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        spot.setParkingLot(parkingLot);

      // setting spot type with given no. of wheels
        if(numberOfWheels<3){
            spot.setSpotType(SpotType.TWO_WHEELER);
        }
        else if(numberOfWheels<5){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }else{
            spot.setSpotType(SpotType.OTHERS);
        }

        // setting price for per houre
        spot.setPricePerHour(pricePerHour);

        // adding spot to list
        parkingLot.getSpotList().add(spot);

        // save the parking lot
        parkingLotRepository1.save(parkingLot);
        return spot;

    }

    @Override
    public void deleteSpot(int spotId) {
        if(spotRepository1.findById(spotId).isPresent()){
            Spot spot = spotRepository1.findById(spotId).get();
            ParkingLot parkingLot = spot.getParkingLot();
            List<Spot> spotList= parkingLot.getSpotList();
            for (Spot s : spotList) {
                if(s == spot)
                    spotRepository1.delete(s);

            }
        spotRepository1.deleteById(spotId);
        }


    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        if(parkingLotRepository1.findById(parkingLotId).isPresent()){
            ParkingLot parkingLot =  parkingLotRepository1.findById(parkingLotId).get();
            Spot requiredSpot = null;

            for(Spot spot : parkingLot.getSpotList()){
                if(spot.getId() == spotId){
                    spot.setPricePerHour(pricePerHour);
                    requiredSpot = spot;
                }
            }
            spotRepository1.save(requiredSpot);
            parkingLotRepository1.save(parkingLot);
            return requiredSpot;
        }
        else{
            return null;
        }
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
