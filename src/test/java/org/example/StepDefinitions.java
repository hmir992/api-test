package org.example;

import apiServices.GitSearchServices;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.*;


public class StepDefinitions {
    private Response response;

    @Given("All {string} are fetched for {string} order by {string} {string}")
    public void getSearchLanguage(String pathParam, String q, String sort, String order) throws IOException {

        GitSearchServices gitSearchServices = new GitSearchServices();
        response = gitSearchServices.getSearch(pathParam, q, sort == "any" ? null : sort, order == "any" ? null : order);
    }


    @When("Response status code is {int}")
    public void checkStatusCode(int statusCode) {
        assertEquals(statusCode, response.statusCode(), "Status code isn't equal " + statusCode);
    }


    @Then("How many repositories are there")
    public void getRepoCount() {
        HashMap<String, Object> result = response.as(new TypeRef<HashMap<String, Object>>() {
        });

        System.out.println("Number of repositories is " + result.get("total_count"));
    }

    @Then("The response should only have repositories created between {string} and {string}")
    public void getAfterDate(String date1, String date2) throws ParseException {

        HashMap<String, Object> result = response.as(new TypeRef<HashMap<String, Object>>() {
        });
        ArrayList<HashMap<String, Object>> itemList = (ArrayList<HashMap<String, Object>>) result.get("items");

        for (HashMap<String, Object> item : itemList) {
            String createdAt = (String) item.get("created_at");

            SimpleDateFormat spm = new SimpleDateFormat("yyyy-MM-dd");

            Date createdDate = spm.parse(createdAt);
            Date startDate = spm.parse(date1);
            Date endDate = spm.parse(date2);

            Calendar createdDatCal = Calendar.getInstance();
            Calendar startedDateCal = Calendar.getInstance();
            Calendar endDateCal = Calendar.getInstance();

            createdDatCal.setTime(createdDate);
            startedDateCal.setTime(startDate);
            endDateCal.setTime(endDate);

            assertTrue(createdDatCal.after(startedDateCal), "Created before the specified date");
            assertTrue(createdDatCal.before(endDateCal), "Created after the specified date");

        }
    }


    @Then("The response scheme should be validated against this {string} scheme")
    public void validateSchema(String schemaName) {
        response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaName));
    }

    @Then("The response should only have repos written in {string} and {string}")
    public void checkResponseRepoLanguages(String language1, String language2) {
        HashMap<String, Object> result = response.as(new TypeRef<HashMap<String, Object>>() {
        });
        ArrayList<HashMap<String, Object>> itemList = (ArrayList<HashMap<String, Object>>) result.get("items");

        for (HashMap<String, Object> item : itemList) {
            String language = (String) item.get("language");
            assertTrue(language.equalsIgnoreCase(language1) || language.equalsIgnoreCase(language2),
                    "There is a different programming language " +
                            "\nthan the programming languages specified in the response: \n" + language);
        }
    }

    @Then("The stars of the repositories should be between {int} and {int}")
    public void checkStars(int startStarsCount, int endStarsCount) {

        HashMap<String, Object> result = response.as(new TypeRef<HashMap<String, Object>>() {
        });
        ArrayList<HashMap<String, Object>> itemList = (ArrayList<HashMap<String, Object>>) result.get("items");

        for (HashMap<String, Object> item : itemList) {
            int starCountOfRepo = Integer.parseInt(String.valueOf(item.get("stargazers_count")));
            assertTrue(starCountOfRepo > startStarsCount, "The total number of stars is less than stated!");
            assertTrue(starCountOfRepo < endStarsCount, "The total number of stars is more than stated!");

        }

    }

    @Then("The repositories in the response must be order in {string} order according to the {string}.")
    public void checkRepoSortOrderForIntCount(String type, String accordingItem) {
        HashMap<String, Object> result = response.as(new TypeRef<HashMap<String, Object>>() {
        });
        ArrayList<HashMap<String, Object>> itemList = (ArrayList<HashMap<String, Object>>) result.get("items");



        for (int i = 0; i < itemList.size(); i++) {
            if (i + 1 != itemList.size()) {
                int starsOfThePreviousRepo = Integer.parseInt(String.valueOf(itemList.get(i).get(accordingItem)));
                int starsOfTheNextRepo = Integer.parseInt(String.valueOf(itemList.get(i + 1).get(accordingItem)));
                if (type.equalsIgnoreCase("decreasing"))
                    assertTrue(starsOfThePreviousRepo > starsOfTheNextRepo);
                else if (type.equalsIgnoreCase("ascending"))
                    assertTrue(starsOfThePreviousRepo > starsOfTheNextRepo);
                else
                    fail("The sort type is wrong, the type must be descending or ascending, but the type given is " + type);
            }
        }
    }

    @Then("The owner of the warehouses in the response should be {string}")
    public void checkUserName(String userName) {

        HashMap<String, Object> result = response.as(new TypeRef<HashMap<String, Object>>() {
        });
        ArrayList<HashMap<String, Object>> itemList = (ArrayList<HashMap<String, Object>>) result.get("items");

        for (HashMap<String, Object> item : itemList) {
            String ownerName = ((HashMap<String, String>) item.get("owner")).get("login");
            assertEquals(userName, ownerName, "username is different than expected");
        }
    }

    @Then("Specify a maximum number of results")
    public void maximumStarNumber() {

        HashMap<String, Object> result = response.as(new TypeRef<HashMap<String, Object>>() {
        });
        ArrayList<HashMap<String, Object>> itemList = (ArrayList<HashMap<String, Object>>) result.get("items");

        int maximumStar = Integer.parseInt(String.valueOf(itemList.get(0).get("stargazers_count")));
        System.out.println("Maximum stars number is: " + maximumStar);
    }
}
