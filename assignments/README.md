# Assignments
This repository contains a local version of [Andy](https://github.com/cse1110/andy) including exercises that can be solved and graded. To use this repository locally you have to [clone it](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository) to your own machine.

## Importing an Exercise
To open an exercise, you have to choose a folder which corresponds to a category of an exercise. In that folder, there is a list of exercise from which you can choose, each one following the same basic structure. Inside every exercise, there is a `README.md` file explaining the exercise and a `pom.xml` file which contains the full project structure. To work on an exercise, you have to import this `pom.xml` file into your preferred IDE, e.g. IntelliJ or Eclipse.

## Structure of an Exercise
Every exercise contains three folders: `config`, `solution` and `src`. The folders have the following functionality:
- `config`: contains a `Configuration.java` file, which is used by `andy` for grading your exercise. You do not have to change this, but if you want to have a different type of grading, check out the [Andy](https://github.com/cse1110/andy) repository on how to change this file.
- `solution`: contains a `ExerciseNameTest.java` file, which is a solution to the exercise. You could use this if you're stuck.
- `src`: contains two files, `ExerciseName.java` and `ExerciseNameTest.java`. The file without `Test` is the file that you need to test, the file including `Test` is the file where you have to write your solution.

For `andy` to work properly, you should not add any other files to the exercises. If you add any files, we cannot guarantee it will work correctly.

## Solving an Exercise
The only thing you have to do is write your tests inside the `ExerciseNameTest.java` file inside the `src` folder, not in the `solution` folder.

## Grading an Exercise
Grading an exercise goes through the command line or through a Maven plugin. For the command line you need to have [Maven](https://maven.apache.org/install.html) installed.

### Command Line
You have to ensure you are in the same directory the `pom.xml` file is in. If you are in that directory you can just run: `mvn andy:run`. This will output your errors, results and grades to the console.

### Maven Plugin
If you have a Maven Plugin integration with your IDE, you would only have to look for the plugins under the category `andy` and run the `andy:run` plugin. This will run Andy in a new window and print the errors, results and grades there.

## Requirements

We use Java 17 for all the exercises and in the grading tool. We can't promise things will work in other versions (you know Java, don't you?)

## License

This repository is licensed under Creative Commons. Some exercises point to websites that inspired us, and those websites have their own licenses.
