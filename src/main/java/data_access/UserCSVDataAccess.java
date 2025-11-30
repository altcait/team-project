package data_access;

import entity.User;
import entity.UserFactory;
import use_case.login.LoginUserAccess;
import use_case.signup.SignUpUserAccess;

import java.io.*;
import java.util.*;

public class UserCSVDataAccess implements LoginUserAccess, SignUpUserAccess {

    private static final String HEADER = "username,password,countries";

    private final File userCSVFile;
    private final UserFactory userFactory;

    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> users = new HashMap<>();

    private String currentUsername;

    public UserCSVDataAccess(String csvPath, UserFactory userFactory) {
        this.userFactory = userFactory;
        this.userCSVFile = new File(csvPath);

        headers.put("username", 0);
        headers.put("password", 1);
        headers.put("countries", 2);

        try {
            if (!userCSVFile.exists()) {
                userCSVFile.createNewFile();
                writeHeader();
            }
            if (userCSVFile.length() == 0) {
                writeHeader();
            }
            loadFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeHeader() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userCSVFile))) {
            writer.write(HEADER);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(userCSVFile))) {
            String header = reader.readLine();
            if (!HEADER.equals(header)) {
                throw new RuntimeException("CSV header mismatch");
            }

            String row;
            while ((row = reader.readLine()) != null) {
                String[] cols = row.split(",", 3);

                String username = cols[headers.get("username")].trim();
                String password = cols[headers.get("password")].trim();
                String countriesStr = cols.length > 2 ? cols[2].trim() : "";

//                List<String> favourites = countriesStr.isEmpty()
//                        ? new ArrayList<>()
//                        : Arrays.asList(countriesStr.split(";"));

                User user = userFactory.create(username, password, new HashMap<>());
                users.put(username, user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userCSVFile))) {
            writer.write(HEADER);
            writer.newLine();

            for (User user : users.values()) {
//                String countriesJoined = String.join(";", user.getFavouriteCountries());

                writer.write(String.format("%s,%s",
                        user.getName(),
                        user.getPassword()
                        ));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
        saveAll();
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    @Override
    public String getCurrentUsername() {
        return this.currentUsername;
    }
}
