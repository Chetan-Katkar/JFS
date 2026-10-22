package ticket.booking.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.User;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class userBookingService {
    private User user;
    private List<User> usersList;
    private static final String USER_PATH = "../localDB/Users.json";

    private ObjectMapper OM = new ObjectMapper();

    public userBookingService(User user) throws IOException
    {
        this.user = user;
        File users = new File(USER_PATH);
        usersList = OM.readValue(users, new TypeReference<List<User>>() {
        });
    }
}
