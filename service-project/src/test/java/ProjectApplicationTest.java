import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.model.project.ProjectInfo;
import com.demo.project.ProjectServiceApplication;
import com.demo.project.mapper.ProjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * @Author thymi
 * @Date 2020/7/1
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ProjectServiceApplication.class})
public class ProjectApplicationTest {

    @Autowired
    ProjectMapper projectMapper;

    @Test
    public void save() {
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setName("厚街万达广场");
        projectInfo.setSimName("厚街万达");
        int insert = projectMapper.insert(projectInfo);
        System.out.println("项目保存结果: " + insert);
    }

    @Test
    public void select() {
        LambdaQueryWrapper<ProjectInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select(ProjectInfo::getId, ProjectInfo::getName, ProjectInfo::getSimName)
                .like(ProjectInfo::getName, "南沙");
        List<ProjectInfo> projectInfos = projectMapper.selectList(lambdaQueryWrapper);
        projectInfos.forEach(System.out::println);
    }

    @Test
    public void selectPage() {
        Page<ProjectInfo> page = new Page<>(1,2,true);
        Page<ProjectInfo> page1 = projectMapper.selectPage(page, null);
        System.out.println(page1.getTotal());
        System.out.println(page1.getPages());
    }

    @Test
    public void delete() {
        int i = projectMapper.deleteById(2L);
        System.out.println("删除结果: " + i);
    }
}
