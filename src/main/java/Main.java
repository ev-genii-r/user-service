import com.innowise.rudkovskii.entity.User;
import com.innowise.rudkovskii.repository.UserRepository;
import com.innowise.rudkovskii.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.innowise.rudkovskii");
        UserService service = (UserService) applicationContext.getBean(UserService.class);
        User user = service.getById(1);
        System.out.println(user);
    }
}
