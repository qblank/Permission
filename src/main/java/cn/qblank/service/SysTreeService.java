package cn.qblank.service;

import cn.qblank.dao.SysAclMapper;
import cn.qblank.dao.SysAclModuleMapper;
import cn.qblank.dao.SysDeptMapper;
import cn.qblank.dto.AclDto;
import cn.qblank.dto.AclModuleLevelDto;
import cn.qblank.dto.DeptLevelDto;
import cn.qblank.model.SysAcl;
import cn.qblank.model.SysAclModule;
import cn.qblank.model.SysDept;
import cn.qblank.util.LevelUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author evan_qb
 * @date 2018/8/8 17:31
 */
@Service
public class SysTreeService {
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;
    @Autowired
    private SysCoreService sysCoreService;
    @Autowired
    private SysAclMapper sysAclMapper;


    public List<AclModuleLevelDto> userAclTree(int userId){
        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
        List<SysAcl> allAclList = sysAclMapper.getAll();
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl : userAclList){
            AclDto dto = AclDto.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);


    }

    /**
     * 角色权限树
     * @param roleId
     * @return
     */
    public List<AclModuleLevelDto> roleTree(int roleId){
        //取出用户已经被分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentuserAclList();
        //取出角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        //用户权限集合
        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        //角色权限集合
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        //获取当前系统所有权限点
        List<SysAcl> allAclList = sysAclMapper.getAll();
        //将权限都存入set集合中
        Set<SysAcl> aclSet = new HashSet<>(allAclList);

        //将数据适配到dto中
        ArrayList<AclDto> aclDtoList = Lists.newArrayList();
        for(SysAcl acl: aclSet){
            AclDto dto = AclDto.adapt(acl);
            //判断用户是否有这个权限
            if (userAclIdSet.contains(acl.getId())){
                dto.setHasAcl(true);
            }
            //判断用户存在的权限是否为选中状态
            if (roleAclIdSet.contains(acl.getId())){
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    /**
     * 转换成树
     * @param aclDtoList
     * @return
     */
    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList){
        if (CollectionUtils.isEmpty(aclDtoList)){
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();
        Multimap<Integer,AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for(AclDto acl: aclDtoList){
            if (acl.getStatus() == 1){
                moduleIdAclMap.put(acl.getAclModuleId(),acl);
            }
        }
        bindAclsWithOrder(aclModuleLevelList,moduleIdAclMap);
        return aclModuleLevelList;
    }

    /**
     * 绑定排序的权限点
     * @param aclModuleLevelList
     * @param moduleIdAclMap
     */
    public void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList,Multimap<Integer,AclDto> moduleIdAclMap){
        if (CollectionUtils.isEmpty(aclModuleLevelList)){
            return;
        }
        for (AclModuleLevelDto dto : aclModuleLevelList){
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)){
                //排序
                Collections.sort(aclDtoList,aclSeqComparator);
                dto.setAclList(aclDtoList);
            }
            //将权限点放入到对应模块
            bindAclsWithOrder(dto.getAclModuleList(),moduleIdAclMap);
        }
    }

    /**
     * 准备好数据并适配成dto对象
     * @return
     */
    public List<AclModuleLevelDto> aclModuleTree(){
        List<SysAclModule> sysAclModuleList = sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> dtoList= Lists.newArrayList();
        for(SysAclModule aclModule: sysAclModuleList){
            dtoList.add(AclModuleLevelDto.adapt(aclModule));
        }
        return aclModuleListToTree(dtoList);
    }

    /**
     * 将数据转换成递归树
     * @param dtoList
     * @return
     */
    public List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList){
        if (CollectionUtils.isEmpty(dtoList)){
            return Lists.newArrayList();
        }

        Multimap<String,AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
        //创建一级目录
        List<AclModuleLevelDto> rootList = Lists.newArrayList();
        //遍历出一级目录并将其存入到rootList中
        for (AclModuleLevelDto dto: dtoList){
            levelAclModuleMap.put(dto.getLevel(),dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())){
                rootList.add(dto);
            }
        }
        Collections.sort(rootList,aclModuleSeqComparator);
        //递归生成树
        transformAclModule(rootList,LevelUtil.ROOT,levelAclModuleMap);
        return rootList;
    }

    public void transformAclModule(List<AclModuleLevelDto> aclModuleLevelList,String level,Multimap multimap){
        //获取每一层级的数据
        for (int i = 0; i < aclModuleLevelList.size(); i++) {
            AclModuleLevelDto dto = aclModuleLevelList.get(i);
            String nextLevel = LevelUtil.calculaterLevel(level, dto.getId());
            List<AclModuleLevelDto> tempAclModuleList = (List<AclModuleLevelDto>) multimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempAclModuleList)){
                //按照seq排序
                Collections.sort(tempAclModuleList,aclModuleSeqComparator);
                //设置下一层部门
                dto.setAclModuleList(tempAclModuleList);
                //继续递归，直至没有下一级元素
                transformAclModule(aclModuleLevelList,nextLevel,multimap);
            }

        }

    }

    /**
     * 生成dept树
     */
    public List<DeptLevelDto> deptTree(){
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        List<DeptLevelDto> dtoList = Lists.newArrayList();
        for(SysDept dept: deptList){
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }
        return deptListToTree(dtoList);
    }

    /**
     * 将List转换成Tree
     * @param deptLevelList 所有部门列表
     * @return
     */
    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelList){
        //判断查询到的数据是否为空
        if (CollectionUtils.isEmpty(deptLevelList)){
            return Lists.newArrayList();
        }
        //创建Multimap对象
        Multimap<String,DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        //创建第一级目录
        List<DeptLevelDto> rootList = Lists.newArrayList();
        //遍历每一级目录并将其存入MultiMap中
        for (DeptLevelDto dto: deptLevelList){
            levelDeptMap.put(dto.getLevel(),dto);
            //从中筛选出一级部门，并存入rootList中
            if (LevelUtil.ROOT.equals(dto.getLevel())){
                rootList.add(dto);
            }
        }
        //对root树进行排序  按照seq从小到大进行排序
        Collections.sort(rootList, deptSeqComparator);
        //递归生成树
        transformDeptTree(rootList,LevelUtil.ROOT,levelDeptMap);
        return rootList;
    }

    /**
     * 递归排序
     * level:0, 0, all 0->0.1,0.2
     * level:0.1
     * level:0.2
     * @param deptLevelList
     * @param level
     * @param levelDeptMap
     */
    public void transformDeptTree(List<DeptLevelDto> deptLevelList,
                                  String level,
                                  Multimap<String,DeptLevelDto> levelDeptMap){
        for (int i = 0;i < deptLevelList.size();i++){
            //获取该层的每个元素
            DeptLevelDto deptLevelDto = deptLevelList.get(i);
            //处理当前层级的数据，得到下一层级的级别
            String nextLevel = LevelUtil.calculaterLevel(level, deptLevelDto.getId());
            //处理下一层
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)){
                //排序
                Collections.sort(tempDeptList,deptSeqComparator);
                //设置下一层部门
                deptLevelDto.setDeptList(tempDeptList);
                //进入到下一层进行处理
                transformDeptTree(tempDeptList,nextLevel,levelDeptMap);
            }
        }
    }

    /**
     * 重写排序
     */
    public Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    /**
     * 将AclModuleLevelDto对象按照seq排序
     */
    public Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {
        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    public Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>() {
        @Override
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
