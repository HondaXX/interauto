package com.honda.interauto.tools.httpTool;

import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.InnerResPojo;
import com.honda.interauto.pojo.ReqPojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestTool {
    private static Logger logger = LogManager.getLogger(RequestTool.class);

    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        if (attr != null) {
            return attr.getRequest();
        }else{
            return null;
        }
    }

    public static InnerResPojo jugeReqPojo(ReqPojo rp, List<String> serverList){
        InnerResPojo irp = new InnerResPojo();
        String serverStr = rp.getServerId();

        if (serverStr.equals(null) || serverStr.equals("")){
            logger.info("serverId字段为空");
            irp.setInnerCode(BaseError.NULL_ERROR);
            irp.setInnerDesc(BaseError.NULL_ERROR_DESC);
            return irp;
        }else if(rp.getRequestBody().equals(" ")){
            logger.info("requestBody为空字符串");
            irp.setInnerCode(BaseError.NULL_ERROR);
            irp.setInnerDesc(BaseError.NULL_ERROR_DESC);
            return irp;
        }
        else if (!(rp.getRequestBody() instanceof Map)){
            logger.info("requestBody不是一个map格式的数据");
            irp.setInnerCode((BaseError.FOMAT_ERROR));
            irp.setInnerDesc(BaseError.FOMAT_ERROR_DESC);
            return irp;
        }else if(!(serverList.contains(serverStr))){
            logger.info("没找到匹配的serverId");
            irp.setInnerCode(BaseError.SERVERID_NOT_FOUND);
            irp.setInnerDesc(BaseError.SERVERID_NOT_FOUND_DESC);
            return irp;
        }
        else {
            return null;
        }
    }
}
