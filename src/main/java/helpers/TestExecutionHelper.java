package helpers;

import models.AndroidDevice;
import models.Configs;
import models.IosDevice;
import models.TestData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class TestExecutionHelper extends BaseHelper {
    public TestExecutionHelper(Configs configs) {
        this.configs = configs;
    }

    private final Configs configs;

    public TestData buildTestData(TestData testData){
        testData = buildTestMobileNumber(testData);
        testData = buildTestEmailPassData(testData);
        testData = buildPaymentTestData(testData);

        return testData;
    }

    public TestData buildTestEmailPassData(TestData testData){
        testData.setTestEmail(configs.getTestEmail().replace("@", "+"
                + getCurrentTimeStamp("yyyyMMdd_HHmmss")
                + "_" + new Random().nextInt(1000000)
                + "_" + testData.getMobileNumber() + "@"));
        testData.setTestPassword(configs.getTestPassword());

        return testData;
    }

    public TestData buildTestMobileNumber(TestData testData){
        String mobileNumber = String.valueOf(configs.getTestMobileNumber());
        mobileNumber = switch (configs.getTestMobileCompany()) {
            case "Etisalat" -> mobileNumber.concat("1");
            case "Vodafone" -> mobileNumber.concat("0");
            case "Orange" -> mobileNumber.concat("2");
            default -> mobileNumber.concat(String.valueOf(new Random().nextInt(3)));
        };
        mobileNumber = mobileNumber.concat(String.valueOf(new Random().nextInt(90000000) + 10000000));
        testData.setMobileNumber(mobileNumber);
        return testData;
    }

    public TestData buildPaymentTestData(TestData testData){
        testData.setTestCreditCard(configs.getTestCreditCard());
        testData.setTestExpiryDate(configs.getTestExpDate());
        testData.setTestCVC(configs.getTestCVC());
        testData.setSecondaryTestCreditCard(configs.getSecondaryTestCreditCard());
        testData.setSecondaryTestExpiryDate(configs.getSecondaryTestExpDate());
        testData.setSecondaryTestCVC(configs.getSecondaryTestCVC());

        return testData;
    }

    public String getCurrentTimeStamp(String format){
        return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
    }

    public AndroidDevice buildAndroidDeviceData(AndroidDevice androidDevice){
        androidDevice.setPlatformName(configs.getAndroidPlatformName());
        androidDevice.setName(configs.getAndroidDeviceName());
        androidDevice.setPortNumber(String.valueOf(generateRandomPortNumber()));
        androidDevice.setAdbName("emulator-" + androidDevice.getPortNumber());
        androidDevice.setOsVersion(configs.getAndroidPlatformVersion());
        androidDevice.setAutomationName(configs.getAndroidAutomationName());

        return androidDevice;
    }

    public IosDevice buildIosDeviceData(IosDevice iosDevice){
        iosDevice.setPlatformName(configs.getIosPlatformName());
        iosDevice.setDeviceName(configs.getiOSDeviceName());
        iosDevice.setDeviceUdid(configs.getIosUDID());
        iosDevice.setOsVersion(configs.getIosPlatformVersion());
        iosDevice.setAutomationName(configs.getIosAutomationName());

        return iosDevice;
    }
}
