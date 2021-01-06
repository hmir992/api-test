@All
Feature: Git Search

  Scenario: Fetching the total number of Github repositories for a given programming language
    Given All "repositories" are fetched for "language:java+language:Python" order by "any" "any"
    When Response status code is 200
    Then  The response scheme should be validated against this "gitSearchSchema.json" scheme
    Then The response should only have repos written in "java" and "Python"
    Then How many repositories are there

  Scenario: Filter by a given creation date
    Given All "repositories" are fetched for "language:java+language:Go+created:2016-04-30..2019-07-04" order by "any" "any"
    When Response status code is 200
    Then The response scheme should be validated against this "gitSearchSchema.json" scheme
    Then The response should only have repos written in "java" and "Go"
    Then The response should only have repositories created between "2016-04-30" and "2019-07-04"
    Then How many repositories are there

  Scenario: Fetch repositories for a specific user
    Given All "repositories" are fetched for "user:hmir992" order by "any" "any"
    When Response status code is 200
    Then The response scheme should be validated against this "gitSearchSchema.json" scheme
    Then The owner of the warehouses in the response should be "hmir992"
    Then How many repositories are there

  Scenario: Fetch the most starred Github repositories and order the results decreasingly.
    Given All "repositories" are fetched for "stars:1000..10000000" order by "stars" "desc"
    When Response status code is 200
    Then The response scheme should be validated against this "gitSearchSchema.json" scheme
    Then The stars of the repositories should be between 1000 and 10000000
    Then The repositories in the response must be order in "decreasing" order according to the "stargazers_count".
    Then How many repositories are there

  Scenario: Specify a maximum number of results
    Given All "repositories" are fetched for "stars:1000..10000000" order by "stars" "desc"
    When Response status code is 200
    Then The response scheme should be validated against this "gitSearchSchema.json" scheme
    Then Specify a maximum number of results

  Scenario: Failure request verifications
    Given All "repositories" are fetched for "" order by "stars" "desc"
    When Response status code is 422
    Then The response scheme should be validated against this "failSchema.json" scheme




