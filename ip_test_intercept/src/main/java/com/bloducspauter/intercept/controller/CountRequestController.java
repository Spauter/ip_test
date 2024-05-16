package com.bloducspauter.intercept.controller;


import com.bloducspauter.base.dto.QueryFacilityInformationParamsDto;
import com.bloducspauter.base.dto.ResultDto;
import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.enums.CommonError;
import com.bloducspauter.base.page.PageData;
import com.bloducspauter.base.page.PageParams;
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
import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/ban")
    @ApiOperation(value = "对某个设备或者IP进行封禁操作")
    public ResultDto isBanAnIp(QueryFacilityInformationParamsDto params) {
        Integer id=params.getId();
        int status=params.getStatus();
        FacilityInformation facilityInformation = facilityInformationService.findById(id);
        if (facilityInformation == null) {
            return new ResultDto(HttpStatus.NOT_FOUND.value(),CommonError.QUERY_NULL.getErrMessage(),null);
        }
        facilityInformation.setStatus(status);
        facilityInformationService.update(facilityInformation);
        return new ResultDto(HttpStatus.OK.value(),"操作成功",null);
    }

    @GetMapping("/query_page")
    @ApiOperation("分页展示所有请求对象信息")
    public PageData<FacilityInformation> queryFacilityInfo(PageParams pageParams) {
        Map<String, Object> map = new HashMap<>();
        try {
            return facilityInformationService.selectPage(pageParams);
        } catch (Exception e) {
            return null;
        }
    }
}
