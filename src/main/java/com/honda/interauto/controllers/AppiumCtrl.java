package com.honda.interauto.controllers;

import com.honda.interauto.entity.AppInfoEntity;
import com.honda.interauto.entity.PhoneInfoEntity;
import com.honda.interauto.pojo.BaseConfigs;
import com.honda.interauto.pojo.BaseError;
import com.honda.interauto.pojo.ReqPojo;
import com.honda.interauto.pojo.ResPojo;
import com.honda.interauto.services.AppInfoService;
import com.honda.interauto.services.PhoneInfoService;
import com.honda.interauto.tools.sysTool.TypeChangeTool;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/Appium")
public class AppiumCtrl {
    private final Logger logger = LogManager.getLogger(AppiumCtrl.class);

    @Autowired
    private PhoneInfoService phoneInfoService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private BaseConfigs baseConfigs;

    @PostMapping(value = "/InitDeviceApp.json", produces = "application/json;charset=UTF-8" )
    public ResPojo initDevice(@RequestBody ReqPojo reqPojo){
        Integer deviceId = Integer.parseInt(reqPojo.getRequestBody().get("deviceId").toString());
        Integer appId = Integer.parseInt(reqPojo.getRequestBody().get("appId").toString());

        PhoneInfoEntity phoneInfo = phoneInfoService.getPhoneById(deviceId);
        AppInfoEntity appInfo = appInfoService.getAppById(appId);
        //基础属性设置
        DesiredCapabilities desCap = new DesiredCapabilities();
        desCap.setCapability("automationName", "Appium");
        desCap.setCapability("newCommandTimeout", "30");
        desCap.setCapability("sessionOverride", true);
//		cap.setCapability("unicodeKeyboard", true);
//		cap.setCapability("resetKeyboard", false);
        //设备信息设置
        desCap.setCapability("platformName", phoneInfo.getPlatformName());
        desCap.setCapability("deviceName", phoneInfo.getDeviceName());
        desCap.setCapability("platformVersion", phoneInfo.getPlatformVersion());
        //app信息设置
        desCap.setCapability("appPackage", appInfo.getAppPackage());
        desCap.setCapability("appActivity", appInfo.getAppActivity());
        desCap.setCapability("appWaitActivity", appInfo.getAppWaitActivity());

        AndroidDriver<MobileElement> driver =null;
        try{
            driver = new AndroidDriver<MobileElement>(new URL(baseConfigs.getAppiumServerHost()), desCap);
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            TypeChangeTool.sysSleep(2);
            if (driver != null){
                logger.info("========>初始化appium服务成功");
            }
        }catch (MalformedURLException e){
            logger.info("========>appium服务连接设备失败");
            e.printStackTrace();
            ResPojo res = new ResPojo();
            res.setErrorCode(BaseError.APPIUM_INIT_FAILED);
            res.setErrorDesc(BaseError.APPIUM_INIT_FAILED_DESC);
            return res;
        }

        WebElement element1 = driver.findElement(By.xpath("//android.widget.TextView[@resource-id='com.miui.calculator:id/btn_1_s']"));
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

        driver.closeApp();
        driver.quit();

        ResPojo res = new ResPojo();
        res.setResCode(BaseError.RESPONSE_OK);
        res.put("res", "success");
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
        DesiredCapabilities desCap = new DesiredCapabilities();

//        desCap.setCapability("automationName", "Appium");
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

            WebElement element1 = driver.findElement(By.xpath("//android.widget.TextView[@resource-id='com.miui.calculator:id/btn_1_s']"));
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

            TouchAction ta = new TouchAction(driver);
            System.out.println("====>长按截图开始");
            ta.longPress(elementJG).waitAction(Duration.ofSeconds(2));
            ta.perform();

            String basePath = "D:\\work\\idea\\temp\\file\\interauto\\Screenshot\\";
            SimpleDateFormat sdfD = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdfT = new SimpleDateFormat("HHmmss");
            Date date = new Date();
            String dirPath = basePath + sdfD.format(date);
            String fileName = sdfT.format(date);
            try {
                File file = new File(dirPath);
                if (!file.exists()) {
                    System.out.println("创建不存在文件夹");
                    file.mkdirs();
                }
                File imageFile = driver.getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(imageFile, new File(dirPath + File.separator + fileName +".jpg"));
            }catch (Exception e){
                System.out.println("截图保存错误");
                e.printStackTrace();
            }
            driver.closeApp();
            driver.quit();
        }catch (MalformedURLException e){
            System.out.println("========>服务连接设备失败");
            e.printStackTrace();
        }
    }
}
