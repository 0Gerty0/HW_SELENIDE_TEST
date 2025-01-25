import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static java.time.Duration.ofSeconds;

public class CardDeliveryTest {

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "http://localhost:9999"; // Устанавливаем базовый URL для тестов
        Configuration.browser = "chrome"; // Указываем браузер
        Configuration.browserSize = "1920x1080"; // Устанавливаем размер окна браузера
    }

    @Test
    void shouldSubmitRequestSuccessfully() {
        // Планируемая дата встречи через 3 дня от текущей
        String planningDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // Открываем главную страницу приложения
        open("/");

        // Заполняем поле "Город"
        $("[data-test-id=city] input").setValue("Москва");

        // Заполняем поле "Дата"
        $("[data-test-id=date] input").doubleClick().sendKeys(planningDate);

        // Заполняем поле "ФИО"
        $("[data-test-id=name] input").setValue("Иван Иванов");

        // Заполняем поле "Телефон"
        $("[data-test-id=phone] input").setValue("+79270000000");

        // Ставим галочку в чекбоксе "Согласие на обработку данных"
        $("[data-test-id=agreement]").click();

        // Нажимаем кнопку "Забронировать"
        $$("button").find(exactText("Забронировать")).click();

        // Проверяем, что появилось сообщение об успешной отправке заявки
        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible, ofSeconds(15)) // Уведомление должно быть видно в течение 15 секунд
                .shouldHave(text("Встреча успешно забронирована на " + planningDate)); // Проверяем текст сообщения
    }
}

