package com.honda.interauto.controllers;

import com.honda.interauto.entity.*;
import com.honda.interauto.pojo.BaseConfigs;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.*;
import com.honda.interauto.tools.appiumTool.ElementTool;
import com.honda.interauto.tools.sysTool.OtherTool;
import com.honda.interauto.tools.sysTool.TypeChangeTool;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/Appium")
public class AppiumCtrl {
    private final Logger logger = LogManager.getLogger(AppiumCtrl.class);

    @Autowired
    private PhoneInfoService phoneInfoService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private EvenOperateService evenOperateService;
    @Autowired
    private BaseConfigs baseConfigs;
    @Autowired
    private CaseResDetailService caseResDetailService;
    @Autowired
    private CaseResOverViewService caseResOverViewService;

    @PostMapping(value = "/InitDeviceApp.json", produces = "application/json;charset=UTF-8" )
    public ResPojo initDevice(@RequestBody ReqPojo reqPojo){
        Integer deviceId = Integer.parseInt(reqPojo.getRequestBody().get("deviceId").toString());
        Integer appId = Integer.parseInt(reqPojo.getRequestBody().get("appId").toString());
        List<Integer> evenList = (List<Integer>) reqPojo.getRequestBody().get("evenList");

        PhoneInfoEntity phoneInfo = phoneInfoService.getPhoneById(deviceId);
        AppInfoEntity appInfo = appInfoService.getAppById(appId);
        //基础属性设置
        DesiredCapabilities desCap = new DesiredCapabilities();
        if (Integer.parseInt(String.valueOf(phoneInfo.getPlatformVersion().charAt(0))) >= 6){
            desCap.setCapability("automationName", "UiAutomator2");
        }
        else {
            desCap.setCapability("automationName", "Appium");
        }
        desCap.setCapability("newCommandTimeout", "30");
        desCap.setCapability("sessionOverride", true);
        desCap.setCapability("noReset", true);
        desCap.setCapability("noSign", true);
        //设置中文输入
        desCap.setCapability("unicodeKeyboard", true);
        desCap.setCapability("resetKeyboard", true);
        //设备信息设置
        desCap.setCapability("platformName", phoneInfo.getPlatformName());
        desCap.setCapability("deviceName", phoneInfo.getDeviceName());
        desCap.setCapability("platformVersion", phoneInfo.getPlatformVersion());
        //app信息设置
        desCap.setCapability("appPackage", appInfo.getAppPackage());
        desCap.setCapability("appActivity", appInfo.getAppActivity());
        desCap.setCapability("appWaitActivity", appInfo.getAppWaitActivity());

        AndroidDriver<MobileElement> driver =null;
        Activity mainActivity = new Activity(appInfo.getAppPackage(), appInfo.getAppActivity());
        WebDriverWait waitor = null;
        try{
            driver = new AndroidDriver<MobileElement>(new URL(baseConfigs.getAppiumServerHost()), desCap);
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            if (driver != null){
                logger.info("========>初始化appium服务成功");
            }
            //等待一个事务，多少秒没完成便退出
            waitor = new WebDriverWait(driver, 2);
        }catch (MalformedURLException e){
            logger.info("========>appium服务连接设备失败");
            e.printStackTrace();
            ResPojo res = new ResPojo();
            res.setErrorCode(BaseError.APPIUM_INIT_FAILED);
            res.setErrorDesc(BaseError.APPIUM_INIT_FAILED_DESC);
            return res;
        }

        Map<Integer, String> evenResMap = new HashMap<Integer, String>();
        //用例数
        Integer totalCount = 0;
        int failCount = 0;
        int successCount = 0;
        //跑用例的动作id
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = simpleDateFormat.format(date);
        String runTagId = caseResDetailService.runApiTagId(date);

        for (Integer evenId : evenList){
            totalCount += 1;
            //设置每个用例回到app起始页
            driver.startActivity(mainActivity);

            logger.info("========>开始事件,id为{}", evenId);
            List<EvenOperateEntity> operateList = evenOperateService.getEvenByEvenId(evenId);
            if (operateList == null || operateList.size() == 0){
                logger.info("========>id为{}的事件不存在或事件未添加步骤", evenId);
                CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, null, evenId, "1", BaseError.EVEN_NOT_FOUND, BaseError.EVEN_NOT_FOUND_DESC, "事件不存在或未设置事件步骤");
                caseResDetailService.saveCaseRes(caseResDetailEntity);
                evenResMap.put(evenId, BaseError.EVEN_NOT_FOUND_DESC);
                failCount += 1;
                continue;
            }
            //操作事件里的所有步骤
            for (EvenOperateEntity evenOperate : operateList){
                //sort为0的时结果比较，直接跳过
                if (evenOperate.getSort() == 0){
                    continue;
                }
                Integer sortId = evenOperate.getSort();
                logger.info("========>执行事件中步骤id:{}", sortId);
                WebElement ele = ElementTool.eleIsExist(driver, evenOperate.getEleMethod(), evenOperate.getEleIdentify(), waitor);
                if (ele == null){
                    logger.info("========>步骤id为{}的元素找不到, 跳过该事件", sortId);
                    CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, null, evenId, "1", BaseError.ELE_NOTFOUND, BaseError.ELE_NOTFOUND_DESC, "事件步骤id元素不存在:" + sortId);
                    caseResDetailService.saveCaseRes(caseResDetailEntity);
                    evenResMap.put(evenId, sortId + "->" + BaseError.ELE_NOTFOUND_DESC);
                    ElementTool.getScreenShot(driver, baseConfigs.getAppiumScreenFile());
                    failCount += 1;
                    break;
                }

                //定义元素操作是否出错，出错直接跳过
                boolean operateRes = true;

                switch (evenOperate.getEleOperate()){
                    case 1:
                        ele.click();
                        break;
                    case 2:
                        ele.sendKeys(evenOperate.getEleText());
                        break;
                    case 3:
                        ele.getText();
                        break;
                    case 4:
                        for (int k = 0; k < 6; k++){
                            if (null == ElementTool.eleIsExist(driver, 12, evenOperate.getEleText(), waitor)){
//                                waitor.until(ExpectedConditions.presenceOfElementLocated(By.xpath(evenOperate.getEleText()))).equals(null)
                                ElementTool.slipModule(driver, ele, evenOperate.getSlipDerection());
                                continue;
                            }else{
                                WebElement eleScroll = ElementTool.eleIsExist(driver, 12, evenOperate.getEleText(), waitor);
                                eleScroll.click();
                                break;
                            }
                        }
                        logger.info("未找到滑动后等待的元素,步骤id:{},跳过该事件", sortId);
                        CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, null, evenId, "1", BaseError.ELE_NOTFOUND, BaseError.ELE_NOTFOUND_DESC, "滑动后应出现元素没出现:" + sortId);
                        caseResDetailService.saveCaseRes(caseResDetailEntity);
                        evenResMap.put(evenId, sortId + "->滑动-" + BaseError.ELE_NOTFOUND_DESC);
                        ElementTool.getScreenShot(driver, baseConfigs.getAppiumScreenFile());
                        operateRes = false;
                        break;
                    case 5:
                        boolean switchStatus = evenOperate.getEleText().equals(String.valueOf(0)) ? false : true;
//                        logger.info("---->" + ele.getAttribute("checked") + "---->" + switchStatus);
                        if (ele.getAttribute("checked").equals(switchStatus)){
                            break;
                        }
                        ele.click();
                        break;
                    case 6:
                        ElementTool.longPress(driver, ele);
                        break;
                }

                if (operateRes == false) {
                    failCount += 1;
                    break;
                }

            }
            //获得事件的结果对象
            logger.info("==>事件id{}开始对比结果", evenId);
            EvenOperateEntity evenRes = operateList.stream().filter(evenOperate -> evenOperate.getSort() == 0).collect(Collectors.toList()).get(0);
            WebElement eleRes = ElementTool.eleIsExist(driver, evenRes.getEleMethod(), evenRes.getEleIdentify(), waitor);

            if (evenRes.getEleOperate() == 3){
                String exceptRes = evenRes.getEleText();
                if (!(eleRes.getText().equals(exceptRes))){
                    logger.info("========>事件id:" + evenId + "-->测试未通过, 预期结果:" + evenRes.getEleText() + "&实际结果:" + eleRes.getText());
                    CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, null, evenId, "1", BaseError.ELE_RES_NOTMATCH, BaseError.ELE_RES_NOTMATCH_DESC, "预期结果:" + evenRes.getEleText() + "&实际结果:" + eleRes.getText());
                    caseResDetailService.saveCaseRes(caseResDetailEntity);
                    evenResMap.put(evenId, BaseError.ELE_RES_NOTMATCH_DESC);
                    ElementTool.getScreenShot(driver, baseConfigs.getAppiumScreenFile());
                    failCount += 1;
                    continue;
                }
                logger.info("========>事件id:{}-->测试通过", evenId);
                CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, null, evenId, "0", BaseError.CASE_OK, "success", "事件通过");
                caseResDetailService.saveCaseRes(caseResDetailEntity);
                successCount += 1;
                evenResMap.put(evenId, BaseError.CASE_OK);
                continue;
            }else if (evenRes.getEleOperate() == 1){
                eleRes.click();
                logger.info("========>事件id:{}-->测试通过", evenId);
                CaseResDetailEntity caseResDetailEntity = new CaseResDetailEntity(null, runTagId, null, evenId, "0", BaseError.CASE_OK, "success", "事件通过");
                caseResDetailService.saveCaseRes(caseResDetailEntity);
                successCount += 1;
                evenResMap.put(evenId, BaseError.CASE_OK);
                continue;
            }

        }

        //总记录
        String userName = OtherTool.splitStr(runTagId, "-")[0];
        String endTime = simpleDateFormat.format(new Date());
        logger.info("========>统计批次【{}】事件概览,共{}个事件,成功-{},失败-{}", runTagId, totalCount, successCount, failCount);
        CaseResOverViewEntity caseResOverViewEntity = new CaseResOverViewEntity(runTagId, null, appId, totalCount, failCount, successCount, userName, startTime, endTime);
        caseResOverViewService.recordOverView(caseResOverViewEntity);

        driver.closeApp();
        driver.quit();

        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.put("res", evenResMap);
        return res;
    }

    @PostMapping(value = "/newPhone.json", produces = "application/json;charset=UTF-8")
    public ResPojo newPhone(@RequestBody ReqPojo reqPojo){
        PhoneInfoEntity phoneInfoEntity = new PhoneInfoEntity();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        phoneInfoEntity.setPhoneName(reqPojo.getRequestBody().get("phoneName").toString());
        phoneInfoEntity.setPlatformName(reqPojo.getRequestBody().get("platformName").toString());
        phoneInfoEntity.setDeviceName(reqPojo.getRequestBody().get("deviceName").toString());
        phoneInfoEntity.setPlatformVersion(reqPojo.getRequestBody().get("platformVersion").toString());
        phoneInfoEntity.setCreator(reqPojo.getRequestBody().get("creator").toString());
        phoneInfoEntity.setCreateTime(sdf.format(date));
        phoneInfoEntity.setUpdataTime(sdf.format(date));

        if (phoneInfoService.newPhone(phoneInfoEntity) != 1){
            logger.info("========>保存手机信息失败：{}", phoneInfoEntity.getPhoneName());
            ResPojo res = new ResPojo();
            res.setErrorCode(BaseError.PHONE_NEW_FAILED);
            res.setErrorDesc(BaseError.PHONE_NEW_FAILED_DESC);
            return res;
        }
        logger.info("========>保存手机信息成功：{}", phoneInfoEntity.getPhoneName());
        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("res", "new Phone success");
        return res;
    }

    @PostMapping(value = "/getAllPhones.json", produces = "application/json;charset=UTF-8")
    public ResPojo getAllPhones(@RequestBody ReqPojo reqPojo){
        List<PhoneInfoEntity> phoneList = phoneInfoService.getAllPhones();
        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.put("phones", phoneList);
        return res;
    }

    @PostMapping(value = "/getPhoneById.json", produces = "application/json;charset=UTF-8")
    public ResPojo getPhoneById(@RequestBody ReqPojo reqPojo){
        Integer deviceId = Integer.parseInt(reqPojo.getRequestBody().get("id").toString());
        PhoneInfoEntity phoneInfo = phoneInfoService.getPhoneById(deviceId);
        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.put("phone", phoneInfo);
        return res;
    }

    @PostMapping(value = "/newApp.json", produces = "application/json;charset=UTF-8")
    public ResPojo newApp(@RequestBody ReqPojo reqPojo){
        AppInfoEntity appInfo = new AppInfoEntity();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        appInfo.setAppPackage(reqPojo.getRequestBody().get("appPackage").toString());
        appInfo.setAppActivity(reqPojo.getRequestBody().get("appActivity").toString());
        appInfo.setAppWaitActivity(reqPojo.getRequestBody().get("appWaitActivity").toString());
        appInfo.setCreator(reqPojo.getRequestBody().get("creator").toString());
        appInfo.setAppDes(reqPojo.getRequestBody().get("appDes").toString());
        appInfo.setCreateTime(sdf.format(date));
        appInfo.setUpdataTime(sdf.format(date));

        if (appInfoService.newApp(appInfo) != 1){
            logger.info("========>保存app信息失败：{}", appInfo.getAppPackage());
            ResPojo res = new ResPojo();
            res.setErrorCode(BaseError.APP_NEW_FAILED);
            res.setErrorDesc(BaseError.APP_NEW_FAILED_DESC);
            return res;
        }
        logger.info("========>保存app信息成功：{}", appInfo.getAppPackage());
        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.putData("res", "new appInfo success");
        return res;
    }

    @PostMapping(value = "/getAllApps.json", produces = "application/json;charset=UTF-8")
    public ResPojo getAllApps(@RequestBody ReqPojo reqPojo){
        List<AppInfoEntity> appList = appInfoService.getAllApps();
        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.put("app", appList);
        return res;
    }

    @PostMapping(value = "/getAppById.json", produces = "application/json;charset=UTF-8")
    public ResPojo getAppById(@RequestBody ReqPojo reqPojo){
        Integer appId = Integer.parseInt(reqPojo.getRequestBody().get("id").toString());
        AppInfoEntity appInfo = appInfoService.getAppById(appId);
        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.put("app", appInfo);
        return res;
    }

    public static void main(String[] args){

//        String astr = "4.4.4";
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < astr.length(); i++){
//            if (Character.isDigit(astr.charAt(i))){
//                sb.append(astr.charAt(i));
//            }
//        }
//        String newStr = sb.toString();
//        System.out.println(newStr);

        DesiredCapabilities desCap = new DesiredCapabilities();

        desCap.setCapability("automationName", "Appium");
        desCap.setCapability("deviceName", "7145da7f");
        desCap.setCapability("platformName", "Android");
        desCap.setCapability("platformVersion", "4.4.4");

        desCap.setCapability("appPackage", "com.miui.calculator");//com.miui.calculator  com.miui.weather2
        desCap.setCapability("appActivity", "cal.CalculatorActivity");//CalculatorActivity  ActivityWeatherMain
        desCap.setCapability("newCommandTimeout", "30");
        desCap.setCapability("appWaitActivity", "cal.CalculatorActivity");//
        desCap.setCapability("sessionOverride", true);
//		cap.setCapability("unicodeKeyboard", true);
//		cap.setCapability("resetKeyboard", false);


        try{
            AndroidDriver<MobileElement> driver = new AndroidDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), desCap);
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            TypeChangeTool.sysSleep(2);

            WebElement element1 = driver.findElement(By.xpath("//android.widget.TextView[@resource-id='com.miui.calculator:id/btn_']"));
            TypeChangeTool.sysSleep(2);
            element1.click();
            WebElement elementJia = driver.findElement(By.xpath("//android.widget.ImageView[@resource-id='com.miui.calculator:id/btn_plus_s']"));
            TypeChangeTool.sysSleep(2);
            elementJia.click();
            WebElement element5 = driver.findElement(By.xpath("//android.widget.TextView[@resource-id='com.miui.calculator:id/btn_5_s']"));
            TypeChangeTool.sysSleep(2);
            element5.click();
            WebElement elementDY = driver.findElement(By.xpath("//android.widget.ImageView[@resource-id='com.miui.calculator:id/btn_equal_s']"));
            TypeChangeTool.sysSleep(2);
            elementDY.click();

            //通过寻找父子兄弟关系获得元素，
            //通过父子关系获得,元素位置直接是列表下的儿子位置(若看list的index得加一)
//            String JGStrXPath = "//android.widget.TextView[@text='=']/parent::android.widget.LinearLayout/android.widget.TextView[2]";
            //通过兄弟关系获得(直接数相邻多少个，相邻1，隔一个2)
            String JGStrXPath = "//android.widget.TextView[@text='=']/following-sibling::android.widget.TextView[1]";
            WebElement elementJG = driver.findElement(By.xpath(JGStrXPath));
            System.out.println("====>" + elementJG.getText());

            //通过UiSelector选择对应的类型
//            List<MobileElement> selList = driver.findElementsByAndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\")");
//            selList.forEach(selEle -> System.out.println("===>" + selEle.getText()));
            //通过class选择对应的类型
//            List<MobileElement> eleList = driver.findElementsByClassName("android.widget.TextView");
//            eleList.forEach(webEle -> System.out.println("===>" + webEle.getText()));




            driver.closeApp();
            driver.quit();
        }catch (MalformedURLException e){
            System.out.println("========>服务连接设备失败");
            e.printStackTrace();
        }
    }


}
