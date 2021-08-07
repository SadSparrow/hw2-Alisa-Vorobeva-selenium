package org.sparrow;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.sparrow.base.BaseTests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractRaiffeisenTest extends BaseTests {
    /*protected String fullNameXpath = "//input[@name='fullName']";
    protected String birthDateXpath = "//input[@name='birthDate']";
    protected String birthPlaceXpath = "//input[@name='birthPlace']";
    protected String genderMaleXpath = "//label[@data-marker='Radio.constructor.Box'][1]";//genderMaleXpath = "//input[@name='gender']"; некликабелен
    protected String isResidentXpath = "//span[@data-marker='Switcher.Jackdaw']";
    protected String citizenshipSelectXpath = "//div[@data-marker='Select.value.Selectarea']";
    protected String foreignSeriesXpath = "//input[@name='foreignSeries']";
    protected String foreignNumberXpath = "//input[@name='foreignNumber']";
    protected String foreignIssuedDateXpath = "//input[@name='foreignIssuedDate']";
    protected String foreignIssuedByXpath = "//input[@name='foreignIssuedBy']";
    protected String registrationAddressByXpath = "//input[@name='registrationAddress']";
    protected String phoneXpath = "//input[@name='phone']";*/

    //переписанные xpath - лучше или нет?
    private final String parent = "//div[contains(@data-marker, 'Fieldset')]//*[@data-marker-field='%s']";
    protected String fullNameXpath = String.format(parent, "fullName");
    protected String birthDateXpath = String.format(parent, "birthDate");
    protected String birthPlaceXpath = String.format(parent, "birthPlace");
    private final String genderXpath = String.format(parent, "gender") + "//input[@value='%s']/..";
    protected String genderMaleXpath = String.format(genderXpath, "M");
    protected String genderFemaleXpath = String.format(genderXpath, "F");
    protected String isResidentXpath = "//span[@data-marker='Switcher.Jackdaw']";
    //String.format(parent, "isResident") + "/span[@data-marker='Switcher.Jackdaw']";
    protected String citizenshipSelectXpath = String.format(parent, "citizenship") + "/div";
    protected String foreignSeriesXpath = String.format(parent, "foreignSeries");
    protected String foreignNumberXpath = String.format(parent, "foreignNumber");
    protected String foreignIssuedDateXpath = String.format(parent, "foreignIssuedDate");
    protected String foreignIssuedByXpath = String.format(parent, "foreignIssuedBy");
    protected String registrationAddressByXpath = String.format(parent, "registrationAddress");
    protected String salaryXpath = String.format(parent, "salary");
    protected String acceptSalaryXpath = String.format(parent, "acceptSalary");
    protected String pledgeAddressXpath = String.format(parent, "pledgeAddress");
    protected String emailXpath = String.format(parent, "email");
    protected String phoneXpath = String.format(parent, "phone");
    protected String agreementXpath = String.format(parent, "agreement");
    protected String agreementCloseXpath = "//div[@data-marker='Modal.Close']";

    //список полей, которые будут заполнены
    protected List<String> elementsXpathList = new ArrayList<>(Arrays.asList(
            fullNameXpath,
            birthDateXpath,
            birthPlaceXpath,
            genderMaleXpath,
            citizenshipSelectXpath,
            foreignNumberXpath,
            foreignIssuedDateXpath,
            foreignIssuedByXpath,
            registrationAddressByXpath,
            phoneXpath
    ));

    //map полей обязательных для заполнения и текста сообщений об ошибке
    protected Map<String, String> elementsXpathAndErrorMessage = Map.ofEntries(
            Map.entry(fullNameXpath, "Поле обязательно для заполнения"),
            Map.entry(birthDateXpath, "Поле обязательно для заполнения"),
            Map.entry(birthPlaceXpath, "Поле обязательно для заполнения"),
            Map.entry(genderMaleXpath, "Поле обязательно для заполнения"),
            Map.entry(citizenshipSelectXpath, "Поле обязательно для заполнения"),
            Map.entry(foreignNumberXpath, "Поле обязательно для заполнения"),
            Map.entry(foreignIssuedByXpath, "Поле обязательно для заполнения"),
            Map.entry(registrationAddressByXpath, "Некорректный адрес"),
            Map.entry(salaryXpath, "Поле обязательно для заполнения"),
            Map.entry(acceptSalaryXpath, "Поле обязательно для заполнения"),
            Map.entry(pledgeAddressXpath, "Выберите из списка"),
            Map.entry(emailXpath, "Поле обязательно для заполнения"),
            Map.entry(phoneXpath, "Поле обязательно для заполнения"),
            Map.entry(agreementXpath, "Поле обязательно для заполнения")
    );

    //Явное ожидание того что элемент станет кликабельным
    protected void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    //заполнение поля input по xpath
    protected void fillInputFieldByXpath(String xpath, String value) {
        WebElement element = driver.findElement(By.xpath(xpath));
        fillInputField(element, value);
    }

    //заполнение поля input с выпадающим списком по xpath
    protected void fillInputFieldDropDownByXpath(String xpath, String value) {
        WebElement element = driver.findElement(By.xpath(xpath));
        fillInputField(element, value);
        WebElement dropDown = element.findElement(By.xpath("./../../div[@data-marker='Autocomplete.value.DropdownBox']"));
        dropDown.click();
    }

    //вынесен дублирующий код из fillInputFieldByXpath и fillInputFieldDropDownByXpath
    private void fillInputField(WebElement element, String value) {
        waitUtilElementToBeClickable(element);
        element.click();
        element.clear();
        element.sendKeys(value);
        Assertions.assertEquals(value, element.getAttribute("value"), "Поле было заполнено некорректно");
        System.out.println(element.getAttribute("value"));
    }

    //медленное заполнение для полей с маской
    protected void fillSlowInputMaskFieldByXpath(String xpath, String value, String result) {
        WebElement element = driver.findElement(By.xpath(xpath));
        waitUtilElementToBeClickable(element);
        element.click();
//        element.clear();
//        char[] chars = value.toCharArray();
//
//        for (char aChar : chars) {
//            element.click();
//            element.sendKeys(aChar + "");
//        }
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.chord(Keys.CONTROL, "A"))
                .sendKeys(Keys.BACK_SPACE)
                .sendKeys(value)
                .build()
                .perform();
        Assertions.assertEquals(result, element.getAttribute("value"), "Поле было заполнено некорректно");
        System.out.println(element.getAttribute("value"));
    }

    //выбрать страну из списка
    protected void selectCountry(String countryValue) {
        WebElement country = driver.findElement(By.xpath("//div[@data-marker='Item.Value' and contains(text(), '" + countryValue + "')]"));
        country.click();
    }

    //проверка уведомления об ошибке у поля
    protected void checkErrorMessageAtField(String xpath, String errorMessage) {
        WebElement element = driver.findElement(By.xpath(xpath + "/../div[@data-marker='Fieldset.value.Error']" +
                " | " + xpath + "/../../div[@data-marker='Fieldset.value.Error']" +
                " | " + xpath + "/../../../div[@data-marker='Fieldset.value.Error']"));
        Assertions.assertEquals(errorMessage, element.getText(), "Проверка ошибки у поля не была пройдена");
    }
}