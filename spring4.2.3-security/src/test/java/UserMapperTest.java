import com.security.dao.UserDao;
import com.security.pojo.Permission;
import com.security.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class UserMapperTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testFindByUsername(){
        User user = userDao.findByUsername("admin");
        System.out.println(user);
    }

    @Test
    public void testFindPermissionByUsername(){
        List<Permission> list = userDao.findPermissionByUsername("admin");
        for (Permission permission : list){
            System.out.println(permission.getName() + ":" + permission.getTag());
        }
    }
}
