package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utility.Helper;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.Properties;

import static utility.ConstantsUtil.LONG_DEFAULT_WAIT;
import static utility.ConstantsUtil.MEDIUM_DEFAULT_WAIT;
import static utility.SetGetVariables.setEpochTime;

public class BaseTest {

    public static ExtentReports report;
    public static ExtentTest extentlogger;
    public static Properties prop;
    public static String merchantId;
    public static String businessFlow;
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    public static WebDriver driver;
    private ChromeOptions co;
    private FirefoxOptions fo;
    private EdgeOptions eo;
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

    private WebDriver getDriver() {

        return tlDriver.get();
    }

    @Parameters({"browser", "APIExecution", "x-merchant-id"})
    @BeforeSuite
    public void setUp(String browser, String apiExecution, String apiMerchantId) {
        ExtentSparkReporter extent = new ExtentSparkReporter(new File(System.getProperty("user.dir") + "/Reports/TestResult" + Helper.getCurrentDateTime() + ".html"));
        // ExtentHtmlReporter extent=new ExtentHtmlReporter(new File(System.getProperty("user.dir")+"/Reports/TestResult"+ Helper.getCurrentDateTime()+".html"));
        report = new ExtentReports();
        report.attachReporter(extent);
        extent.config().setReportName("Automation Test Execution Report");
        report.setSystemInfo("Environment","Staging Environment");
        report.setSystemInfo("Author", "Mayur Bilgoji");
        extent.config().setDocumentTitle("TEST RESULT");
        setEpochTime(Helper.getTodaysEpochTime());
        prop = Helper.init_properties();
        if (apiExecution.equalsIgnoreCase("TRUE")) {
            // prop = Helper.init_properties();
            logger.info("\n============================Starting the Test Execution======================");
            if (apiMerchantId.equalsIgnoreCase("AJIOB2B")) {
                merchantId = prop.getProperty("ajio-merchant-id");
                logger.info("merchant id " + merchantId);
                businessFlow = prop.getProperty("ajio-business-flow");
                logger.info("business flow " + businessFlow);

            } else if (apiMerchantId.equalsIgnoreCase("INSTOCKFIN")) {
                merchantId = prop.getProperty("instock-merchant-id");
                logger.info("merchant id " + merchantId);
                businessFlow = prop.getProperty("instock-business-flow");
                logger.info("business flow " + businessFlow);
            } else if (apiMerchantId.equalsIgnoreCase("TIRABEAUTY")) {
                merchantId = prop.getProperty("tira-merchant-id");
                logger.info("merchant id " + merchantId);
                businessFlow = prop.getProperty("tira-business-flow");
                logger.info("business flow " + businessFlow);
            }
            else if (apiMerchantId.equalsIgnoreCase("RESQ")) {
                merchantId = prop.getProperty("resq-merchant-id");
                logger.info("merchant id " + merchantId);
                businessFlow = prop.getProperty("resq-business-flow");
                logger.info("business flow " + businessFlow);
            }
        }
    }

    @Parameters({"browser","APIExecution"})
    @BeforeClass
    public void browserSetUp(String browser, String apiExecution) {
        if(!apiExecution.equalsIgnoreCase("True"))
        {
            logger.warn("Make sure JioMart Cart is empty before running the suite");
            if (browser.equalsIgnoreCase("chrome")) {
                co = new ChromeOptions();

            /*Map<String, String> mobileEmulation = new HashMap<String, String>();
            mobileEmulation.put("deviceName", "Pixel 5");*/

                if (Boolean.parseBoolean(prop.getProperty("headless"))) {
                    co.addArguments("--headless");
               /* co.addArguments("--auto-open-devtools-for-tabs");
                co.setExperimentalOption("mobileEmulation", mobileEmulation);*/
                    System.out.println("Test Scripts will run in Chrome headless mode.");

                } else if (Boolean.parseBoolean(prop.getProperty("incognito"))) {
                    co.addArguments("--incognito");
                /*co.addArguments("--auto-open-devtools-for-tabs");
                co.setExperimentalOption("mobileEmulation", mobileEmulation);*/
                    System.out.println("Test Scripts will run in Chrome incognito mode.");

                } else {
                    co = new ChromeOptions();
                    System.out.println("Test Scripts will run in Chrome default mode.");

                }
                driver = new ChromeDriver(co);
               // tlDriver.set( new ChromeDriver(co));
                ;


            } else if (browser.equalsIgnoreCase("firefox")) {
                fo = new FirefoxOptions();

                if (Boolean.parseBoolean(prop.getProperty("headless"))) {
                    fo.addArguments("--headless");
                    System.out.println("Test Scripts will run in Firefox headless mode.");

                } else if (Boolean.parseBoolean(prop.getProperty("incognito"))) {
                    fo.addArguments("--incognito");
                    System.out.println("Test Scripts will run in Firefox incognito mode.");

                } else {
                    fo = new FirefoxOptions();
                    System.out.println("Test Scripts will run in Firefox default mode.");

                }
                driver = new FirefoxDriver(fo);
                //tlDriver.set(new FirefoxDriver(fo));


            } else if (browser.equalsIgnoreCase("Edge")) {
                eo = new EdgeOptions();

                if (Boolean.parseBoolean(prop.getProperty("headless"))) {
                    eo.addArguments("--headless");
                    System.out.println("Test Scripts will run in Edge headless mode.");

                } else if (Boolean.parseBoolean(prop.getProperty("incognito"))) {
                    eo.addArguments("--incognito");
                    System.out.println("Test Scripts will run in Edge incognito mode.");

                } else {
                    eo = new EdgeOptions();
                    System.out.println("Test Scripts will run in Edge default mode.");

                }
                driver = new EdgeDriver(eo);
                //tlDriver.set(new EdgeDriver(eo));


            } else if (browser.equalsIgnoreCase("Safari")) {
                driver = new SafariDriver();
                ;
            }
            //driver=getDriver();
            driver.manage().deleteAllCookies();
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(MEDIUM_DEFAULT_WAIT));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(LONG_DEFAULT_WAIT));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(MEDIUM_DEFAULT_WAIT));
            logger.warn("Make sure JioMart Cart is empty before running the suite");
        }
        else
        {
            logger.info("Running API tests");
        }
    }

    @Parameters("APIExecution")
    @AfterClass
    public void quitBrowser(String apiExecution)
    {
        if(!apiExecution.equalsIgnoreCase("TRUE"))
        {
            driver.quit();
        }
    }


    @AfterMethod
    public void tearDown(ITestResult result) throws IOException
    {
        if(result.getStatus()==ITestResult.FAILURE)
        {
            extentlogger.fail("Test Failed => "+result.getThrowable());
            Throwable t=result.getThrowable();
            StringWriter writer=new StringWriter();
            t.printStackTrace(new PrintWriter(writer));
            logger.error(writer.toString());
          //  String description=result.getMethod().getDescription();
            logger.info(result.getMethod().getDescription()+"==> Test Failed");

        }
        else if(result.getStatus()==ITestResult.SUCCESS)
        {
            extentlogger.pass(result.getMethod().getMethodName()+"Test Passed");
            logger.info(result.getMethod().getMethodName()+"==> Test Passed");
            logger.info(result.getMethod().getDescription()+"==> Test Passed");
        }
        else if(result.getStatus()==ITestResult.SKIP)
        {
            extentlogger.pass(result.getMethod().getMethodName()+"Test Skipped");
            logger.info(result.getMethod().getMethodName()+"==> Test Skipped");
            logger.info(result.getMethod().getDescription()+"==> Test Skipped");
        }

        extentlogger.assignCategory("Overall");//This is to add tags section in extent report
        report.flush();
    }

    @AfterSuite
    public void endExecution()
    {
        logger.info("\n=======================Test Suite Execution Completed================================ ");
    }
}
