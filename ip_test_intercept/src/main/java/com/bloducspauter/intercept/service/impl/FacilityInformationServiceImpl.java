package com.bloducspauter.intercept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bloducspauter.base.page.PageData;
import com.bloducspauter.base.page.PageParams;
import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.po.TotalRejectedPo;
import com.bloducspauter.base.sort.SortData;
import com.bloducspauter.base.sort.SortParams;
import com.bloducspauter.intercept.mapper.FacilityInformationMapper;
import com.bloducspauter.intercept.service.FacilityInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;
/**
 * @author Bloduc Spauter
 *
 */
@Slf4j
@Service
public class FacilityInformationServiceImpl implements FacilityInformationService {

    @Resource
    private FacilityInformationMapper requestEntityMapper;

    @Override
    @Transactional
    public int insert(FacilityInformation requestFacilityInformation) {
        int id = requestFacilityInformation.getId();
        log.info("id:{}", id);
        FacilityInformation selectedEntity = findById(id);
        //判断是否存在，没有作添加否则做更新
        if (selectedEntity != null) {
            log.info("Entity {} is already exists, updating...", requestFacilityInformation.getId());
            int total = selectedEntity.getTotalRequest() + requestFacilityInformation.getTotalRequest();
            int rejected = selectedEntity.getRejectedRequest() + requestFacilityInformation.getRejectedRequest();
            requestFacilityInformation.setTotalRequest(total);
            requestFacilityInformation.setRejectedRequest(rejected);
            return update(requestFacilityInformation);
        } else {
            return requestEntityMapper.insert(requestFacilityInformation);
        }
    }

    @Override
    public int update(FacilityInformation requestFacilityInformation) {
        return requestEntityMapper.updateById(requestFacilityInformation);
    }

    @Override
    public int delete(FacilityInformation requestFacilityInformation) {
        return requestEntityMapper.deleteById(requestFacilityInformation.getId());
    }

    @Override
    public FacilityInformation findById(int id) {
        return requestEntityMapper.selectById(id);
    }

    @Override
    public List<FacilityInformation> findAll() {
        return requestEntityMapper.selectList(null);
    }

    @Override
    public PageData<FacilityInformation> selectPage(PageParams pageParams) {
        QueryWrapper<FacilityInformation> queryWrapper = new QueryWrapper<>();
        long pageNo = pageParams.getPageOn();
        long pageSize = pageParams.getPageSize();
        Page<FacilityInformation> informationPage = new Page<>(pageNo, pageSize);
        Page<FacilityInformation> pageResult = requestEntityMapper.selectPage(informationPage, queryWrapper);
        List<FacilityInformation> facilityInformation = pageResult.getRecords();
        long total = pageResult.getTotal();
        return new PageData<>(total, pageSize, facilityInformation);
    }

    @Override
    public SortData<FacilityInformation> sortData(SortParams sortParams) {
        QueryWrapper<FacilityInformation> queryWrapper = new QueryWrapper<>();
        boolean isSort = sortParams.isSort();
        String sortBy = sortParams.getSortBy();
        queryWrapper.orderBy(true, isSort, sortBy);
        queryWrapper.last("limit " + sortParams.getLimit());
        List<FacilityInformation> facilityInformation = requestEntityMapper.selectList(queryWrapper);
        return new SortData<>(sortBy, facilityInformation);
    }

    @Override
    public int isBanAnIp(Integer id, Integer status) {
        FacilityInformation facilityInformation = requestEntityMapper.selectById(id);
        if (facilityInformation == null) {
            return 404;
        }
        facilityInformation.setStatus(status);
        return update(facilityInformation);
    }

    @Override
    public List<Object> getForbiddenEntities() {
        QueryWrapper<FacilityInformation>facilityInformationQueryWrapper=new QueryWrapper<>();
        facilityInformationQueryWrapper.eq("status",1);
        facilityInformationQueryWrapper.select("id");
        return requestEntityMapper.selectObjs(facilityInformationQueryWrapper);
    }

}
