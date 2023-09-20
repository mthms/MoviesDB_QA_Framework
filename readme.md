# Automation Framework for Web, Android, and iOS App Testing

This automation framework provides support for testing web, Android, and iOS applications. It is built using Java and requires the IntelliJ IDE, Java 20 or higher, Appium 2.0, and the appropriate Appium drivers.

## Prerequisites

Before running the project, ensure that you have the following:

- IntelliJ IDE
- Java 20 or higher installed
- Appium 2.0 installed. Refer to the official Appium documentation at [https://appium.io/docs/en/2.0/intro/](https://appium.io/docs/en/2.0/intro/) for installation instructions.
- Appium Driver `uiAutomator2` installed. Use the command `appium driver install uiautomator2` to install it.
- Appium Driver `XCUITest` installed. Use the command `appium driver install xcuitest` to install it.
- chromeDriver to be downloaded and added to the resources directory
- Emulator and device configurations matching the current environment. Ensure that you have the necessary emulator configurations set up for Android testing and the required device configurations for iOS testing. These configurations should match the specifications of the emulators and devices available on your local development environment.

Please ensure that all the necessary tools and dependencies are properly installed and configured before proceeding.

## Configuration

The configuration for the framework is managed through the `config.properties` file. It provides feature flags to control various setup steps. You can enable or disable any setup step by switching the corresponding flag to `true` or `false` in the `config.properties` file.

Additionally, make sure to update the `config.properties` file with your Ngrok Auth key, which you obtained from the Ngrok website.

## Running the Project

1. Open the project in the IntelliJ IDE.
2. Build the project and make sure the build process is completed and all dependencies are loaded
3. Configure the project with the required dependencies and libraries.
4. Set up the `config.properties` file with the necessary configurations, including the Ngrok Auth key.
5. Run the desired test scripts from the test suite using the test runner provided in the project.

Ensure that the appropriate Appium drivers (`uiAutomator2` and `XCUITest`) are installed before running the tests.

## Support

If you encounter any issues or have questions related to the framework or its usage, please reach out to the project maintainers or refer to the support resources available in the project repository.

## Contributing

Contributions to the framework are welcome. To contribute, please follow these guidelines:

1. Fork the repository on GitHub.
2. Clone your forked repository to your local development environment.
3. Create a new branch for your feature or bug fix.
4. Make your changes and ensure they are well-tested.
5. Commit your changes with clear and descriptive commit messages.
6. Push your branch to your forked repository on GitHub.
7. Submit a pull request from your branch to the main repository's `master` branch.

Please note that direct pushes to the main repository are not allowed. All changes must go through a pull request for review.

Once you submit a pull request, the project maintainers will review your changes. They may provide feedback or request additional changes before merging your code into the main repository.

Thank you for your contributions to the project!