package org.sparrow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RaiffeisenTest extends AbstractRaiffeisenTest {

    @BeforeEach
    void beforeEach() {
        String baseUrl = "https://www.raiffeisen.ru";
        driver.get(baseUrl);
    }

    @Test
    public void start() {

        //закрыть всплывающее окно "Ваш город Москва?"
        String popUpRegionCloseXpath = "//div[contains(@class, 'informer__close')]";
        WebElement popUpRegionBtnClose = driver.findElement(By.xpath(popUpRegionCloseXpath));
        waitUtilElementToBeClickable(popUpRegionBtnClose);
        popUpRegionBtnClose.click();

        //выбрать пункт меню «Ипотека»
        String mortgageMenuXPath = "//a[@class='main-menu__link' and contains(text(), 'Ипотека')]";
        WebElement mortgageMenuBtn = driver.findElement(By.xpath(mortgageMenuXPath));
        waitUtilElementToBeClickable(mortgageMenuBtn);
        mortgageMenuBtn.click();

        //выбрать подпункт меню «Рефинансирование»
        String refinancingSubMenuXpath = "//div[@id='menu2']//a[@class='menu-link main-menu__link' and contains(text(), 'Рефинансирование')]";
        WebElement refinancingSubMenuBtn = driver.findElement(By.xpath(refinancingSubMenuXpath));
        waitUtilElementToBeClickable(refinancingSubMenuBtn);
        refinancingSubMenuBtn.click();

        //нажать кнопку «Оставить заявку»
        String checkoutBtnXpath = "//div[@class='b-intro__block-buttons']/a[@class='button']";
        WebElement checkoutBtn = driver.findElement(By.xpath(checkoutBtnXpath));
        waitUtilElementToBeClickable(checkoutBtn);
        checkoutBtn.click();

        //переключить слайдер "Являюсь гражданином РФ"
        WebElement isResidentSlider = driver.findElement(By.xpath(isResidentXpath));
        waitUtilElementToBeClickable(isResidentSlider);
        isResidentSlider.click();

        Assertions.assertEquals("Паспорт иностранного гражданина",
                driver.findElement(By.xpath("//h3[contains(text(), 'гражданина')]")).getText(),
                "Слайдер не переключился");

        //нажать кнопку "продолжить"
        WebElement submitBtn = driver.findElement(By.xpath("//button[@type='submit']"));
        submitBtn.click();

        //проверить все сообщения об ошибке обязательных полей
        Assertions.assertAll(
                () -> elementsXpathAndErrorMessage.forEach(this::checkErrorMessageAtField)
        );

        //заполнить форму (знаяения проверяются при заполнении)
        fillInputFieldDropDownByXpath(fullNameXpath, "Иванов Иван Иванович");
        fillSlowInputMaskFieldByXpath(birthDateXpath, "01012000", "01.01.2000");
        fillInputFieldByXpath(birthPlaceXpath, "Москва");

        WebElement genderMaleRadioBtn = driver.findElement(By.xpath(genderMaleXpath));
        waitUtilElementToBeClickable(genderMaleRadioBtn);
        genderMaleRadioBtn.click();

        WebElement citizenshipSelect = driver.findElement(By.xpath(citizenshipSelectXpath));
        citizenshipSelect.click();
        selectCountry("Германия");

        Assertions.assertEquals("Германия",
                driver.findElement(By.xpath(citizenshipSelectXpath)).getText(),
                "Страна не выбрана");

        fillInputFieldByXpath(foreignSeriesXpath, "1234");
        fillInputFieldByXpath(foreignNumberXpath, "123456789");
        fillSlowInputMaskFieldByXpath(foreignIssuedDateXpath, "01012020", "01.01.2020");
        fillInputFieldByXpath(foreignIssuedByXpath, "выдан выдан");
        fillSlowInputMaskFieldByXpath(phoneXpath, "1111111111", "+7 (111) 111-11-11");
        fillInputFieldDropDownByXpath(registrationAddressByXpath, "г Санкт-Петербург, ул 10-я Советская, д 9");

        //нажать кнопку "продолжить"
        submitBtn.click();

        //проверить, что уведомления об ошибке заполненных полей не отображаются
        Assertions.assertAll(
                () -> elementsXpathList.forEach(e -> checkErrorMessageAtField(e, ""))
        );
    }
}