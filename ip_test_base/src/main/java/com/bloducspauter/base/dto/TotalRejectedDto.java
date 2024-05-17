package com.bloducspauter.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Bloduc Spauter
 *
 */
@ApiModel("被拒绝的访问数量")
@Data
public class TotalRejectedDto {
    @ApiModelProperty("被拒绝的IP数量")
    private int totalRejected;
}
