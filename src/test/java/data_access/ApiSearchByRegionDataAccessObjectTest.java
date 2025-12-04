package data_access;

import entity.Country;

import java.util.List;

public class ApiSearchByRegionDataAccessObjectTest {

    public static void main(String[] args) {
        ApiSearchByRegionDataAccessObject dao = new ApiSearchByRegionDataAccessObject();

        List<Country> countries = dao.getAllCountries();

        System.out.println("Total countries fetched: " + countries.size());

        for (Country c : countries) {
            System.out.println(c);
        }
    }
}