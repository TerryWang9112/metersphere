package io.metersphere.service;

import io.metersphere.base.domain.Project;
import io.metersphere.base.domain.ProjectExample;
import io.metersphere.base.mapper.ProjectMapper;
import io.metersphere.commons.exception.MSException;
import io.metersphere.user.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectService {
    @Resource
    private ProjectMapper projectMapper;

    public Project addProject(Project project) {
        if (StringUtils.isBlank(project.getName())) {
            MSException.throwException("Project name cannot be null");
        }
        ProjectExample example = new ProjectExample();
        example.createCriteria()
                .andWorkspaceIdEqualTo(SessionUtils.getCurrentWorkspaceId())
                .andNameEqualTo(project.getName());
        if (projectMapper.countByExample(example) > 0) {
            MSException.throwException("The project name already exists");
        }
        project.setId(UUID.randomUUID().toString());
        long createTime = System.currentTimeMillis();
        project.setCreateTime(createTime);
        project.setUpdateTime(createTime);
        // set workspace id
        project.setWorkspaceId(SessionUtils.getCurrentWorkspaceId());
        projectMapper.insertSelective(project);
        return project;
    }

    public List<Project> getProjectList() {
        // todo 查询条件设置
        return projectMapper.selectByExample(null);
    }

    public void deleteProject(String projectId) {
        projectMapper.deleteByPrimaryKey(projectId);
    }

    public void updateProject(Project project) {
        project.setCreateTime(null);// 创建时间禁止修改
        project.setUpdateTime(System.currentTimeMillis());
        projectMapper.updateByPrimaryKeySelective(project);
    }

    public List<Project> listAll() {
        return projectMapper.selectByExample(null);
    }
}
