package com.restful.booker.testsuite;

import com.restful.booker.info.BookingSteps;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.anything;

@RunWith(SerenityRunner.class)
public class BookingCRUDTest extends TestBase {

    public static String username = "admin";
    public static String password = "password123";
    public static String firstname = "Sia" + TestUtils.getRandomValue();
    public static String lastname = "Hardik" + TestUtils.getRandomValue();

    public static Integer totalprice = 155;
    public static Boolean depositpaid = true;
    public static String additionalneeds = "Breakfast";
    public static int bookingID;
    public static String token;

    @Steps
    BookingSteps bookingSteps;

    @Title("This will pass authantication")
    @Test
    public void test001() {
        ValidatableResponse response = bookingSteps.auth(username, password);
        response.log().all().statusCode(200);
        token = response.log().all().extract().path("token");
    }

    @Title("This test will create a new Booking")
    @Test
    public void test002() {
        HashMap<Object, Object> bookingsDatesMap = new HashMap<>();
        bookingsDatesMap.put("checkin", "2015-12-01");
        bookingsDatesMap.put("checkout", "2015-12-05");
        ValidatableResponse response = bookingSteps.createBooking(firstname, lastname, totalprice, depositpaid, bookingsDatesMap, additionalneeds);
        response.log().all().statusCode(200);
        bookingID = response.log().all().extract().path("bookingid");
        HashMap<?, ?> bookingMap = response.log().all().extract().path("");
        Assert.assertThat(bookingMap, anything(firstname));
        System.out.println(token);
    }

    @Title("This test will Update the booking")
    @Test
    public void test003() {
        HashMap<Object, Object> bookingsDatesMap = new HashMap<>();
        bookingsDatesMap.put("checkin", "2015-12-01");
        bookingsDatesMap.put("checkout", "2015-12-05");
        firstname = firstname + "_updated";
        ValidatableResponse response = bookingSteps.updateBooking(bookingID, firstname, lastname, totalprice, depositpaid, bookingsDatesMap, additionalneeds);
        response.log().all().statusCode(200);
        HashMap<?, ?> bookingMap = response.log().all().extract().path("");
        Assert.assertThat(bookingMap, anything(firstname));
        System.out.println(token);
    }

    @Title("This test will Deleted the booking")
    @Test
    public void test004() {

        ValidatableResponse response = bookingSteps.deleteBooking(bookingID);
        response.log().all().statusCode(201);
        ValidatableResponse response1 = bookingSteps.getBookingByID(bookingID);
        response1.log().all().statusCode(404);
    }
}