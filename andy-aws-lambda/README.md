# AWS lambda

You can deploy Andy to AWS lambda, so it gets easier for you to embedded it in any platform you use.

* Package this project, `mvn package`
* Upload the .jar to S3
* Create your AWS lambda function pointing to this jar
* Pick Java 17 as runtime
* Define `nl.tudelft.cse1110.andy.nl.tudelft.cse1110.andy.aws.AndyOnAWSLambda::handleRequest` if you are going to use AWS API Gateway to expose the lambda as a HTTP service. Or `nl.tudelft.cse1110.andy.nl.tudelft.cse1110.andy.aws.AndyOnAWSLambda::run` in case you won't.
* Call the API with the following JSON: 

```
{
  "action": "<ANDY ACTION HERE>",
  "solution": "<STUDENT SOLUTION>",
  "library": "<PRODUCTION CODE UNDER TEST>",
  "configuration": "<RUBRIC CONFIGURATION>"
}
```