package com.bloducspauter.intercept.controller;

import com.bloducspauter.base.dto.QueryFacilityInformationParamsDto;
import com.bloducspauter.base.dto.ResultDto;
import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.enums.CommonError;
import com.bloducspauter.base.page.PageData;
import com.bloducspauter.base.page.PageParams;
import com.bloducspauter.base.sort.SortData;
import com.bloducspauter.base.sort.SortParams;
import com.bloducspauter.intercept.service.FacilityInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * 控制层
 *
 * @author Bloduc Spauter
 */
//@RestController=@Controller+@RequestBody
@Api(value = "HttpServletRequest请求测试")
@RestController
@RequestMapping("/intercept")
@Slf4j
public class CountRequestController {

    @Resource
    FacilityInformationService facilityInformationService;


    @PostMapping("/forbid")
    @ApiOperation(value = "对或者IP进行封禁操作")
    public ResultDto forbidAnEntities(QueryFacilityInformationParamsDto params) {
        String ip = params.getIp();
        int status = params.getStatus();
        FacilityInformation facilityInformation = facilityInformationService.findById(ip);
        if (facilityInformation == null) {
            return new ResultDto(HttpStatus.NOT_FOUND.value(), CommonError.QUERY_NULL.getErrMessage(), null);
        }
        facilityInformation.setStatus(1);
        facilityInformationService.update(facilityInformation);
        return new ResultDto(HttpStatus.OK.value(), "操作成功", null);
    }

    @GetMapping("/query_page")
    @ApiOperation("分页展示所有请求对象信息")
    public PageData<FacilityInformation> queryFacilityInfo(PageParams pageParams) {
        try {
            return facilityInformationService.selectPage(pageParams);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }

    @GetMapping("/sort_page")
    @ApiOperation("展示前几名的对象")
    public SortData<FacilityInformation> sortData(SortParams sortParams) {
        try {
            return facilityInformationService.sortData(sortParams);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }

    @ApiOperation("统计到目前为止被封禁的设备数量")
    @GetMapping("/blacklist")
    public ResultDto showBlacklist() {
        int count = facilityInformationService.getForbiddenEntitiesCount();
        return new ResultDto(HttpStatus.OK.value(), "查询成功", count);
    }
}
