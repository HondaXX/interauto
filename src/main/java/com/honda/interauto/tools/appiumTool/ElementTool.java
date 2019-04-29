package com.honda.interauto.tools.appiumTool;

import com.honda.interauto.entity.EvenOperateEntity;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class ElementTool {
    private final static Logger logger = LogManager.getLogger(ElementTool.class);

    private final static int TYPE_RESOURCE = 11;
    private final static int TYPE_XPATH = 12;
    private final static int TYPE_CLASSNAME = 13;
    private final static int TYPE_TOAST = 14;

    private final static Integer LEFT_TO_RIGHT = 1;
    private final static Integer RIGHT_TO_RIGHT = 2;
    private final static Integer UP_TO_DOWN = 3;
    private final static Integer DOWN_TO_UP = 4;

    public static WebElement eleIsExist(AppiumDriver<MobileElement> driver, Integer eleType, String eleInfo, WebDriverWait waitor){
        WebElement ele = null;
        switch (eleType){
            case TYPE_RESOURCE:
                try{
                    if (driver.findElement(By.id(eleInfo)) != null){
                        ele = driver.findElement(By.id(eleInfo));
                        return ele;
                    }
                }catch (NoSuchElementException e){
                    logger.info("====>找不到元素:{}", eleInfo);
//                    e.printStackTrace();
                    return null;
                }
            case TYPE_XPATH:
                try{
                    if (driver.findElement(By.xpath(eleInfo)) != null){
                        ele = driver.findElement(By.xpath(eleInfo));
                        return ele;
                    }
                }catch (NoSuchElementException e){
                    logger.info("====>找不到元素:{}", eleInfo);
//                    e.printStackTrace();
                    return null;
                }
            case TYPE_CLASSNAME:
                try{
                    if (driver.findElement(By.className(eleInfo)) != null){
                        ele = driver.findElement(By.className(eleInfo));
                        return ele;
                    }
                }catch (NoSuchElementException e){
                    logger.info("====>找不到元素:{}", eleInfo);
//                    e.printStackTrace();
                    return null;
                }
            case TYPE_TOAST:
                try{
                    if (waitor.until(ExpectedConditions.presenceOfElementLocated(By.xpath(eleInfo))) != null){
                        logger.info("==>处理toast弹窗...");
                        ele = driver.findElement(By.xpath(eleInfo));
                        logger.info("========>弹窗context:{}", ele.getText());
                        return ele;
                    }
                }catch (NoSuchElementException e){
                    logger.info("====>找不到元素:{}", eleInfo);
//                    e.printStackTrace();
                    return null;
                }
        }
        return null;
    }


    public static void slipScreen(AndroidDriver<MobileElement> driver, Integer direction){
        int width = driver.manage().window().getSize().getWidth();
        int height = driver.manage().window().getSize().getHeight();

        switch (direction){
            case 1:  //向右
                TouchAction rightswipe = new TouchAction(driver).press(width/10, height/2).waitAction(Duration.ofSeconds(2))
                                                                .moveTo(9*width/10, height/2).release();
                rightswipe.perform();
                break;
            case 2:  //向左
                TouchAction leftswipe = new TouchAction(driver).press(9*width/10, height/2).waitAction(Duration.ofSeconds(1))
                                                               .moveTo(width/10, height/2).release();
                leftswipe.perform();
                break;
            case 3:  //向下
                TouchAction downswipe = new TouchAction(driver).press(width/2, height/8).waitAction(Duration.ofSeconds(2))
                                                               .moveTo(width/2, 7*height/8).release();
                downswipe.perform();
                break;
            case 4:  //向上
                TouchAction upswipe = new TouchAction(driver).press(width/2, 7*height/8).waitAction(Duration.ofSeconds(2))
                                                             .moveTo(width/2, height/8).release();
                upswipe.perform();
                break;
        }
    }

    public static void slipModule(AndroidDriver<MobileElement> driver, WebElement element, Integer direction){
        Point left_top = element.getLocation();
        Dimension diSize = element.getSize();
        int startX = left_top.getX();
        int startY = left_top.getY();
        int widthMod = diSize.getWidth();
        int heightMod = diSize.getHeight();
        logger.info("==>ModelInfo-->x:{}, y:{}, width:{}, height:{}", startX, startY, widthMod, heightMod);


        switch (direction){
            case 1:  //向右
                TouchAction rightswipe = new TouchAction(driver).press(startX + widthMod/10, startY + heightMod/2).waitAction(Duration.ofSeconds(2))
                                                                .moveTo(startX + 9*widthMod/10, startY + heightMod/2).release();
                rightswipe.perform();
                break;
            case 2:  //向左
                TouchAction leftswipe = new TouchAction(driver).press(startX + 9*widthMod/10, startY + heightMod/2).waitAction(Duration.ofSeconds(2))
                                                               .moveTo(startX + widthMod/10, startY + heightMod/2).release();
                leftswipe.perform();
                break;
            case 3:  //向下
                TouchAction downswipe = new TouchAction(driver).press(startX + widthMod/2, startY + heightMod/4).waitAction(Duration.ofSeconds(2))
                                                               .moveTo(startX + widthMod/2, startY + 3*heightMod/4).release();
                downswipe.perform();
                break;
            case 4:  //向上
                TouchAction upswipe = new TouchAction(driver).press(startX + widthMod/2, startY + 3*heightMod/4).waitAction(Duration.ofSeconds(2))
                                                             .moveTo(startX + widthMod/2, startY + heightMod/4).release();
                upswipe.perform();
                break;
        }
    }

    public static void longPress(AndroidDriver<MobileElement> driver, WebElement element){
        TouchAction ta = new TouchAction(driver);
        logger.info("==>长按元素...");
        ta.longPress(element).waitAction(Duration.ofSeconds(2));
        ta.perform();
    }

    public static void getScreenShot(AndroidDriver<MobileElement> driver, String basePath){
        SimpleDateFormat sdfD = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfT = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String dirPath = basePath + sdfD.format(date);
        String fileName = sdfT.format(date);
        try {
            File file = new File(dirPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File imageFile = driver.getScreenshotAs(OutputType.FILE);
            String filePath = dirPath + File.separator + fileName +".jpg";
            FileUtils.copyFile(imageFile, new File(filePath));
            logger.info("==>保存图片至{}", filePath);
        }catch (Exception e){
            logger.info("截图保存错误");
            e.printStackTrace();
        }
    }
}
