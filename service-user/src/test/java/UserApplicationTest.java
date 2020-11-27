import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.model.user.Permission;
import com.demo.common.model.user.RolePermission;
import com.demo.common.model.user.User;
import com.demo.common.model.user.UserRole;
import com.demo.user.UserServiceApplication;
import com.demo.user.mapper.PermissionMapper;
import com.demo.user.mapper.RolePermissionMapper;
import com.demo.user.mapper.UserMapper;
import com.demo.user.mapper.UserRoleMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.shardingsphere.api.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 在spring boot升级到2.4.0后，junit也随之升级到5.0，需要如下调整：<br/>
 * 1. @RunWith(SpringRunner.class)改为@ExtendWith(SpringExtension.class)；<br/>
 * 2. @Test改为引用org.junit.jupiter.api.Test；
 *
 * @Author thymi
 * @Date 2020/11/26
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserServiceApplication.class})
public class UserApplicationTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

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

    @Test
    public void list() {
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    public void save() {
        User user = new User();
        user.setUsername("tom");
        user.setPassword("$2a$10$TD99.i45lC04TgL8hdf2/ext/B0xEJHRIIwLduQql26XYXULT1TlW");
        user.setUserAge(28);
        userMapper.insert(user);
    }

    @Test
    public void update() {
        User user = new User();
        // 姓名条件
//        Map map = new HashMap();
//        map.put("name","jobs");
//        List list = userMapper.selectByMap(map);
//        user = (User) list.get(0);

        user = userMapper.selectById(3L);

        User userNew = new User();
        userNew.setId(user.getId());
        userNew.setPhone("18888888888");
        user.setRemark("备注信息");
        userNew.setCreated(LocalDateTime.now());
        userMapper.updateById(userNew);
    }

    @Test
    public void selectById() {
//        List list = new ArrayList();
//        list.add("1");
//        list.add("3");
//        List users = userMapper.selectBatchIds(list);
        List users = userMapper.selectBatchIds(Arrays.asList(1L, 3L));
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrapper() {
        // 构造条件
        QueryWrapper<User> userQueryWrapper = Wrappers.<User>query();
        // 指定返回的字段
//        userQueryWrapper.select("id","name","phone");
        // 重要: 排除指定字段
        userQueryWrapper.select(User.class, field -> !field.getColumn().equals("created")
                && !field.getColumn().equals("updated"));
        userQueryWrapper.likeRight("phone", "1");
//        userQueryWrapper.between("created","2020-06-01","2020-06-30");
        userQueryWrapper.isNotNull("phone");
        userQueryWrapper.apply("date_format(created,'%Y-%m-%d') = {0}", "2020-06-10");
        userQueryWrapper.in("id", Arrays.asList(1, 2, 3, 4, 5)).last("limit 1");
        userQueryWrapper.orderByDesc("created");
        List<User> users = userMapper.selectList(userQueryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByCondition() {
        String name = "thymi";
        String phone = "";
        QueryWrapper<User> userQueryWrapper = Wrappers.<User>query();
//        if (StringUtils.isNotEmpty(name)) userQueryWrapper.like("name", name);
//        if (StringUtils.isNotEmpty(phone)) userQueryWrapper.like("phone", phone);
        // 用以下方式取代上面的语句
        userQueryWrapper.like(StringUtils.isNotEmpty(name), "name", name).like(StringUtils.isNotEmpty(phone), "phone", phone);
        List<User> users = userMapper.selectList(userQueryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByEntity() {

        // 使用sharding-jdbc指定查询主数据库(master)
        HintManager.getInstance().setMasterRouteOnly();

        User user = new User();
        user.setUsername("thymi");
        user.setPhone("18");
        QueryWrapper<User> userQueryWrapper = Wrappers.<User>query(user);
        List<User> users = userMapper.selectList(userQueryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByAllEq() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "jobs");
        map.put("id", "3");
        map.put("email", null);

        QueryWrapper<User> userQueryWrapper = Wrappers.<User>query();
        // 过滤掉name条件
        userQueryWrapper.allEq((k, v) -> !k.equals("name"), map);
        List<User> users = userMapper.selectList(userQueryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByLambda() {
        QueryWrapper<User> userQueryWrapper = Wrappers.<User>query();
//        userQueryWrapper.likeRight("phone","1").and(qw -> qw.eq("name","thymi").or().eq("name","jobs"));
//        userQueryWrapper.isNotNull("phone").or(qw -> qw.likeRight("phone","1").isNotNull("name"));
        userQueryWrapper.nested(qw -> qw.eq("name", "thymi").or().eq("name", "jobs")).likeRight("phone", "1");
        List<User> users = userMapper.selectList(userQueryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * 使用场景, 如果字段比较多, 而需要返回的字段比较少, 使用selectList会返回所有你不需要的字段
     * 使用selectMaps可以只返回你需要的字段
     */
    @Test
    public void selectByMap() {
        QueryWrapper<User> userQueryWrapper = Wrappers.<User>query();
        userQueryWrapper.select("id", "name").nested(qw -> qw.eq("name", "thymi").or().eq("name", "jobs")).likeRight("phone", "1");

        List<Map<String, Object>> maps = userMapper.selectMaps(userQueryWrapper);
        maps.forEach(System.out::println);
    }

    @Test
    public void count() {
        QueryWrapper<User> userQueryWrapper = Wrappers.<User>query();
        userQueryWrapper.nested(qw -> qw.eq("name", "thymi").or().eq("name", "jobs")).likeRight("phone", "1");
        System.out.println(userMapper.selectCount(userQueryWrapper));
    }

    /**
     * 如果查询到多条记录会报错
     */
    @Test
    public void selectOne() {
        QueryWrapper<User> userQueryWrapper = Wrappers.<User>query();
        userQueryWrapper.eq("name", "jobs");
        User user = userMapper.selectOne(userQueryWrapper);
        System.out.println(user);
    }

    /**
     * Lambda条件查询的好处是不用担心字段写错
     */
    @Test
    public void selectLambda() {
//        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
//        userLambdaQueryWrapper.select(User::getId, User::getUsername, User::getPhone)
//                .like(User::getPhone, "1")
//                .notLike(User::getUsername,"test")
//                .ge(User::getCreated, "2020-06-01");
//        List<User> users = userMapper.selectList(userLambdaQueryWrapper);

        List<User> users = new LambdaQueryChainWrapper<User>(userMapper)
                .like(User::getPhone, "1")
                .notLike(User::getUsername, "test")
                .ge(User::getCreated, "2020-06-01")
                .list();
        users.forEach(System.out::println);
    }

    /**
     * 自定义sql
     */
    @Test
    public void customSql() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.select(User::getId, User::getUsername, User::getPhone)
                .like(User::getPhone, "1")
                .notLike(User::getUsername, "test")
                .ge(User::getCreated, "2020-06-01");
        List<User> users = userMapper.selectAll(userLambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * 分页查询
     */
    @Test
    public void selectByPage() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        // isSearchCount为false表示查总记录数, 在移动端场景通常不需要总记录数
        Page<User> userPage = new Page<>(1, 1, true);
        IPage<User> users = userMapper.selectPage(userPage, null);
        System.out.println("总页数: " + users.getPages());
        System.out.println("总记录数: " + users.getTotal());
        users.getRecords().forEach(System.out::println);
    }

    /**
     * 自定义分页
     */
    @Test
    public void selectMyPage() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        // isSearchCount为false表示查总记录数, 在移动端场景通常不需要总记录数
        Page<User> userPage = new Page<>(1, 1, true);
        // todo: 这里不能传分页插件???????, 使用的@select 不是xml
        IPage<User> users = userMapper.selectAllPage(userPage, userLambdaQueryWrapper);
        System.out.println("总页数: " + users.getPages());
        System.out.println("总记录数: " + users.getTotal());
        users.getRecords().forEach(System.out::println);
    }

    @Test
    public void updateById() {
        User user = new User();
//        user.setId(2L);
        user.setEmail("");

        int i = userMapper.updateById(user);
        System.out.println("影响记录数: " + i);
    }

    @Test
    public void updateByWrapper() {
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<User>();
        userLambdaUpdateWrapper.nested(luw -> luw.eq(User::getUsername, "sara")
                .or()
                .like(User::getPhone, "99"));
        User user = new User();
        user.setEmail("123345@126.com");
        int update = userMapper.update(user, userLambdaUpdateWrapper);
        System.out.println("影响记录数: " + update);
    }

    @Test
    public void updateByWrapper1() {
        User whereUser = new User();
        whereUser.setUsername("sara");
        // 用对象构造where条件,只能是eq
        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<User>(whereUser);
        // 如果更新字段少, 可以直接指定
        userLambdaUpdateWrapper.set(User::getEmail, "test@126.com");
        int update = userMapper.update(null, userLambdaUpdateWrapper);
        System.out.println("影响记录数: " + update);
    }

    /**
     * 基于乐观锁的更新
     * <p>
     * 乐观锁实现方式：
     * 1 取出记录时，获取当前version
     * 2 更新时，带上这个version
     * 3 执行更新时， set version = newVersion where version = oldVersion
     * 4 如果version不对，就更新失败
     * <p>
     * 仅支持 updateById(id) 与 update(entity, wrapper) 方法
     */
    @Test
    public void updateByVersion() {
        User whereUser = new User();
        whereUser.setUsername("sara");
        whereUser.setVersion(1);

        // 观察更新前的版本号
        QueryWrapper<User> userQueryWrapper = Wrappers.<User>query(whereUser);
        List<User> users = userMapper.selectList(userQueryWrapper);
        users.forEach(System.out::println);

//        LambdaUpdateWrapper<User> userLambdaUpdateWrapper = new LambdaUpdateWrapper<User>();
//        userLambdaUpdateWrapper.eq(User::getUsername, "sara");
        UpdateWrapper<User> userUpdateWrapper = Wrappers.update();
        userUpdateWrapper.eq("name", "sara");

        User user = new User();
        user.setEmail("sara_test@126.com");

        // 更新后, 版本号会自动加1
        int update = userMapper.update(user, userUpdateWrapper);

        System.out.println("影响记录数: " + update);

    }

    @Test
    public void deleteById() {
        int i = userMapper.deleteById(1277452194675240962L);
        System.out.println("影响记录数: " + i);
    }

    @Test
    public void deleteByIds() {
        int i = userMapper.deleteBatchIds(Arrays.asList(1271287421730295809L, 1271287695446495233L));
        System.out.println("影响记录数: " + i);
    }

    @Test
    public void deleteByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "ronane");

        int i = userMapper.deleteByMap(map);
        System.out.println("影响记录数: " + i);
    }

    @Test
    public void deleteByWrapper() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.select(User::getId, User::getUsername, User::getPhone)
                .like(User::getPhone, "1")
                .notLike(User::getUsername, "test")
                .ge(User::getCreated, "2020-06-01");
        int delete = userMapper.delete(userLambdaQueryWrapper);
        System.out.println("影响记录数: " + delete);
    }

//    @Test
//    public void getOne() {
////        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.lambdaQuery();
////        userLambdaQueryWrapper.select(User::getId, User::getUsername, User::getPhone)
////                .like(User::getPhone, "1");
//        QueryWrapper<User> userQueryWrapper = Wrappers.<User>query();
//        userQueryWrapper.like("name", "thymi");
//        User one = userService.getOne(userQueryWrapper);
//        System.out.println("查询结果: " + one);
//    }
//
//    @Test
//    public void logicDelete() {
//        QueryWrapper<User> userQueryWrapper = Wrappers.<User>query();
//        userQueryWrapper.like("name", "ronane");
//        boolean remove = userService.remove(userQueryWrapper);
//        System.out.println("删除结果: " + remove);
//    }

    /**
     * 获取用户权限, 使用自定义sql
     * 首次时间:30ms
     */
    @Test
    public void getPermission() {
        List<Permission> permissions = permissionMapper.selectByUserId(1L);
        permissions.forEach(System.out::println);
    }

    /**
     * 获取用户权限, 使用mapper(四次查询), 与上面的方法对比性能
     * 首次时间:15ms
     */
    @Test
    public void getUserPermission() {
        // 第一步: 得到用户角色id
        LambdaQueryWrapper<UserRole> userRoleLambdaQueryWrapper = Wrappers.lambdaQuery();
        userRoleLambdaQueryWrapper.eq(UserRole::getUserId, 1L);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleLambdaQueryWrapper);
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        // 第二步: 得到角色对应的权限id
        LambdaQueryWrapper<RolePermission> rolePermissionLambdaQueryWrapper = Wrappers.lambdaQuery();
        rolePermissionLambdaQueryWrapper.in(RolePermission::getRoleId, roleIds);
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(rolePermissionLambdaQueryWrapper);
        List<Long> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        // 第三步: 得到用户权限
        List<Permission> permissions = permissionMapper.selectBatchIds(permissionIds);
        permissions.forEach(System.out::println);
    }
}
