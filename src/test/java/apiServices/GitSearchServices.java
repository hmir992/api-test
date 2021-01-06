package apiServices;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Test;
import util.GetProperties;

import java.io.IOException;
import java.util.HashMap;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class GitSearchServices {

    private final static String BASE_PATH = "search";

    public Response getSearch(String pathParam, String q, String sort, String order) throws IOException {
        GetProperties getProp = new GetProperties();

        RequestSpecification request = RestAssured
                .given()
                .header("Accept", "application/vnd.github.v3+json")
                .urlEncodingEnabled(false)
                .basePath(BASE_PATH)
                .baseUri(getProp.getBaseUrl())
                .queryParam("q", q)
                .pathParam("pathParam", pathParam)
                .when();
        if (sort != null)
            request.queryParam("sort", sort);

        if (order != null)
            request.queryParam("order", order);

        return request.get("/{pathParam}")
                .then()
                .log()
                .ifStatusCodeMatches(anyOf(is(304), is(422), is(503)))
                .extract()
                .response();
    }

    @Test
    public void methodTest() throws IOException {
        Response response = getSearch("repositories", "java", null, null);
        HashMap<String, Object> result = response.as(new TypeRef<HashMap<String, Object>>() {
        });
    }
}
