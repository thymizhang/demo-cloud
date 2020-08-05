import com.demo.user.UserServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author thymi
 * @Date 2020/6/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserServiceApplication.class})
public class ShardingTest {

    /**
     * 加载默认数据源, 测试是否被sharding-jdbc接管
     */
    @Resource
    private DataSource dataSource;

    @Test
    public void testDataSource() {
        String sql = "select * from tb_user";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                System.out.println(rs.toString());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
