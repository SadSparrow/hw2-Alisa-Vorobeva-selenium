package org.sparrow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class RaiffeisenParameterizedTest extends AbstractRaiffeisenTest {

    @BeforeEach
    void beforeEach() {
        String baseUrl = "https://ipoteka.raiffeisen.ru/promo/refin-form";
        driver.get(baseUrl);
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void start(String fullName, String birthPlace, String birthDateEnter, String birthDateResult, String country
            , String foreignSeriese, String foreignNumber, String foreignIssuedDateEnter, String foreignIssuedDateResult
            , String foreignIssuedBy, String phoneEnter, String phoneResult, String registrationAddress) {


        //переключить слайдер "Являюсь гражданином РФ"
        WebElement isResidentSlider = driver.findElement(By.xpath(isResidentXpath));
        waitUtilElementToBeClickable(isResidentSlider);
        isResidentSlider.click();

        Assertions.assertEquals("Паспорт иностранного гражданина",
                driver.findElement(By.xpath("//h3[contains(text(), 'гражданина')]")).getText(),
                "Слайдер не переключился");

        //заполнить форму
        fillInputFieldDropDownByXpath(fullNameXpath, fullName);
        fillInputFieldByXpath(birthPlaceXpath, birthPlace);
        fillSlowInputMaskFieldByXpath(birthDateXpath, birthDateEnter, birthDateResult);

        WebElement genderMaleRadioBtn = driver.findElement(By.xpath(genderMaleXpath));
        waitUtilElementToBeClickable(genderMaleRadioBtn);
        genderMaleRadioBtn.click();

        WebElement citizenshipSelect = driver.findElement(By.xpath(citizenshipSelectXpath));
        citizenshipSelect.click();
        selectCountry(country);

        Assertions.assertEquals(country,
                driver.findElement(By.xpath(citizenshipSelectXpath)).getText(),
                "Страна не выбрана/выбрана некорректно");

        fillInputFieldByXpath(foreignSeriesXpath, foreignSeriese);
        fillInputFieldByXpath(foreignNumberXpath, foreignNumber);
        fillSlowInputMaskFieldByXpath(foreignIssuedDateXpath, foreignIssuedDateEnter, foreignIssuedDateResult);
        fillInputFieldByXpath(foreignIssuedByXpath, foreignIssuedBy);
        fillSlowInputMaskFieldByXpath(phoneXpath, phoneEnter, phoneResult);
        fillInputFieldDropDownByXpath(registrationAddressByXpath, registrationAddress);

        //нажать кнопку "продолжить"
        WebElement submitBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        submitBtn.click();

        //проверить, что уведомления об ошибке заполненных полей не отображаются
        Assertions.assertAll(
                () -> elementsXpathList.forEach(e -> checkErrorMessageAtField(e, ""))
        );
    }

    private static Stream<Arguments> getData() {
        DateTimeFormatter formatDot = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter formatText = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate now = LocalDate.now();
        LocalDate sixtyThreeTomorrow = now.minusYears(63).plusDays(1);
        LocalDate twentyOneToday = now.minusYears(21);

        return Stream.of(
                Arguments.of("Ли Ёну", "Дэгу", twentyOneToday.format(formatText), twentyOneToday.format(formatDot)//граничное значение 21 год
                        , "Корея, республика", "1", "1234", "06082021", "06.08.2021", "В"
                        , "0000000000", "+7 (000) 000-00-00", "г Москва, ул Белореченская, д 1"),
                Arguments.of("Петров Петр Петрович", "Ярославль", "10081982", "10.08.1982"
                        , "Словакия", "num", "12345", "15072015", "15.07.2015", "Выдан выдан"
                        , "1234567890", "+7 (123) 456-78-90", "г Архангельск, линия Чёрная Курья 1-я"),
                Arguments.of("Христорождественский Пантелеймон Пантелеймонович", "Лиллехаммер", sixtyThreeTomorrow.format(formatText), sixtyThreeTomorrow.format(formatDot)//граничное значение 63 года
                        , "Норвегия", "1234", "1234567890", "01011920", "01.01.1920", "выдан выдан выдан выдан выдан выдан выдан выдан выдан выдан выдан выдан выдан выдан выдан выданвыдан"
                        , "9999999999", "+7 (999) 999-99-99", "Владимирская обл, Суздальский р-н, деревня Пустой Ярославль, ул Зеленая, д 21")
                //паспорт выдан до рождения, уведомления об ошибке нет
        );
    }
}