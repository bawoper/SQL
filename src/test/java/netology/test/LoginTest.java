package netology.test;

import netology.data.DataHelper;
import netology.data.SQLHelper;
import netology.page.LoginPage;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.open;
import static netology.data.SQLHelper.cleanAuthCodes;
import static netology.data.SQLHelper.cleanDatabase;

public class LoginTest {
    LoginPage loginPage;
    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfoWithTestData();

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }
    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }
    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }
    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from sut test data")
    void shouldSuccessfulLogin() {
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }
    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void shouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.login(authInfo);
        loginPage.verifyErrorNotification("Ошибка! Неверно указан логин или пароль");
    }
    @Test
    @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
    void shouldGetErrorNotificationIfLoginWithExistUserAndRandomVerificationCode() {
       var verificationPage = loginPage.validLogin(authInfo);
       var verificationCode = DataHelper.generateRandomVerificationCode();
       verificationPage.verify(verificationCode.getCode());
       verificationPage.verifyErrorNotification("Ошибка! Неверно указан код! Попробуйте ещё раз.");

    }

}
